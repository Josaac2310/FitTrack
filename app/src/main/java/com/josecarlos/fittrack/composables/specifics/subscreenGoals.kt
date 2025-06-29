package com.josecarlos.fittrack.composables.specifics

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.josecarlos.fittrack.AppPersistance
import com.josecarlos.fittrack.R
import com.josecarlos.fittrack.composables.generals.botBar
import com.josecarlos.fittrack.composables.generals.floatButton
import com.josecarlos.fittrack.composables.generals.textHeader
import com.josecarlos.fittrack.composables.generals.topBar
import com.josecarlos.fittrack.data.MetaRegType
import com.josecarlos.fittrack.data.MetaType
import com.josecarlos.fittrack.data.dataClases.Caducidad
import com.josecarlos.fittrack.data.dataClases.Meta
import com.josecarlos.fittrack.data.dataClases.MetaCaduca
import com.josecarlos.fittrack.data.dataClases.MetaDiaria
import com.josecarlos.fittrack.data.dataClases.MetaReg
import com.josecarlos.fittrack.data.dataClases.RegistroMeta
import com.josecarlos.fittrack.mvc.controller.CalcFormulas
import com.josecarlos.fittrack.mvc.model.CRUDMetas
import com.josecarlos.fittrack.ui.styles.TextSizes
import com.josecarlos.fittrack.ui.theme.DarkPrimary
import com.josecarlos.fittrack.ui.views.viewmodels.MetasViewModel
import com.josecarlos.fittrack.utils.CalendarSystem.CalendarSystem.getTodayDia
import com.josecarlos.fittrack.utils.CalendarSystem.Dia
import com.josecarlos.fittrack.utils.CalendarSystem.diasDeDiferencia
import com.josecarlos.fittrack.utils.CalendarSystem.getCurrentDateFormatted
import com.josecarlos.fittrack.utils.Responsive.getResponsiveHeight
import com.josecarlos.fittrack.utils.hasRegisterForToday
import com.josecarlos.fittrack.utils.isConseguido
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.annotation.meta.When

