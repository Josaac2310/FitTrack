package com.josecarlos.fittrack.composables.specifics

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.josecarlos.fittrack.AppPersistance
import com.josecarlos.fittrack.data.Deportes
import com.josecarlos.fittrack.data.Familias
import com.josecarlos.fittrack.data.dataClases.Registro
import com.josecarlos.fittrack.data.getIconForDeporte
import com.josecarlos.fittrack.mvc.controller.CalcFormulas
import com.josecarlos.fittrack.mvc.model.CRUDRegistros
import com.josecarlos.fittrack.utils.CalendarSystem.Anio
import com.josecarlos.fittrack.utils.CalendarSystem.CalendarSystem
import com.josecarlos.fittrack.utils.CalendarSystem.CalendarViewModel
import com.josecarlos.fittrack.utils.CalendarSystem.Dia
import com.josecarlos.fittrack.utils.CalendarSystem.Mes
import kotlinx.coroutines.launch

@Composable
fun DayView(
    innerPadding: PaddingValues,
    dia: Dia,
    onClose: () -> Unit,
    mes: Mes,
    anio: Anio,
    calendarViewModel: CalendarViewModel
) {
    val scope = rememberCoroutineScope()
    var registros by remember { mutableStateOf(dia.registros) }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                                try {
                                    Log.d("BackArrow", "Recargando datos del calendario...")
                                    CalendarSystem.chargeCalendar()
                                    val nuevosRegistros = CRUDRegistros.getAllRegistrosOfUser(AppPersistance.actualUser.email)
                                    nuevosRegistros?.let {
                                        CalendarSystem.initData(it)
                                        Log.d("BackArrow", "Datos del calendario actualizados")
                                    }
                                    onClose()
                                } catch (e: Exception) {
                                    Log.e("BackArrow", "Error al recargar datos: ${e.message}")
                                    onClose()
                                }
                            }
                        }
                        .padding(8.dp), // Padding para hacer el área clickeable más grande
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Volver atrás"
                            )
                        }
                    }

                    Text(
                        text = "Volver",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 24.sp,  // elige el tamaño que prefieras
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = anio.nombre,
                                fontSize = 12.sp
                            )
                            Text(
                                text = mes.nombre,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(80.dp),
                            tonalElevation = 6.dp
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = dia.int.toString(),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = dia.nombre.take(3).uppercase(),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            if (dia.registros.isEmpty()) {
                item {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                Icons.Filled.Sports,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "No hay registros para este día",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(registros.size) { i ->
                    val reg = dia.registros[i]
                    var expanded by remember { mutableStateOf(false) }

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { expanded = !expanded },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Card principal con información básica
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Icono del deporte
                                Surface(
                                    shape = CircleShape,
                                    modifier = Modifier.size(60.dp),
                                    tonalElevation = 2.dp
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Icon(
                                            getIconForDeporte(reg.tipo),
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }

                                // Información principal
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = reg.tipo.toString(),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = reg.familia.toString(),
                                        fontSize = 14.sp
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.wrapContentSize(),
                                        tonalElevation = 1.dp
                                    ) {
                                        Text(
                                            text = getDuracionFormatted(reg.duracion),
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                // Botones de acción
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                try {
                                                    Log.d("DeleteRegistro", "Intentando borrar registro...")
                                                    val deleted = CRUDRegistros.deleteRegistro(reg, AppPersistance.actualUser.email)
                                                    Log.d("DeleteRegistro", "Borrado exitoso? $deleted")
                                                    if (deleted) {
                                                        // Actualizamos el CalendarViewModel
                                                        calendarViewModel.setUnCharged()
                                                        CalendarSystem.chargeCalendar()
                                                        val nuevosRegistros = CRUDRegistros.getAllRegistrosOfUser(AppPersistance.actualUser.email)
                                                        nuevosRegistros?.let {
                                                            CalendarSystem.initData(it)
                                                            calendarViewModel.chargeAnios(CalendarSystem.anios)
                                                            calendarViewModel.setCharged()
                                                        }
                                                        // Volvemos a la vista del calendario
                                                        onClose()
                                                    } else {
                                                        Log.d("DeleteRegistro", "No se pudo borrar el registro")
                                                    }
                                                } catch (e: Exception) {
                                                    Log.d("DeleteRegistro", "Error en el proceso: ${e.message}")
                                                }
                                            }
                                        },
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Borrar registro",
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Icon(
                                        if (expanded) Icons.Filled.KeyboardArrowUp
                                        else Icons.Filled.KeyboardArrowDown,
                                        contentDescription = if (expanded) "Contraer" else "Expandir"
                                    )
                                }
                            }

                            // Panel expandido con detalles
                            // Panel expandido con detalles
                            if (expanded) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 1.dp
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Detalles del ejercicio",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    // Metadatos del registro
                                    reg.metadata.entries.forEach { (key, value) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = key,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Medium
                                                )
                                            )
                                            Text(
                                                text = value.toString(),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }

                                    // Cálculos específicos para cardio y acuático
                                    if (reg.familia == Familias.CARDIO || reg.familia == Familias.ACUATICO) {
                                        val ritmo = CalcFormulas.getRitmoMedio(
                                            reg.duracion.toInt(),
                                            reg.metadata.get("distancia").toString().toFloat()
                                        )

                                        val (ritmoTexto, unidades) = when (reg.tipo) {
                                            Deportes.BICICLETA, Deportes.ELIPTICA, Deportes.CINTA_DE_CORRER -> {
                                                val ritmoKmH = ritmo * 60
                                                Pair(String.format("%.2f", ritmoKmH), "km/h")
                                            }
                                            else -> {
                                                val ritmoMMin = ritmo * 1000
                                                Pair(String.format("%.0f", ritmoMMin), "m/min")
                                            }
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth(),
                                            tonalElevation = 2.dp
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Ritmo medio:",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                )
                                                Text(
                                                    text = "$ritmoTexto $unidades",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                            }
                                        }
                                    }

                                    // Cálculo de pasos para cardio
                                    if (reg.familia == Familias.CARDIO) {
                                        val longitudPaso = CalcFormulas.getLongitudPasos(162)
                                        val totalPasos = CalcFormulas.getTotalPasos(
                                            reg.metadata.get("distancia").toString().toFloat().toInt(),
                                            longitudPaso
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth(),
                                            tonalElevation = 2.dp
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Total pasos:",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                )
                                                Text(
                                                    text = "${totalPasos.toInt()} pasos",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                            }
                                        }
                                    }

                                    // Cálculos de calorías (MOVIDO AL FINAL)
                                    val totalKcal = CalcFormulas.calcularKcalQuemadas(
                                        CalcFormulas.getMetOfDeporte(reg.tipo).toFloat(),
                                        60f,
                                        reg.duracion.toInt()
                                    )
                                    val kcalPorMin = CalcFormulas.getKcalxMin(totalKcal, reg.duracion.toInt())

                                    ElevatedCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "Calorías quemadas",
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Total:",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = "$totalKcal kcal",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                )
                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Por minuto:",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = "$kcalPorMin kcal/min",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getDuracionFormatted(tiempo: Float): String {
    if (tiempo > 60) {
        val horas = tiempo / 60
        val horasInt = horas.toInt()
        val minutos = tiempo - (60 * horasInt)
        return "${horasInt}h y ${minutos.toInt()} min"
    } else {
        return "${tiempo.toInt()} min"
    }
}