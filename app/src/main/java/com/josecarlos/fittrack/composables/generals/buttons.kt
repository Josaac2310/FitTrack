package com.josecarlos.fittrack.composables.generals

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.josecarlos.fittrack.ui.styles.TextSizes

@Composable
fun simpleButton(text:String, clickFuntion: () -> Unit){
    ElevatedButton(onClick = {clickFuntion()}) {
        textHeader(text, TextSizes.MEDIUM)
    }
}

@Composable
fun menuButton(text:String, clickFuntion: () -> Unit){
    ElevatedButton(onClick = {clickFuntion()}, colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.Green)) {
        textHeader(text, TextSizes.MEDIUM)
    }
}

@Composable
fun floatButton(icono: ImageVector, modifier:Modifier=Modifier, clickFuntion: () -> Unit){
    FloatingActionButton(modifier = modifier ,onClick = {clickFuntion()}) {
        Icon(icono, "")
    }
}

