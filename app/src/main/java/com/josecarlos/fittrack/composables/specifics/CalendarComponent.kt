package com.josecarlos.fittrack.composables.specifics

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.josecarlos.fittrack.ui.styles.Iconos
import com.josecarlos.fittrack.utils.CalendarSystem.CalendarViewModel
import com.josecarlos.fittrack.utils.CalendarSystem.Dia
import com.josecarlos.fittrack.utils.CalendarSystem.divideDaysOfMonth
import com.josecarlos.fittrack.utils.CalendarSystem.getInitialDay

@Composable
fun MyCalendarComponent(calVM: CalendarViewModel = CalendarViewModel()) {
    val calendarState by calVM.calendarState.collectAsState()
    val greenColor = Color(0xFF388E3C)

    Column(
        Modifier.fillMaxSize().padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Registro de actividad mensual",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Header con mes y año
        ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp,
                focusedElevation = 10.dp,
                hoveredElevation = 10.dp
            )
        ) {
            Row(){
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    // Header centrado con año, mes y flechas
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { calVM.changeMes(false)}) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Mes anterior")
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(text = calendarState.actualAnio.nombre, fontSize = 18.sp)
                            Text(text = calendarState.actualMes.nombre, fontSize = 24.sp, fontWeight = FontWeight.Medium)
                        }

                        IconButton(onClick = { calVM.changeMes(true) }) {
                            Icon(Icons.Filled.ArrowForward, contentDescription = "Mes siguiente")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(Modifier.fillMaxWidth()) {
                        listOf("L", "M", "X", "J", "V", "S", "D").forEach {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Días del mes
                    val diaInicial = getInitialDay(calendarState.actualMes.dias.firstOrNull() ?: Dia())
                    val arraysDias = divideDaysOfMonth(calendarState.actualMes.dias, diaInicial)

                    arraysDias.forEach { semana ->
                        Row(Modifier.fillMaxWidth()) {
                            semana.forEach { dia ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                ) {
                                    if (dia != null) {
                                        calendarDay(dia, calVM)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    calVM.showcreateNewRegistro()
                },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = greenColor,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Añadir registro",
                        modifier = Modifier,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Añadir registro", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun calendarDay(dia: Dia, viewModel: CalendarViewModel) {
    ElevatedCard(
        onClick = {
            if (dia.registros.isNotEmpty()){
                viewModel.chargeShowDayData(dia)
                viewModel.showDay()
            }
        },
        modifier = Modifier.fillMaxSize(),
        colors = if(dia.registros.isNotEmpty())
            CardDefaults.elevatedCardColors(containerColor = Color(0xFF4CAF50))
        else
            CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            focusedElevation = 6.dp,
            hoveredElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = dia.int.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if(dia.registros.isNotEmpty()) Color.White else Color.Black
            )
        }
    }
}