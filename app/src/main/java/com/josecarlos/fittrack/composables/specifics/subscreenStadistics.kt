package com.josecarlos.fittrack.composables.specifics

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.min
import com.josecarlos.fittrack.AppPersistance
import com.josecarlos.fittrack.composables.generals.simpleButton
import com.josecarlos.fittrack.composables.generals.textHeader
import com.josecarlos.fittrack.data.dataClases.Registro
import com.josecarlos.fittrack.mvc.model.CRUDRegistros
import com.josecarlos.fittrack.ui.styles.TextSizes
import com.josecarlos.fittrack.utils.CalendarSystem.CalendarSystem.generarInformePDF
import com.josecarlos.fittrack.utils.CalendarSystem.CalendarSystem.getLunesOfYears
import com.josecarlos.fittrack.utils.CalendarSystem.getCurrentDateFormatted
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAdjusters
import kotlin.math.roundToInt

@Composable
fun subscreenStadistics(context: Context,innerPaddingValues: PaddingValues) {
    var registros by remember { mutableStateOf<List<Registro>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }

    // Load registros when the screen is first displayed
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val result = CRUDRegistros.getAllRegistrosOfUser(AppPersistance.actualUser.email)
            registros = result ?: emptyList()
            isLoading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            textHeader("Actividad Semanal", TextSizes.XXXLARGE)
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            var lista = getLunesOfYears()
            var fecha by remember { mutableStateOf(lista.second.get(lista.first).first) }
            var objetivoActual by remember { mutableStateOf(lista.first) }

            WeeklyActivityGraph(fecha = fecha, registros = registros)

            Row(Modifier.fillMaxWidth()){
                Column(Modifier.weight(0.25f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    IconButton(onClick = {
                        try{
                            objetivoActual--
                            fecha = lista.second.get(objetivoActual).first
                        }catch(e:IndexOutOfBoundsException){
                            objetivoActual++
                        }
                    }) {
                        Icon(Icons.Filled.ArrowBack, "")
                    }
                }
                Column(Modifier.weight(0.50f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    Text(lista.second.get(objetivoActual).second, textAlign = TextAlign.Center)
                }
                Column(Modifier.weight(0.25f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    IconButton(onClick = {
                        try{
                            objetivoActual++
                            fecha = lista.second.get(objetivoActual).first
                        }catch(e:IndexOutOfBoundsException){
                            objetivoActual--
                        }
                    }) {
                        Icon(Icons.Filled.ArrowForward, "")
                    }
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                ElevatedButton(onClick = {
                    generarInformePDF(context, getCurrentDateFormatted())
                }) {
                    Text("Generar informe en PDF")
                }
            }
        }

    }
}

private fun getDayNameInSpanish(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Lun"
        DayOfWeek.TUESDAY -> "Mar"
        DayOfWeek.WEDNESDAY -> "Mié"
        DayOfWeek.THURSDAY -> "Jue"
        DayOfWeek.FRIDAY -> "Vie"
        DayOfWeek.SATURDAY -> "Sáb"
        DayOfWeek.SUNDAY -> "Dom"
    }
}

private fun getFullDayNameInSpanish(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Lunes"
        DayOfWeek.TUESDAY -> "Martes"
        DayOfWeek.WEDNESDAY -> "Miércoles"
        DayOfWeek.THURSDAY -> "Jueves"
        DayOfWeek.FRIDAY -> "Viernes"
        DayOfWeek.SATURDAY -> "Sábado"
        DayOfWeek.SUNDAY -> "Domingo"
    }
}

fun stringToLocalDate(fechaString: String): LocalDate? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        LocalDate.parse(fechaString, formatter)
    } catch (e: DateTimeParseException) {
        null
    }
}

@Composable
fun WeeklyActivityGraph(fecha:String,  registros: List<Registro>) {
    val currentDate = stringToLocalDate(fecha)
    val startOfWeek = currentDate?.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val weekDays = (0..6).map { startOfWeek!!.plusDays(it.toLong()) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val durations = weekDays.map { date ->
        calculateDayTotalDuration(registros, date, dateFormatter)
    }

    Log.d("TOTAL_SEMANAL", "Total calculado: ${durations.sum()} min")

    // Find maximum duration for scaling
    val maxDuration = durations.maxOrNull() ?: 0f
    val maxHeight = 100.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            weekDays.forEachIndexed { index, date ->
                val duration = durations[index]

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(40.dp)
                ) {
                    // Duration text
                    Box(
                        modifier = Modifier.height(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (duration > 0) {
                            Text(
                                text = "${duration.toInt()}min",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

//  barras de las graficas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                weekDays.forEachIndexed { index, date ->
                    val duration = durations[index]
                    val barHeight = if (maxDuration > 0) {
                        (duration / maxDuration * maxHeight.value).dp
                    } else {
                        0.dp
                    }

                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(if (barHeight > 0.dp) barHeight else 4.dp)
                            .shadow(
                                elevation = if (duration > 0) 4.dp else 0.dp,
                                shape = RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 8.dp,
                                    bottomStart = 2.dp,
                                    bottomEnd = 2.dp
                                )
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 8.dp,
                                    bottomStart = 2.dp,
                                    bottomEnd = 2.dp
                                )
                            )
                            .background(
                                brush = if (duration > 0) {

                                    Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                        )
                                    )

                                } else {
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                            MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                                        )
                                    )
                                }
                            )
                    )
                }
            }
        }

        // Days labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            weekDays.forEach { date ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(40.dp)
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = getDayNameInSpanish(date.dayOfWeek),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (date == currentDate) FontWeight.Bold else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.BarChart,
                        contentDescription = "Resumen Semanal",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Resumen Semanal",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

                // Promedio diario
                val averageDuration = durations.average().toFloat()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Promedio diario:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = formatDuration(averageDuration),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                // Día más activo
                val maxDurationIndex = durations.indexOfFirst { it == maxDuration }
                if (maxDurationIndex != -1 && maxDuration > 0) {
                    val mostActiveDay = weekDays[maxDurationIndex]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Día más activo:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${getFullDayNameInSpanish(mostActiveDay.dayOfWeek)} ${mostActiveDay.dayOfMonth} - ${formatDuration(maxDuration)}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
                else{
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Día más activo:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "Ninguno",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

                // Total semanal
                val totalDuration = durations.sum()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total semanal:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = formatDuration(totalDuration),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                // Días activos
                val activeDays = durations.count { it > 0 }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Días activos:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "$activeDays de 7",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}

private fun formatDuration(minutes: Float): String {
    return if (minutes >= 60) {
        val hours = (minutes / 60).toInt()  // Cambiado de roundToInt() a toInt()
        val remainingMinutes = (minutes % 60).roundToInt()

        if (remainingMinutes > 0) {
            "$hours h $remainingMinutes min"
        } else {
            "$hours h"
        }
    } else {
        "${minutes.roundToInt()} min"
    }
}

private fun calculateDayTotalDuration(
    registros: List<Registro>,
    date: LocalDate,
    dateFormatter: DateTimeFormatter
): Float {
    val dateString = date.format(dateFormatter)
    return registros
        .filter { it.fecha == dateString }
        .sumOf { it.duracion.toDouble() }
        .toFloat()
}

