package com.josecarlos.fittrack.utils

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import coil.compose.rememberAsyncImagePainter
import com.josecarlos.fittrack.data.dataClases.Meta
import com.josecarlos.fittrack.utils.CalendarSystem.CalendarSystem.getTodayDia
import com.josecarlos.fittrack.utils.CalendarSystem.getCurrentDateFormatted
import com.josecarlos.fittrack.utils.CalendarSystem.parseDateFromFirebase

fun startActivity(context: Context, clase: Class<*>){
    val intent = Intent(context, clase)
    context.startActivity(intent)
}

fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    Runtime.getRuntime().exit(0) // Termina el proceso actual
}

@Composable
fun ProfileImage(photoUrl: String?) {
    if (photoUrl != null) {
        Image(
            painter = rememberAsyncImagePainter(photoUrl),
            contentDescription = "Foto de perfil"
        )
    }
}

fun hasRegisterForToday(meta: Meta):Boolean{
    meta.registros.forEach { r ->
        if(r.fecha == getCurrentDateFormatted()){
          return true
        }
    }
    return false
}

fun isConseguido(valor:String):Boolean{
    val valores = valor.split("/")
    if(valores.size == 2){
        val v1 = valores.get(0).toFloat()
        val v2 = valores.get(1).toFloat()
        if(v1 >= v2) return true else return false
    }
    return false
}

