package com.josecarlos.fittrack.composables.specifics

import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.josecarlos.fittrack.composables.generals.menuButton
import com.josecarlos.fittrack.composables.generals.simpleButton
import com.josecarlos.fittrack.composables.generals.textHeader
import com.josecarlos.fittrack.composables.generals.textText
import com.josecarlos.fittrack.ui.styles.TextSizes

@Composable
fun subscreenHome(){
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxSize().weight(0.15f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            textHeader(texto = "Bienvenido JC", TextSizes.XXXLARGE)
        }
        Row(Modifier.fillMaxSize().weight(0.40f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {}
        Row(Modifier.fillMaxSize().weight(0.1f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            menuButton(text = "Ultimos Registros") { }
        }
        Row(Modifier.fillMaxSize().weight(0.1f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            menuButton(text = "Ultimos Registros") { }
        }
        Row(Modifier.fillMaxSize().weight(0.1f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            menuButton(text = "Ultimos Registros") { }
        }
        Row(Modifier.fillMaxSize().weight(0.15f)) {

        }
        textText(" ", TextSizes.XLARGE)
    }
}

@Composable
@Preview
fun prueba(){
    subscreenHome()
}

