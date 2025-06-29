package com.josecarlos.fittrack

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.josecarlos.fittrack.data.Deportes
import com.josecarlos.fittrack.data.Familias
import com.josecarlos.fittrack.data.MetaRegType
import com.josecarlos.fittrack.data.MetaType
import com.josecarlos.fittrack.data.dataClases.MetaDiaria
import com.josecarlos.fittrack.data.dataClases.MetaReg
import com.josecarlos.fittrack.data.dataClases.Registro
import com.josecarlos.fittrack.data.dataClases.RegistroMeta
import com.josecarlos.fittrack.mvc.model.CRUDMetas
import com.josecarlos.fittrack.mvc.model.CRUDRegistros
import com.josecarlos.fittrack.mvc.model.CRUDUsuario
import com.josecarlos.fittrack.mvc.view.LauncherScreen
import com.josecarlos.fittrack.ui.theme.FitTrackTheme
import com.josecarlos.fittrack.utils.startActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LauncherActivity : ComponentActivity() {
    val context = this

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            val isUserLogged = GoogleAuth.getCurrentUser(context)


            if(isUserLogged){
                AppPersistance.actualUser = CRUDUsuario.getUser(GoogleAuth.getUserEmail(context))!!

                delay(3000)
                startActivity(context, MainActivity::class.java)

            }
            else{
                startActivity(context, LoginActivity::class.java)
            }

        }

        enableEdgeToEdge()
        setContent {
            LauncherScreen()
        }
    }
}


