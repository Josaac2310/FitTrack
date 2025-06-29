package com.josecarlos.fittrack

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.josecarlos.fittrack.GoogleAuth.getUserPhoto
import com.josecarlos.fittrack.Notification.NotificationSystem.crearCanalNotificacion
import com.josecarlos.fittrack.Notification.NotificationSystem.crearNotificacionPrueba
import com.josecarlos.fittrack.composables.generals.botBar
import com.josecarlos.fittrack.composables.generals.navDrawer
import com.josecarlos.fittrack.composables.generals.topBar
import com.josecarlos.fittrack.composables.specifics.DayView
import com.josecarlos.fittrack.composables.specifics.MyCalendarComponent
import com.josecarlos.fittrack.composables.specifics.ScreenNewRegistro
import com.josecarlos.fittrack.composables.specifics.subscreenGoals
import com.josecarlos.fittrack.composables.specifics.subscreenMetaDetail
import com.josecarlos.fittrack.composables.specifics.subscreenNewMeta
import com.josecarlos.fittrack.composables.specifics.subscreenStadistics
import com.josecarlos.fittrack.mvc.model.CRUDRegistros
import com.josecarlos.fittrack.mvc.view.HomeScreen
import com.josecarlos.fittrack.ui.theme.FitTrackTheme
import com.josecarlos.fittrack.ui.views.viewmodels.MetasViewModel
import com.josecarlos.fittrack.ui.views.viewmodels.NewRegistroViewModel
import com.josecarlos.fittrack.ui.views.viewmodels.ViewModelMain
import com.josecarlos.fittrack.ui.views.viewmodels.screens
import com.josecarlos.fittrack.utils.CalendarSystem.CalendarSystem
import com.josecarlos.fittrack.utils.CalendarSystem.CalendarViewModel
import com.josecarlos.fittrack.utils.CalendarSystem.divideDaysOfMonth
import com.josecarlos.fittrack.utils.CalendarSystem.parseDateFromFirebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val calendarViewModel = CalendarViewModel()
        val mainViewModel = ViewModelMain()
        val newRegistroViewModel = NewRegistroViewModel()
        val metasViewModel = MetasViewModel()
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isgranted:Boolean ->
            if(!isgranted){
                finishAffinity()
                System.exit(0)
            }
        }

        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        crearCanalNotificacion(context)
        crearNotificacionPrueba(context)

        CoroutineScope(Dispatchers.IO).launch{

            CalendarSystem.chargeCalendar()
            val registros = CRUDRegistros.getAllRegistrosOfUser(AppPersistance.actualUser.email)
            Log.d("Registros", registros.toString())
            if (registros != null) {
                CalendarSystem.initData(registros)
            } else {
                Log.e("MainActivity", "Error: registros es null")
            }
            calendarViewModel.chargeAnios(CalendarSystem.anios)
            calendarViewModel.setCharged()
        }

        enableEdgeToEdge()
        setContent {
            val screenState by mainViewModel.actualScreen.collectAsState()
            val calendarState by calendarViewModel.calendarState.collectAsState()
            var isDrawerOpen by remember { mutableStateOf(false) }
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val metasState by metasViewModel.uiState.collectAsState()

            FitTrackTheme {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            modifier = Modifier.fillMaxWidth(0.75f) // Aquí limitas el ancho al 75%

                        ) {
                            navDrawer(
                                context = context,
                                email = AppPersistance.actualUser.email,
                                onClose = {
                                    scope.launch { drawerState.close() }
                                },
                                url = GoogleAuth.getUserPhoto(context)
                            )
                        }
                    }
                ) {
                    Scaffold(Modifier.fillMaxSize().systemBarsPadding(),
                        bottomBar = { botBar(mainViewModel) },
                        topBar = { topBar (onDrawerOpen = { scope.launch { drawerState.open() } }) },
                        floatingActionButton = {
                            if(screenState == screens.METAS && metasState.createNewMeta == false && metasState.showMetaDetail == false)
                                FloatingActionButton(
                                    onClick = {
                                        metasViewModel.showCreateNewMeta()
                                    },
                                    modifier = Modifier
                                        .size(50.dp)
                                        .shadow(12.dp, CircleShape),
                                    containerColor = Color(0xFF4CAF50), // Verde más profesional
                                    contentColor = Color.White,
                                    elevation = FloatingActionButtonDefaults.elevation(
                                        defaultElevation = 8.dp,
                                        pressedElevation = 12.dp,
                                        hoveredElevation = 10.dp
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "",
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                        }
                        )
                    { innerPadding ->
                    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                        when(screenState){  //Cambia de pantalla en funcion del valor de screenState (estado de navegacion)
                                            //ese valor cambia al hacer click en alguno de los botones de BotBar()
                            screens.HOME -> {
                                // Si el usuario quiere crear un nuevo registro
                                if (calendarState.createNewRegistro) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                    ) {
                                        //
                                        ScreenNewRegistro(
                                            onClose = { // Cuando se cierra la pantalla de nuevo registro:
                                                calendarViewModel.hidecreateNewRegistro() // Oculta el formulario
                                                CoroutineScope(Dispatchers.IO).launch{
                                                    calendarViewModel.setUnCharged() // Marca el calendario como "no cargado"
                                                    CalendarSystem.chargeCalendar()
                                                    val registros = CRUDRegistros.getAllRegistrosOfUser(AppPersistance.actualUser.email)
                                                    CalendarSystem.initData(registros!!) //Recarga los datos de la base de datos
                                                    calendarViewModel.chargeAnios(CalendarSystem.anios)
                                                    calendarViewModel.setCharged()
                                                }
                                            },
                                            email = AppPersistance.actualUser.email,
                                            viewModel = newRegistroViewModel
                                        )
                                    }
                                    // Si se debe mostrar la vista de un día específico
                                } else if (calendarState.showDay) {
                                    DayView(
                                        innerPadding,
                                        calendarState.showDay_Dia,
                                        { calendarViewModel.hideDay() }, // Al cerrar el día, oculta la vista
                                        calendarState.showDay_Mes,
                                        calendarState.showDay_Anio,
                                        calendarViewModel
                                    )
                                // Si no hay vista de nuevo registro ni de día específico, se muestra el calendario general
                                } else {
                                    Column(
                                        Modifier.fillMaxSize().padding(innerPadding),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        // Si los datos del calendario están cargados, se muestra el calendario
                                        if(calendarState.calendarChargedInitial) {
                                            MyCalendarComponent(calendarViewModel)
                                        } else {
                                            // Si aún se están cargando los datos, se muestra un indicador de carga
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator()
                                            }
                                        }
                                    }
                                }
                            }
                            screens.METAS -> {
                                if(metasState.createNewMeta){
                                    subscreenNewMeta(context, innerPadding, metasViewModel)
                                }
                                else if(metasState.showMetaDetail){
                                    subscreenMetaDetail(context,metasViewModel, metasState.idMetaDetail!!, metasState.isDiaria, innerPadding)
                                }
                                else{
                                    subscreenGoals(context, innerPadding, metasViewModel)
                                }

                            }
                            screens.STADISTICS -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    subscreenStadistics(context, innerPadding)
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