@Composable
fun subscreenGoals(context: Context,paddingValues: PaddingValues, viewModel: MetasViewModel) {

    var charging by remember { mutableStateOf(true) }
    var metas by remember { mutableStateOf(arrayListOf<Meta>()) }
    var metasFijas by remember { mutableStateOf(arrayListOf<MetaDiaria>()) }

    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState.recharge){
        metas = CRUDMetas.getMetasOfUsuario(AppPersistance.actualUser.email)
        metasFijas = metas.filter { m ->
            m.metaReg.deporte == "general"
        } as ArrayList<MetaDiaria>

        val metasFijasAUX = metasFijas
        val metasFijasAux2 = arrayListOf<MetaDiaria>()
        metasFijasAUX.filter { f -> f.metaReg.tipo == MetaRegType.CALORIAS }.firstOrNull()?.let { metasFijasAux2.add(it) }
        metasFijasAUX.filter { f -> f.metaReg.tipo == MetaRegType.TIEMPO }.firstOrNull()?.let { metasFijasAux2.add(it) }
        metasFijas.clear()
        metasFijas.add(metasFijasAux2.get(0))
        metasFijas.add(metasFijasAux2.get(1))

        metas = metas.filter { m ->
            m.metaReg.deporte != "general"
        } as ArrayList<Meta>


        charging = false
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if(charging){
            CircularProgressIndicator(Modifier.size(50.dp))
        }
        else{
            Column(modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(0.15f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    goalIndicator(R.drawable.fuego, metasFijas.get(0))
                    goalIndicator(R.drawable.crono, metasFijas.get(1))
                }

                Divider(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    thickness = 2.dp,
                    color = Color.Gray
                )

                // Contenido de metas
                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(0.45f)
                ) {
                    Text(
                        text = "Mis metas",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if(metas.isEmpty()){
                            Text(
                                text = "Aún no tienes metas registradas.",
                                color = Color.DarkGray,
                                fontSize = 16.sp
                            )
                        }
                        else{
                           LazyColumn(Modifier.padding(horizontal = 16.dp)) {
                               items(metas.size) { i ->
                                   val m = metas.get(i)
                                   var expanded by remember { mutableStateOf(false) }
                                   var showDialog by remember { mutableStateOf(false) }
                                   ElevatedCard(Modifier.fillMaxWidth().clickable { viewModel.showShowMetaDetail(id = m, if (m.type == "caduco") false else true) }
                                       .alpha(
                                           if(m.type == "diario"){
                                               val mm = m as MetaDiaria
                                               if(mm.activo){
                                                   if(hasRegisterForToday(m)) 0.8f
                                                   else 1f
                                               }
                                               else{
                                                   0.4f
                                               }
                                           }
                                           else{
                                               val mm = m as MetaCaduca
                                               if(mm.cancelado || mm.finalizado){
                                                   0.4f
                                               }
                                               else{
                                                   if(hasRegisterForToday(m)) 0.8f
                                                   else 1f
                                               }
                                           }
                                           ))
                                   {
                                       Column(Modifier.padding(16.dp)) {
                                           Row(Modifier.fillMaxWidth().padding(vertical = 15.dp)){
                                               Column(Modifier.fillMaxWidth().weight(0.2f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start){
                                                   Icon(
                                                       when(m.metaReg.tipo){
                                                           MetaRegType.CALORIAS -> Icons.Filled.LocalFireDepartment
                                                           MetaRegType.TIEMPO -> Icons.Filled.Timer
                                                           MetaRegType.DISTANCIA -> Icons.Filled.Straighten
                                                           MetaRegType.CHECK -> Icons.Filled.Check
                                                           MetaRegType.UNDEFINED -> Icons.Filled.AddReaction
                                                       }, ""
                                                   )
                                               }
                                               Column(Modifier.fillMaxWidth().weight(0.6f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                                                   Text("Deporte: "+m.metaReg.deporte)


                                               }
                                               Column(Modifier.fillMaxWidth().weight(0.2f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.End) {
                                                   Icon(Icons.Filled.ArrowCircleRight,"" )
                                               }
                                           }
                                           //Divider
                                           Row(Modifier.fillMaxWidth(),){
                                               HorizontalDivider(color = Color.DarkGray)
                                           }
                                           //Muestra el objetivo
                                           Row(Modifier.fillMaxWidth().padding(vertical = 15.dp), horizontalArrangement = Arrangement.Center){
                                               if(m.metaReg.tipo != MetaRegType.CHECK){
                                                   Text("Objetivo: "+when (m.metaReg.tipo) {
                                                       MetaRegType.DISTANCIA -> String.format(
                                                           "%.2f",
                                                           m.metaReg.valor.toFloat()
                                                       ) + " metros"

                                                       MetaRegType.TIEMPO -> String.format(
                                                           "%.2f",
                                                           m.metaReg.valor.toFloat()

                                                       ) + " minutos"

                                                       MetaRegType.CALORIAS -> String.format(
                                                           "%.2f",
                                                           m.metaReg.valor.toFloat()
                                                       ) + " calorias"

                                                       MetaRegType.CHECK -> ""
                                                       MetaRegType.UNDEFINED -> ""
                                                   })
                                               }
                                           }
                                            //Boton registrar hoy
                                           Row(Modifier.fillMaxWidth().padding(vertical = 10.dp),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                                               ElevatedButton(onClick = { showDialog = true }, enabled = if(hasRegisterForToday(m)) false else true ) {
                                                   Text("Registrar hoy")
                                               }
                                           }
                                            //Dias para terminar
                                           Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                                               if(m.type == "caduco"){
                                                   val mm = m as MetaCaduca
                                                   val dif = diasDeDiferencia(getCurrentDateFormatted(), mm.caducidad.fechaFin)
                                                   if (dif == 0){
                                                       Text("¡¡Hoy es el ultimo dia!!", color = Color.Red, fontStyle = FontStyle.Italic)
                                                   }
                                                   else{
                                                       Text("Le quedan ${dif} dias para terminar", fontStyle = FontStyle.Italic)
                                                   }
                                               }
                                           }
                                           // Informacion "ya registrado hoy"
                                           Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                                if(!hasRegisterForToday(m)){
                                                    Text("Sin registrar hoy", color = Color.Red, fontStyle = FontStyle.Italic)
                                                }
                                                else{
                                                    Text("Ya registrado hoy", color = Color.Green, fontStyle = FontStyle.Italic)
                                                }

                                           }

                                           if(expanded){
                                                Row(Modifier.fillMaxWidth()){
                                                    ElevatedCard {

                                                        m.registros.forEach{ r ->
                                                            Text("Fecha: "+r.fecha)
                                                            Text("Valor: "+r.valor+when (m.metaReg.tipo) {
                                                                MetaRegType.DISTANCIA -> " metros"

                                                                MetaRegType.TIEMPO -> " minutos"

                                                                MetaRegType.CALORIAS -> " calorias"

                                                                MetaRegType.CHECK -> ""
                                                                MetaRegType.UNDEFINED -> ""
                                                            })
                                                            HorizontalDivider()
                                                        }
                                                    }
                                                }
                                           }
                                           if(showDialog){
                                               Dialog(onDismissRequest = { showDialog = false }) {
                                                   var checked by remember { mutableStateOf(false) }
                                                   var value by remember { mutableStateOf(0f) }
                                                   ElevatedCard(Modifier.padding(16.dp)) {
                                                       Column(Modifier.padding(16.dp)) {
                                                           Text("Introduzca el valor de la meta para hoy: ")
                                                           if(m.metaReg.tipo == MetaRegType.CHECK){
                                                               Row(){
                                                                   Switch(checked = checked, onCheckedChange = { c -> checked = c })
                                                                   Text(if(checked) "Conseguido" else "No conseguido")
                                                               }
                                                           }
                                                           else{
                                                              Row(){
                                                                  Slider(value = value, onValueChange = { v -> value = v }, valueRange = when (m.metaReg.tipo) {
                                                                      MetaRegType.DISTANCIA -> 10f..20000f
                                                                      MetaRegType.TIEMPO -> 1f..360f
                                                                      MetaRegType.CALORIAS -> 40f..3000f
                                                                      MetaRegType.CHECK -> 0f..0f
                                                                      MetaRegType.UNDEFINED -> 0f..0f
                                                                  }, modifier = Modifier.fillMaxWidth(0.8f) )
                                                                  Text(value.toString())
                                                              }
                                                           }
                                                           ElevatedButton(onClick = {
                                                               CoroutineScope(Dispatchers.IO).launch {
                                                                   val ok = CRUDMetas.insertRegistroIntoMeta(AppPersistance.actualUser.email, RegistroMeta(getCurrentDateFormatted(), value.toString()+" / "+m.metaReg.valor) ,m.id  )
                                                                   showDialog = false
                                                                   CoroutineScope(Dispatchers.Main).launch {
                                                                       if(ok){
                                                                           Toast.makeText(context, "Registro creado", Toast.LENGTH_SHORT).show()
                                                                           viewModel.rechargeUI()
                                                                       }
                                                                       else{
                                                                           Toast.makeText(context, "Error al crear registro", Toast.LENGTH_SHORT).show()
                                                                       }
                                                                   }
                                                               }
                                                           } ) { Text("Registrar") }
                                                       }
                                                   }
                                               }
                                           }
                                       }
                                   }
                                   Spacer(Modifier.height(15.dp))

                               }
                           }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun subscreenNewMeta(context: Context, paddingValues: PaddingValues, viewModel: MetasViewModel){

    var showLoading by remember { mutableStateOf(false) }

    if(showLoading){
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("Creando meta...")
            LinearProgressIndicator()
        }
    }
    else {
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
            Row(
                Modifier
                    .fillMaxSize()
                    .weight(0.05f)) {}
            Row(
                Modifier
                    .fillMaxSize()
                    .weight(0.05f), horizontalArrangement = Arrangement.Start) {
                IconButton(onClick = { viewModel.hideCreateNewMeta() }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        ""
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painterResource(R.drawable.metas),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(DarkPrimary, BlendMode.Modulate),
                        modifier = Modifier.fillMaxSize()
                    )
                    // MEJORA 6: Overlay para mejorar legibilidad del texto
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                    )
                    Text(
                        "NUEVA META",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        ),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Row(
                Modifier
                    .fillMaxSize()
                    .weight(0.75f)) {
                var expanded1 by remember { mutableStateOf(false) }
                var n_tipo by remember { mutableStateOf("Diaria") }
                var n_deporte by remember { mutableStateOf("") }
                var n_tipo2 by remember { mutableStateOf(MetaRegType.TIEMPO) }
                var n_valor by remember { mutableStateOf(0f) }
                var n_fechaInicio by remember { mutableStateOf("") }
                var n_fechaFinal by remember { mutableStateOf("") }
                var expanded2 by remember { mutableStateOf(false) }

                val calendar = Calendar.getInstance()
                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

                val today = LocalDate.now()
                var date1 = remember { mutableStateOf(today.format(dateFormatter)) }
                val datePickerDialog1 = remember {
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            date1.value = "${dayOfMonth}/${month + 1}/${year}"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                }
                var date2 = remember { mutableStateOf(today.plusDays(7).format(dateFormatter)) }
                val datePickerDialog2 = remember {
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            date2.value = "${dayOfMonth}/${month + 1}/${year}"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                }

                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Flag, null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Tipo de meta",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(Modifier.width(16.dp))

                                OutlinedButton(
                                    onClick = { expanded1 = true },
                                    modifier = Modifier.width(120.dp).height(56.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (expanded1) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                    )
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(if (n_tipo.isEmpty()) "Seleccionar..." else n_tipo)
                                        Icon(
                                            if (expanded1) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                            null
                                        )
                                    }
                                }

                                DropdownMenu(expanded1, { expanded1 = false }) {
                                    DropdownMenuItem(
                                        text = { Text("Diaria") },
                                        onClick = { n_tipo = "Diaria"; expanded1 = false }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Caduca") },
                                        onClick = { n_tipo = "Caduca"; expanded1 = false }
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider()
                        Spacer(Modifier.height(7.dp))
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        OutlinedTextField(
                            value = n_deporte,
                            onValueChange = { v -> n_deporte = v },
                            label = { Text("Escriba deporte") },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.FitnessCenter,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 5.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(1.dp)) {
                            Icon(Icons.Filled.Flag, null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(5.dp))
                            Text(
                                "Tipo de registro",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(Modifier.width(12.dp))

                            Box {
                                OutlinedButton(
                                    onClick = { expanded2 = true },
                                    modifier = Modifier.width(145.dp).height(56.dp).padding(vertical = 10.dp, horizontal = 4.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (expanded2) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                    )
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(n_tipo2.toString())
                                        Icon(
                                            if (expanded2) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                            null
                                        )
                                    }
                                }

                                DropdownMenu(expanded2, { expanded2 = false }) {
                                    DropdownMenuItem(
                                        text = { Text(MetaRegType.CALORIAS.toString()) },
                                        onClick = {
                                            n_tipo2 = MetaRegType.CALORIAS; expanded2 = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(MetaRegType.CHECK.toString()) },
                                        onClick = { n_tipo2 = MetaRegType.CHECK; expanded2 = false }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(MetaRegType.TIEMPO.toString()) },
                                        onClick = {
                                            n_tipo2 = MetaRegType.TIEMPO; expanded2 = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(MetaRegType.DISTANCIA.toString()) },
                                        onClick = {
                                            n_tipo2 = MetaRegType.DISTANCIA; expanded2 = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 3.dp)
                    ) {
                        if (n_tipo2 != MetaRegType.CHECK) {

                            Text("Objetivo: ")
                            Spacer(Modifier.width(10.dp))
                            Slider(
                                modifier = Modifier.fillMaxWidth(0.55f),
                                value = n_valor,
                                onValueChange = { n -> n_valor = n },
                                valueRange = when (n_tipo2) {
                                    MetaRegType.DISTANCIA -> 10f..20000f
                                    MetaRegType.TIEMPO -> 1f..360f
                                    MetaRegType.CALORIAS -> 40f..3000f
                                    MetaRegType.CHECK -> 0f..0f
                                    MetaRegType.UNDEFINED -> 0f..0f
                                }
                            )

                            Text(
                                when (n_tipo2) {
                                    MetaRegType.DISTANCIA -> String.format(
                                        "%.2f",
                                        n_valor
                                    ) + " metros"

                                    MetaRegType.TIEMPO -> String.format(
                                        "%.2f",
                                        n_valor
                                    ) + " minutos"

                                    MetaRegType.CALORIAS -> String.format(
                                        "%.2f",
                                        n_valor
                                    ) + " calorias"

                                    MetaRegType.CHECK -> ""
                                    MetaRegType.UNDEFINED -> ""
                                }
                            )
                        }
                    }
                    if (n_tipo == "Caduca") {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ElevatedCard() {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Row {
                                        ElevatedButton(onClick = { datePickerDialog1.show() }) {
                                            Icon(Icons.Filled.CalendarMonth, "")
                                            Spacer(Modifier.width(10.dp))
                                            Text("Seleccione fecha de inicio")
                                        }
                                    }
                                    Row() {
                                        ElevatedButton(onClick = { datePickerDialog2.show() }) {
                                            Icon(Icons.Filled.CalendarMonth, "")
                                            Spacer(Modifier.width(10.dp))
                                            Text("Seleccione fecha de fin")
                                        }
                                    }
                                    Row() {
                                        Text("Fecha inicio: " + date1.value)
                                    }
                                    Row() {
                                        Text("Fecha finalizacion: " + date2.value)
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.height(25.dp))
                        ElevatedButton(onClick = {
                            var newMeta: Meta
                            if (n_tipo == "Caduca") {
                                newMeta = MetaCaduca(
                                    id = "",
                                    cancelado = false,
                                    finalizado = false,
                                    registros = arrayListOf(),
                                    type = n_tipo,
                                    metaReg = MetaReg(
                                        deporte = n_deporte,
                                        tipo = n_tipo2,
                                        valor = n_valor.toString()
                                    ),
                                    caducidad = Caducidad(
                                        fechaCancelacion = "",
                                        fechaInicio = date1.value.toString(),
                                        fechaFin = date2.value.toString()
                                    )
                                )
                                CoroutineScope(Dispatchers.IO).launch {
                                    val ok = CRUDMetas.insertMeta(
                                        MetaType.CADUCO,
                                        newMeta,
                                        AppPersistance.actualUser.email
                                    )
                                    CoroutineScope(Dispatchers.Main).launch {
                                        showLoading = true
                                        delay(1000)
                                        showLoading = false
                                        if (ok) {
                                            Toast.makeText(
                                                context,
                                                "Meta creada",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Error al crear meta",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                        viewModel.hideCreateNewMeta()
                                    }
                                }

                            } else {
                                newMeta = MetaDiaria(
                                    id = "",
                                    registros = arrayListOf(),
                                    type = n_tipo,
                                    metaReg = MetaReg(
                                        deporte = n_deporte,
                                        tipo = n_tipo2,
                                        valor = n_valor.toString()
                                    ),
                                    activo = true
                                )
                                CoroutineScope(Dispatchers.IO).launch {
                                    val ok = CRUDMetas.insertMeta(
                                        MetaType.DIARIO,
                                        newMeta,
                                        AppPersistance.actualUser.email
                                    )
                                    CoroutineScope(Dispatchers.Main).launch {
                                        showLoading = true
                                        delay(1000)
                                        showLoading = false
                                        if (ok) {
                                            Toast.makeText(
                                                context,
                                                "Meta creada",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Error al crear meta",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                        viewModel.hideCreateNewMeta()
                                    }
                                }
                            }
                        }) {
                            Text("Crear meta")
                        }
                    }
                }
            }
            Row(
                Modifier
                    .fillMaxSize()
                    .weight(0.05f)) {}
        }
    }
}

@Composable
fun goalIndicator(iconRes: Int, meta: MetaDiaria) {
    var charging by remember { mutableStateOf(true) }
    var dia by remember { mutableStateOf(Dia()) }
    var progress by remember { mutableStateOf(0f) }



    LaunchedEffect(Unit) {
        dia = getTodayDia()!!
        if (meta.metaReg.tipo == MetaRegType.CALORIAS) {
            var caloriasQuemadas = 0

            dia.registros.forEach { reg ->

                val met = CalcFormulas.getMetOfDeporte(reg.tipo).toFloat()
                val peso = 60f // Peso en kg (puedes cambiarlo según el usuario)
                val duracion = reg.duracion.toInt() // Duración en minutos

                val kcalQuemadas = CalcFormulas.calcularKcalQuemadas(met, peso, duracion)
                caloriasQuemadas += kcalQuemadas
            }

            val objetivo = meta.metaReg.valor.toFloat()

            // Asegurar que el objetivo no sea cero para evitar división por cero
            val porcentaje = if (objetivo > 0) (caloriasQuemadas / objetivo) * 100 else 0f

            // Conversión a rango 0f..1f para CircularProgressIndicator
            progress = (porcentaje / 100).coerceIn(0f, 1f)

            Log.d("Meta", "Calorías quemadas: $caloriasQuemadas, Objetivo: $objetivo, Progreso: $progress")
            charging = false
        }
        else {
            var tiempoTotal = 0

            dia.registros.forEach { reg ->
                tiempoTotal += reg.duracion.toInt()
            }

            val objetivo = meta.metaReg.valor.toFloat()

            // Asegurar que el objetivo no sea cero
            val porcentaje = if (objetivo > 0) (tiempoTotal / objetivo) * 100 else 0f

            // Escalamos a 0f..1f para el CircularProgressIndicator
            progress = (porcentaje / 100).coerceIn(0f, 1f)

            Log.d("Meta", "Tiempo total: $tiempoTotal min, Objetivo: $objetivo min, Progreso: $progress")
            charging=false
        }
    }

    Box(
        modifier = Modifier
            .size(140.dp)
            .shadow(4.dp, shape = CircleShape)
            .background(Color.White, shape = CircleShape)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if(charging){
            LinearProgressIndicator()
        }
        else{
            CircularProgressIndicator(
                strokeWidth = 10.dp,
                progress = progress,
                modifier = Modifier.size(90.dp),
                color = Color(0xFF81C784) // verde claro profesional
            )
            Image(
                    modifier = Modifier.size(50.dp),
            painter = painterResource(iconRes),
            contentDescription = null
            )
        }


    }
}

@Composable
fun subscreenMetaDetail(context: Context, viewModel: MetasViewModel, meta: Meta, isDiaria:Boolean, paddingValues: PaddingValues){

    Column(Modifier.fillMaxSize().padding(paddingValues)) {
        Row(Modifier.fillMaxSize().weight(0.1f), verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = { viewModel.hideShowMetaDetail() }) { Icon(Icons.Filled.ArrowBack,"") }
        }
        Row(Modifier.fillMaxSize().weight(0.89f).padding(horizontal = 5.dp)){
            ElevatedCard {
                Column(Modifier.fillMaxSize().padding(16.dp)) {
                    Row(Modifier.fillMaxWidth().weight(0.05f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
                        Text("Tipo: "+if (isDiaria) "Diaria" else "Caduca", fontStyle = FontStyle.Italic)
                    }
                    Row(Modifier.fillMaxWidth().weight(0.15f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                        Icon(when(meta.metaReg.tipo){
                            MetaRegType.CALORIAS -> Icons.Filled.LocalFireDepartment
                            MetaRegType.TIEMPO -> Icons.Filled.Timer
                            MetaRegType.DISTANCIA -> Icons.Filled.Straighten
                            MetaRegType.CHECK -> Icons.Filled.Check
                            MetaRegType.UNDEFINED -> Icons.Filled.AddReaction
                        }, "", modifier = Modifier.size(50.dp))
                    }
                    Row(Modifier.fillMaxWidth().weight(0.01f)){
                        HorizontalDivider()
                    }
                    Row(Modifier.fillMaxWidth().weight(0.27f)){
                        Column(Modifier.padding(horizontal = 15.dp)) {
                            Text("Deporte: "+meta.metaReg.deporte)
                            Text("Tipo de registro: "+meta.metaReg.tipo)
                            if(meta.metaReg.tipo != MetaRegType.CHECK) Text("Valor objetivo: "+meta.metaReg.valor)
                            Log.d("prueba67", isDiaria.toString())
                            if(isDiaria){
                                val mm = meta as MetaDiaria

                                ElevatedCard(
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Activo: ${if (mm.activo) "Sí" else "No"}",
                                            color = if (mm.activo) Color(0xFF4CAF50) else Color(0xFFE53E3E),
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                            }
                            else{
                                val mm = meta as MetaCaduca
                                Text("¿Ha sido cancelada? "+if(mm.cancelado) "Si" else "No", color = if(mm.cancelado) Color.Red else Color.Unspecified )
                                Text("¿Ha finalizado? "+if(mm.finalizado) "Si" else "No")

                                ElevatedCard(elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)) {
                                    Column(Modifier.padding(horizontal = 16.dp, vertical = 5.dp), horizontalAlignment = Alignment.Start) {
                                        Text("Fecha inicio: "+mm.caducidad.fechaInicio)
                                        Text("Fecha final: "+mm.caducidad.fechaFin)

                                        if(mm.cancelado){
                                            Text("Fecha de cancelacion: "+mm.caducidad.fechaCancelacion, color = Color.Red)
                                        }
                                        else{
                                            val dif = diasDeDiferencia(getCurrentDateFormatted(), mm.caducidad.fechaFin)
                                            if (dif == 0){
                                                Text("¡¡Hoy es el ultimo dia!!", color = Color.Red, fontStyle = FontStyle.Italic)
                                            }
                                            else{
                                                Text("Le quedan ${dif} dias para terminar", fontStyle = FontStyle.Italic)
                                            }
                                        }
                                    }
                                }

                            }

                        }
                    }
                    Row(Modifier.fillMaxWidth().weight(0.43f).padding(vertical = 15.dp)){
                        ElevatedCard(elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)) {
                            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Registros", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Row(Modifier.padding(top = 10.dp).alpha(0.8f)){
                                    LazyColumn() {
                                        item {
                                            Row (Modifier.padding(top = 10.dp)){
                                                Column(Modifier.fillMaxWidth().weight(0.33f), horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text("Fecha")
                                                }
                                                Column(Modifier.fillMaxWidth().weight(0.33f), horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text("Valor")
                                                }
                                                Column(Modifier.fillMaxWidth().weight(0.33f), horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text("Conseguido")
                                                }
                                            }
                                        }
                                        item {
                                            Row (Modifier.padding(vertical = 10.dp)){
                                                HorizontalDivider(color = Color.Black)
                                            }
                                        }
                                        items(meta.registros.size){ i->
                                            val reg = meta.registros.get(i)
                                            Row (modifier = if(isConseguido(reg.valor)) Modifier.background(Color.Green).padding(vertical = 10.dp) else Modifier.background(Color.Red).padding(vertical = 10.dp)){
                                                Column(Modifier.fillMaxWidth().weight(0.33f), horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text(reg.fecha)
                                                }
                                                Column(Modifier.fillMaxWidth().weight(0.33f), horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text(formatToTable(reg.valor))
                                                }
                                                Column(Modifier.fillMaxWidth().weight(0.33f), horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text(if(isConseguido(reg.valor)) "Si" else "No")
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }

                    }
                    Row(Modifier.fillMaxWidth().weight(0.09f), horizontalArrangement = Arrangement.SpaceEvenly){
                        if(isDiaria){
                            var showMsg2 by remember { mutableStateOf(false) }
                            val mm = meta as MetaDiaria

                            ElevatedButton(onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    if(mm.activo) {
                                        val ok = CRUDMetas.activateOrDesactivateMeta(false, AppPersistance.actualUser.email, mm.id)
                                        if(ok){
                                            viewModel.hideShowMetaDetail()
                                            CoroutineScope(Dispatchers.Main).launch {
                                                Toast.makeText(context, "Meta desactivada", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        else{
                                            CoroutineScope(Dispatchers.Main).launch {
                                                Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                    else{
                                        val ok = CRUDMetas.activateOrDesactivateMeta(true, AppPersistance.actualUser.email, mm.id)
                                        if(ok){
                                            viewModel.hideShowMetaDetail()
                                            CoroutineScope(Dispatchers.Main).launch {
                                                Toast.makeText(context, "Meta activada", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        else{
                                            CoroutineScope(Dispatchers.Main).launch {
                                                Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }

                                }
                            }) {
                                if(mm.activo) Text("Desactivar") else Text("Activar")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            ElevatedButton(
                                onClick = {
                                    showMsg2 = true
                                },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Eliminar")
                            }
                            if(showMsg2){
                                Dialog(onDismissRequest = { showMsg2 = false }) {
                                    ElevatedCard() {
                                        Column(Modifier.fillMaxWidth().padding(16.dp)){
                                            Text("¿Estas seguro que quieres borrar?", fontWeight = FontWeight.Bold)
                                            Text("Esta accion es irreversible, una vez se haya borrado la meta no se podrán recuperar los datos")
                                            Row(){
                                                ElevatedButton(onClick = { showMsg2 = false }) {
                                                    Text("Cancelar")
                                                }

                                                ElevatedButton(onClick = {
                                                    CoroutineScope(Dispatchers.IO).launch {
                                                        val ok = CRUDMetas.deleteMeta(mm.id, AppPersistance.actualUser.email)
                                                        if(ok) {
                                                            viewModel.hideShowMetaDetail()
                                                            viewModel.rechargeUI()
                                                            CoroutineScope(Dispatchers.Main).launch {
                                                                Toast.makeText(context, "Meta eliminada", Toast.LENGTH_SHORT).show()
                                                            }
                                                        } else {
                                                            CoroutineScope(Dispatchers.Main).launch {
                                                                Toast.makeText(context, "Error al eliminar la meta", Toast.LENGTH_SHORT).show()
                                                            }
                                                        }
                                                    }
                                                }) {
                                                    Text("Confirmar")
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                        else{
                            val mm = meta as MetaCaduca

                            var showMsg by remember { mutableStateOf(false) }
                            var showMsg2 by remember { mutableStateOf(false) }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ElevatedButton(onClick = {
                                    showMsg = true
                                }, enabled = if(mm.cancelado || mm.finalizado) false else true ) {
                                    Text("Cancelar meta")
                                }

                                ElevatedButton(
                                    onClick = {
                                        showMsg2 = true
                                    },
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = Color.Red,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Eliminar")
                                }
                            }

                            if(showMsg) {
                                Dialog(onDismissRequest = { showMsg = false }) {
                                    var counter by remember { mutableStateOf(10) }
                                    LaunchedEffect(Unit) {
                                        while (counter > 0){
                                            delay(1000)
                                            counter--
                                        }
                                    }
                                    ElevatedCard() {
                                        Column(Modifier.padding(16.dp)) {
                                            Row(horizontalArrangement = Arrangement.Center,  verticalAlignment = Alignment.CenterVertically){
                                                Text("¿Estas seguro de que quieres cancelar la meta?", fontWeight = FontWeight.Bold)
                                            }
                                            Row(horizontalArrangement = Arrangement.Center,  verticalAlignment = Alignment.CenterVertically){
                                                Text("Esta acción es irreversible, una vez le de a aceptar no habrá vuelta atrás")
                                            }
                                            Row(horizontalArrangement = Arrangement.SpaceEvenly,  verticalAlignment = Alignment.CenterVertically){
                                                ElevatedButton(onClick = { showMsg = false}) {
                                                    Text("Cancelar")
                                                }
                                                ElevatedButton(enabled = if(counter > 0) false else true, onClick = {
                                                    if(counter == 0){
                                                        CoroutineScope(Dispatchers.IO).launch {
                                                            val ok = CRUDMetas.cancelarMeta(mm.id, AppPersistance.actualUser.email)
                                                            if(ok){
                                                                viewModel.hideShowMetaDetail()
                                                                CoroutineScope(Dispatchers.Main).launch {
                                                                    Toast.makeText(context, "Meta cancelada", Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            else{
                                                                CoroutineScope(Dispatchers.Main).launch {
                                                                    Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                        }
                                                    }
                                                } ) {
                                                    Text(if(counter == 0) "Confirmar" else "Confirmar ("+counter.toString()+")")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if(showMsg2){
                                Dialog(onDismissRequest = { showMsg2 = false }) {
                                    ElevatedCard() {
                                        Column(Modifier.fillMaxWidth().padding(16.dp)){
                                            Text("¿Estas seguro que quieres borrar?", fontWeight = FontWeight.Bold)
                                            Text("Esta acceion es irreversible, una vez se haya borrado la meta no se podrán recuperar los datos")
                                            Row(){
                                                ElevatedButton(onClick = { showMsg2 = false }) {
                                                    Text("Cancelar")
                                                }

                                                ElevatedButton(onClick = {
                                                    CoroutineScope(Dispatchers.IO).launch {
                                                        val ok = CRUDMetas.deleteMeta(mm.id, AppPersistance.actualUser.email)
                                                        if(ok) {
                                                            viewModel.hideShowMetaDetail()
                                                            viewModel.rechargeUI()
                                                            CoroutineScope(Dispatchers.Main).launch {
                                                                Toast.makeText(context, "Meta eliminada", Toast.LENGTH_SHORT).show()
                                                            }
                                                        } else {
                                                            CoroutineScope(Dispatchers.Main).launch {
                                                                Toast.makeText(context, "Error al eliminar la meta", Toast.LENGTH_SHORT).show()
                                                            }
                                                        }
                                                    }
                                                }) {
                                                    Text("Confirmar")
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
        Row(Modifier.fillMaxSize().weight(0.01f)){

        }
    }
}

fun formatToTable(nums:String):String{
    val numsArray = nums.split("/")
    val n1 = numsArray[0].toFloat()
    val n2 = numsArray[1].toFloat()
    return n1.toInt().toString()+" / "+n2.toInt().toString()
}

