package com.josecarlos.fittrack.composables.specifics

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.josecarlos.fittrack.R
import com.josecarlos.fittrack.data.Deportes
import com.josecarlos.fittrack.data.Familias
import com.josecarlos.fittrack.data.dataClases.Registro
import com.josecarlos.fittrack.data.getDeportesOfFamilia
import com.josecarlos.fittrack.data.getFamiliaDeporte
import com.josecarlos.fittrack.data.getIconForDeporte
import com.josecarlos.fittrack.mvc.controller.CalcFormulas
import com.josecarlos.fittrack.mvc.model.CRUDRegistros.createRegistro
import com.josecarlos.fittrack.ui.theme.DarkPrimary
import com.josecarlos.fittrack.ui.views.viewmodels.NewRegistroViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ScreenNewRegistro(
    onClose: () -> Unit,
    email: String,
    viewModel: NewRegistroViewModel
) {
    val state by viewModel.actualState.collectAsState()
    val scope = rememberCoroutineScope()

    var duracion by remember { mutableStateOf("") }

    var distancia by remember { mutableStateOf("") }
    var calorias by remember { mutableStateOf("") }
    var series by remember { mutableStateOf("") }
    var repeticiones by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var rounds by remember { mutableStateOf("") }
    var puntuacion by remember { mutableStateOf("") }
    var desnivel by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (state.actualStep == 1) {
                        onClose()
                    } else if (state.actualStep == 2) {
                        viewModel.changeStep(1)
                    }
                    else {
                        viewModel.changeStep(2)
                    }
                }
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = when (state.actualStep) {
                    1 -> "Seleccionar Deporte"
                    2 -> "Familia de ${state.actualFamilia.name}"
                    3 -> "Actividad:  ${state.registroNuevo.tipo.name.replace("_", " ")}"
                    else -> "Deportes ${state.actualFamilia.name}"
                },
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        when (state.actualStep) {
            1 -> {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = "Familia deportiva", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Row(Modifier.padding(start = 10.dp, top = 10.dp, bottom = 20.dp)) {
                    Text("Selecciona una Familia deportiva, para poder seleccionar un deporte concreto posteriormente")
                }
                LazyColumn {
                    item {
                        Box(Modifier.fillMaxWidth().height(100.dp).clickable {
                            viewModel.changeStep(2)
                            viewModel.changeFamilia(Familias.CARDIO)
                        }){
                            Image(painter = painterResource(R.drawable.cardio),
                                    contentDescription = "",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    colorFilter = ColorFilter.tint(DarkPrimary, BlendMode.Modulate)   )
                            Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "CARDIO",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black.copy(alpha = 0.8f),
                                            offset = Offset(1f, 1f),
                                            blurRadius = 8f
                                        )
                                    )
                                )                            }
                        }
                    }
                    item {
                        Box(Modifier.fillMaxWidth().height(100.dp).clickable {
                            viewModel.changeStep(2)
                            viewModel.changeFamilia(Familias.CALISTENIA)
                        }){
                            Image(painter = painterResource(R.drawable.bannercalistenia),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(DarkPrimary, BlendMode.Modulate)   )
                            Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "CALISTENIA",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black.copy(alpha = 0.8f),
                                            offset = Offset(1f, 1f),
                                            blurRadius = 8f
                                        )
                                    )
                                )                            }
                        }
                    }
                    item {
                        Box(Modifier.fillMaxWidth().height(100.dp).clickable {
                            viewModel.changeStep(2)
                            viewModel.changeFamilia(Familias.COMBATE)
                        }){
                            Image(painter = painterResource(R.drawable.bannercombate),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(DarkPrimary, BlendMode.Modulate)   )
                            Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "COMBATE",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black.copy(alpha = 0.8f),
                                            offset = Offset(1f, 1f),
                                            blurRadius = 8f
                                        )
                                    )
                                )
                            }
                        }
                    }
                    item {
                        Box(Modifier.fillMaxWidth().height(100.dp).clickable {
                            viewModel.changeStep(2)
                            viewModel.changeFamilia(Familias.EQUIPO)
                        }){
                            Image(painter = painterResource(R.drawable.equipobannerbein),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(DarkPrimary, BlendMode.Modulate)   )
                            Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "EQUIPO",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black.copy(alpha = 0.8f),
                                            offset = Offset(1f, 1f),
                                            blurRadius = 8f
                                        )
                                    )
                                )
                            }
                        }
                    }
                    item {
                        Box(Modifier.fillMaxWidth().height(100.dp).clickable {
                            viewModel.changeStep(2)
                            viewModel.changeFamilia(Familias.ACUATICO)
                        }){
                            Image(painter = painterResource(R.drawable.banneracuatico),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(DarkPrimary, BlendMode.Modulate)   )
                            Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "ACUATICO",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black.copy(alpha = 0.8f),
                                            offset = Offset(1f, 1f),
                                            blurRadius = 8f
                                        )
                                    )
                                )                            }
                        }
                    }
                    item {
                        Box(Modifier.fillMaxWidth().height(100.dp).clickable {
                            viewModel.changeStep(2)
                            viewModel.changeFamilia(Familias.AVENTURA)
                        }){
                            Image(painter = painterResource(R.drawable.bannersenderismo),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(DarkPrimary, BlendMode.Modulate)   )
                            Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "AVENTURA",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black.copy(alpha = 0.8f),
                                            offset = Offset(1f, 1f),
                                            blurRadius = 8f
                                        )
                                    )
                                )                            }
                        }
                    }
                }
            }
            2 -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    val lista = getDeportesOfFamilia(state.actualFamilia)
                    items(lista) { deporte ->
                        ElevatedCard(
                            onClick = { viewModel.updateRegistro(deporte) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = getIconForDeporte(deporte),
                                    contentDescription = deporte.name,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = deporte.name.replace("_", " "),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = getFamiliaDeporte(deporte).name,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
            3 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            OutlinedTextField(
                                value = duracion,
                                onValueChange = { duracion = it

                                                    viewModel.updateDuracion(duracion.toIntOrNull() ?: 0)

                                                    val k = CalcFormulas.calcularKcalQuemadas(CalcFormulas.getMetOfDeporte(state.registroNuevo.tipo).toFloat(), 60f, duracion.toIntOrNull() ?: 0)
                                                    viewModel.updateKcal(k)
                                                },
                                label = { Text("Duración (minutos)") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            when (state.registroNuevo.familia) {
                                Familias.CARDIO -> {
                                    OutlinedTextField(
                                        value = distancia,
                                        onValueChange = { distancia = it },
                                        label = { Text("Distancia (km)") },
                                        modifier = Modifier.fillMaxWidth(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = state.kcal.toString(),
                                        onValueChange = {  },
                                        label = { Text("Calorías quemadas") },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = false,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                                Familias.CALISTENIA, Familias.GIMNASIO -> {
                                    OutlinedTextField(
                                        value = series,
                                        onValueChange = { series = it },
                                        label = { Text("Series") },
                                        modifier = Modifier.fillMaxWidth(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = repeticiones,
                                        onValueChange = { repeticiones = it },
                                        label = { Text("Repeticiones") },
                                        modifier = Modifier.fillMaxWidth(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )


                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (state.registroNuevo.tipo != Deportes.FLEXIONES &&
                                        state.registroNuevo.tipo != Deportes.DOMINADAS &&
                                        state.registroNuevo.tipo != Deportes.ABDOMINALES &&
                                        state.registroNuevo.tipo != Deportes.SENTADILLAS ){
                                        OutlinedTextField(
                                            value = peso,
                                            onValueChange = { peso = it
                                                if(peso == ""){
                                                    viewModel.updatePeso(0f)
                                                }else{
                                                    viewModel.updatePeso(peso.toFloat())
                                                }
                                                val k = CalcFormulas.calcularKcalQuemadas(CalcFormulas.getMetOfDeporte(state.registroNuevo.tipo).toFloat(), 60f, state.actualDuracion)
                                                viewModel.updateKcal(k)
                                            },
                                            label = { Text("Peso (kg)") },
                                            modifier = Modifier.fillMaxWidth(),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    OutlinedTextField(
                                        value = state.kcal.toString(),
                                        onValueChange = {  },
                                        label = { Text("Calorías quemadas") },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = false,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                                Familias.COMBATE -> {
                                    OutlinedTextField(
                                        value = rounds,
                                        onValueChange = { rounds = it },
                                        label = { Text("Rounds") },
                                        modifier = Modifier.fillMaxWidth(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                                Familias.EQUIPO -> {
                                    OutlinedTextField(
                                        value = puntuacion,
                                        onValueChange = { puntuacion = it },
                                        label = { Text("Puntuación") },
                                        modifier = Modifier.fillMaxWidth(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                                Familias.ACUATICO -> {
                                    OutlinedTextField(
                                        value = distancia,
                                        onValueChange = { distancia = it },
                                        label = { Text("Distancia (m)") },
                                        modifier = Modifier.fillMaxWidth(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = calorias,
                                        onValueChange = { calorias = it },
                                        label = { Text("Calorías quemadas") },
                                        modifier = Modifier.fillMaxWidth(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                                Familias.AVENTURA -> {
                                    OutlinedTextField(
                                        value = distancia,
                                        onValueChange = { distancia = it },
                                        label = { Text("Distancia (km)") },
                                        modifier = Modifier.fillMaxWidth(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = desnivel,
                                        onValueChange = { desnivel = it },
                                        label = { Text("Desnivel (m)") },
                                        modifier = Modifier.fillMaxWidth(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                            }
                        }
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.changeStep(1) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Volver")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                val metadata = mutableMapOf<String, Any>()

                                when (state.registroNuevo.familia) {
                                    Familias.CARDIO, Familias.ACUATICO, Familias.AVENTURA -> {
                                        distancia.toFloatOrNull()?.let { metadata["distancia"] = it }
                                        calorias.toIntOrNull()?.let { metadata["calorias"] = it }
                                    }
                                    Familias.CALISTENIA, Familias.GIMNASIO -> {
                                        series.toIntOrNull()?.let { metadata["series"] = it }
                                        repeticiones.toIntOrNull()?.let { metadata["repeticiones"] = it }
                                        if (state.registroNuevo.tipo != Deportes.FLEXIONES &&
                                            state.registroNuevo.tipo != Deportes.DOMINADAS &&
                                            state.registroNuevo.tipo != Deportes.ABDOMINALES &&
                                            state.registroNuevo.tipo != Deportes.SENTADILLAS ){
                                            peso.toFloatOrNull()?.let { metadata["peso"] = it }
                                        }
                                    }
                                    Familias.COMBATE -> {
                                        rounds.toIntOrNull()?.let { metadata["rounds"] = it }
                                    }
                                    Familias.EQUIPO -> {
                                        puntuacion.toIntOrNull()?.let { metadata["puntuacion"] = it }
                                    }
                                    Familias.AVENTURA -> {
                                        desnivel.toIntOrNull()?.let { metadata["desnivel"] = it }
                                    }
                                }

                                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                val registro = Registro(
                                    fecha = dateFormat.format(Date()),
                                    duracion = duracion.toFloatOrNull() ?: 0f,
                                    familia = state.registroNuevo.familia,
                                    tipo = state.registroNuevo.tipo,
                                    metadata = metadata
                                )

                                if (createRegistro(registro, email)) {
                                    viewModel.changeStep(1)
                                    onClose()
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = duracion.isNotEmpty()
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}
//TODO CAMBIAR 60KG EN FUNCIONES DE CALCULOS DE CALORIAS, CUANDO EL USUARIO PUEDA INICIAR SESION





