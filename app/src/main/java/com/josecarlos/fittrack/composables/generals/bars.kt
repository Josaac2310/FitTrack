package com.josecarlos.fittrack.composables.generals

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.josecarlos.fittrack.AppPersistance
import com.josecarlos.fittrack.LoginActivity
import com.josecarlos.fittrack.R
import com.josecarlos.fittrack.data.dataClases.Usuario
import com.josecarlos.fittrack.mvc.model.CRUDUsuario
import com.josecarlos.fittrack.ui.styles.Iconos
import com.josecarlos.fittrack.ui.styles.TextSizes
import com.josecarlos.fittrack.ui.styles.styles.icons
import com.josecarlos.fittrack.ui.views.viewmodels.ViewModelMain
import com.josecarlos.fittrack.ui.views.viewmodels.screens
import com.josecarlos.fittrack.utils.ProfileImage
import com.josecarlos.fittrack.utils.Responsive
import com.josecarlos.fittrack.utils.restartApp

@Composable
fun botBar(viewModel: ViewModelMain){
    Row(Modifier.fillMaxWidth().height(Responsive.getResponsiveHeight(0.096f))){
        // Botón de Estadísticas
        Column(Modifier.fillMaxSize().weight(33f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            IconButton(
                modifier = Modifier.fillMaxSize().padding(15.dp),
                onClick = {
                    viewModel.changeScreen(screens.STADISTICS)
                }
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = icons.get(Iconos.MAIN_STATICS)!!,
                    contentDescription = "Estadísticas"
                )
            }
        }
        Column(Modifier.fillMaxSize().weight(0.05f),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            VerticalDivider()
        }
        // Botón de Home
        Column(Modifier.fillMaxSize().weight(33f),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            IconButton(
                modifier = Modifier.fillMaxSize().padding(15.dp),
                onClick = {
                    viewModel.changeScreen(screens.HOME)
                }
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = icons.get(Iconos.MAIN_HOME)!!,
                    contentDescription = "Inicio"
                )
            }
        }
        Column(Modifier.fillMaxSize().weight(0.05f),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            VerticalDivider()
        }
        // Botón de Metas
        Column(Modifier.fillMaxSize().weight(33f),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            IconButton(
                modifier = Modifier.fillMaxSize().padding(15.dp),
                onClick = {
                    viewModel.changeScreen(screens.METAS)
                }
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = icons.get(Iconos.MAIN_GOALS)!!,
                    contentDescription = "Metas"
                )
            }
        }
    }
}

@Composable
fun topBar(onDrawerOpen: () -> Unit = {}){
    Row(Modifier.fillMaxWidth().height(Responsive.getResponsiveHeight(0.090f))){

        Column(Modifier.fillMaxSize().weight(20f),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            IconButton(
                modifier = Modifier.fillMaxSize().padding(5.dp),
                onClick = onDrawerOpen
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = icons.get(Iconos.MENU_LATERAL)!!,
                    contentDescription = "Abrir menú lateral"
                )
            }
        }
        Column(Modifier.fillMaxSize().weight(60f),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            textHeader("FitTrack", TextSizes.XXXXLARGE)
        }


        Column(Modifier.fillMaxSize().weight(20f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Image(painterResource(R.drawable.logoapp),  "")
        }

    }
    Row(Modifier.fillMaxWidth().height(Responsive.getResponsiveHeight(0.010f)).padding(top = Responsive.getResponsiveHeight(0.090f))){
        HorizontalDivider()
    }
}

@Composable
@Preview
fun pruebads(){
    topBar()
}
@Composable
fun navDrawer(context: Context,email: String, onClose: () -> Unit, url:String) {
    var usuario = AppPersistance.actualUser

    if (usuario != null) {
        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // User information section
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(46.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                    /*
                    Text(
                        text = usuario.nombre.first().uppercase(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    */
                    ProfileImage(url)


                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = usuario.username,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = usuario.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                HorizontalDivider(Modifier.padding(vertical = 8.dp))

                // Health data section
                Text(
                    text = "Datos de salud",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Altura",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = String.format("%.2f cm", usuario.altura),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Column {
                        Text(
                            text = "Peso",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = String.format("%.2f kg", usuario.peso),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            // Logo section (with weight to push button to bottom)
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f) // Esto hace que el logo ocupe el espacio disponible
            ) {
                Image(
                    painter = painterResource(R.drawable.logoapp),
                    contentDescription = "Logo FitTrack",
                    modifier = Modifier
                        .size(260.dp)
                        .align(Alignment.Center)
                )
            }

            // Logout button at the bottom
            Button(
                onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                    val client = GoogleSignIn.getClient(context, gso)

                    client.signOut().addOnCompleteListener {
                        FirebaseAuth.getInstance().signOut()

                        val loginIntent = Intent(context, LoginActivity::class.java)
                        loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(loginIntent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = "Cerrar sesión",
                    color = MaterialTheme.colorScheme.onError,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}