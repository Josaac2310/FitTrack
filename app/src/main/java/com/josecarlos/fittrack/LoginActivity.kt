package com.josecarlos.fittrack

import android.accounts.Account
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.josecarlos.fittrack.data.dataClases.Usuario
import com.josecarlos.fittrack.mvc.model.CRUDUsuario
import com.josecarlos.fittrack.ui.theme.FitTrackTheme
import com.josecarlos.fittrack.utils.restartApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class LoginActivity : ComponentActivity() {
    val context = this
    lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    var showRegister by mutableStateOf(false)
    var savedCredential by  mutableStateOf<AuthCredential?>(null)
    var savedAccount by  mutableStateOf<GoogleSignInAccount?>(null)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1023254478842-ffg8r1dauin6v18n0k6jl2j2jq7p37u6.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            if(result.resultCode == Activity.RESULT_OK){
                val intent = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val email = account.email
                    CoroutineScope(Dispatchers.IO).launch {
                        val userExist = CRUDUsuario.userExists(email.toString())
                        if(userExist){
                            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                                    task_ ->
                                if(task_.isSuccessful){
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Toast.makeText(context, "Sesion iniciada", Toast.LENGTH_SHORT ).show()
                                        delay(500)
                                        restartApp(context)
                                    }
                                }
                                else{
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Toast.makeText(context, "Error al inciar sesion", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        else{
                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                            val client = GoogleSignIn.getClient(context, gso)
                            client.signOut()
                            showRegister = true
                            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                            savedCredential = credential
                            savedAccount = account

                        }
                    }

                }catch (e:ApiException){

                }
            }
        }

        setContent {
            var showCargando by remember { mutableStateOf(false) }

            FitTrackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   Column(Modifier.fillMaxSize().padding(innerPadding)){
                       Row(Modifier.fillMaxSize().weight(0.1f)) {}


                        if(showRegister){
                            if (showCargando){
                                Row(Modifier.fillMaxSize().weight(0.8f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                                    CircularProgressIndicator(Modifier.size(60.dp))
                                }
                            }
                            else{
                                Row(
                                    Modifier
                                        .fillMaxSize()
                                        .weight(0.8f)
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    ElevatedCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .shadow(8.dp, RoundedCornerShape(16.dp)),
                                        shape = RoundedCornerShape(16.dp),
                                        elevation = CardDefaults.elevatedCardElevation(8.dp),
                                    ) {
                                        var peso by remember { mutableStateOf(60f) }
                                        var altura by remember { mutableStateOf(1.60f) }
                                        var sexoAux by remember { mutableStateOf(false) }
                                        var username by remember { mutableStateOf("anónimo") }

                                        val calendar = Calendar.getInstance()
                                        var date = remember { mutableStateOf("01/01/1970") }
                                        val datePickerDialog = remember {
                                            DatePickerDialog(context, { _, year, month, dayOfMonth ->
                                                date.value = "${dayOfMonth}/${month + 1}/${year}"
                                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                                        }

                                        Column(
                                            Modifier
                                                .fillMaxSize()
                                                .padding(24.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Registro de Usuario",
                                                fontSize = 26.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1B5E20)
                                            )

                                            HorizontalDivider(Modifier.padding(vertical = 16.dp))

                                            Text("Introduce el nombre de usuario", fontSize = 16.sp)

                                            TextField(
                                                value = username,
                                                onValueChange = { username = it },
                                                singleLine = true,
                                                textStyle = TextStyle(
                                                    fontSize = 18.sp,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth(0.9f)
                                                    .height(56.dp),
                                                shape = RoundedCornerShape(16.dp),
                                                placeholder = {
                                                    Text(
                                                        "Introduce tu nombre de usuario",
                                                        fontSize = 16.sp,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                },
                                                leadingIcon = {
                                                    Icon(
                                                        imageVector = Icons.Default.Person,
                                                        contentDescription = "Icono usuario",
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                },
                                                keyboardOptions = KeyboardOptions.Default.copy(
                                                    imeAction = ImeAction.Done,
                                                    keyboardType = KeyboardType.Text
                                                )
                                            )

                                            Spacer(Modifier.height(24.dp))

                                            ElevatedButton(
                                                onClick = { datePickerDialog.show() },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Icon(Icons.Filled.CalendarToday, contentDescription = null)
                                                Spacer(Modifier.width(8.dp))
                                                Text("Seleccionar fecha de nacimiento")
                                            }

                                            Spacer(Modifier.height(24.dp))

                                            Text("Introduce tu peso corporal", fontSize = 16.sp)
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Slider(
                                                    value = peso,
                                                    onValueChange = { peso = it },
                                                    valueRange = 20f..300f,
                                                    modifier = Modifier.weight(1f),
                                                    colors = SliderDefaults.colors(
                                                        thumbColor = Color(0xFF4CAF50),
                                                        activeTrackColor = Color(0xFF66BB6A)
                                                    )
                                                )
                                                Spacer(Modifier.width(8.dp))
                                                Text(String.format("%.1f kg", peso))
                                            }

                                            Spacer(Modifier.height(24.dp))

                                            Text("Introduce tu altura", fontSize = 16.sp)
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Slider(
                                                    value = altura,
                                                    onValueChange = { altura = it },
                                                    valueRange = 1f..2.5f,
                                                    modifier = Modifier.weight(1f),
                                                    colors = SliderDefaults.colors(
                                                        thumbColor = Color(0xFF4CAF50),
                                                        activeTrackColor = Color(0xFF66BB6A)
                                                    )
                                                )
                                                Spacer(Modifier.width(8.dp))
                                                Text(String.format("%.2f m", altura))
                                            }

                                            Spacer(Modifier.height(24.dp))

                                            Text("Introduce tu sexo", fontSize = 16.sp)
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Switch(
                                                    checked = sexoAux,
                                                    onCheckedChange = { sexoAux = it },
                                                    thumbContent = {
                                                        Text(if (sexoAux) "F" else "M")
                                                    },
                                                    colors = SwitchDefaults.colors(
                                                        checkedThumbColor = Color(0xFFEC407A),
                                                        uncheckedThumbColor = Color(0xFF42A5F5)
                                                    )
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(if (sexoAux) "Femenino" else "Masculino")
                                            }

                                            Spacer(Modifier.height(32.dp))

                                            ElevatedButton(
                                                onClick = {
                                                    showCargando = true

                                                    CoroutineScope(Dispatchers.IO).launch {
                                                        val ok = CRUDUsuario.createUser(
                                                            Usuario(
                                                                email = savedAccount?.email.toString(),
                                                                username = username,
                                                                nombre = savedAccount?.displayName.toString(),
                                                                sexo = if (sexoAux) "F" else "M",
                                                                fecha_nacimiento = date.value,
                                                                peso = peso,
                                                                altura = altura,
                                                                apellidos = " "
                                                            )
                                                        )
                                                        if(ok){
                                                            FirebaseAuth.getInstance().signInWithCredential(savedCredential!!).addOnCompleteListener {
                                                                    task_ ->
                                                                if(task_.isSuccessful){
                                                                    CoroutineScope(Dispatchers.IO).launch {
                                                                        CoroutineScope(Dispatchers.Main).launch {
                                                                            Toast.makeText(
                                                                                context,
                                                                                "¡Registro exitoso!",
                                                                                Toast.LENGTH_LONG
                                                                            ).show()
                                                                        }
                                                                        AppPersistance.actualUser = CRUDUsuario.getUser(savedAccount?.email.toString())!!
                                                                        delay(3000)
                                                                        restartApp(context)
                                                                    }
                                                                }
                                                                else{
                                                                    CoroutineScope(Dispatchers.Main).launch {
                                                                        Toast.makeText(context, "Error al inciar sesion", Toast.LENGTH_SHORT).show()
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            CoroutineScope(Dispatchers.Main).launch {
                                                                Toast.makeText(context, "Error en el registro", Toast.LENGTH_SHORT).show()
                                                            }
                                                        }
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier.fillMaxWidth(0.8f)
                                            ) {
                                                Icon(Icons.Filled.AppRegistration, contentDescription = null)
                                                Spacer(Modifier.width(8.dp))
                                                Text("Registrarse", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Título de la app
                                Text(
                                    text = "FITTRACK",
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 2.sp,
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black.copy(alpha = 0.3f),
                                            offset = Offset(4f, 4f),
                                            blurRadius = 8f
                                        ),
                                        fontFamily = FontFamily.Default // Cámbialo por tu fuente si tienes una
                                    )
                                )

                                // Logo App
                                Image(
                                    painter = painterResource(R.drawable.logoapp),
                                    contentDescription = "Logo de la app",
                                    modifier = Modifier
                                        .size(400.dp), // Ajusta según necesidad
                                    contentScale = ContentScale.Fit
                                )

                                ElevatedButton(
                                    onClick = {
                                        val signInIntent = googleSignInClient.signInIntent
                                        googleSignInLauncher.launch(signInIntent)
                                    },
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .height(50.dp)
                                        .fillMaxWidth(),
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = Color.White, // Fondo blanco como el oficial
                                        contentColor = Color.Black     // Texto en negro
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.iconogoogle),
                                        contentDescription = "Google icon",
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Iniciar sesión",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                       Row(Modifier.fillMaxSize().weight(0.1f)) {}
                   }
                }
            }
        }
    }
}
