package com.josecarlos.fittrack.mvc.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.josecarlos.fittrack.R
import com.josecarlos.fittrack.composables.generals.floatButton
import com.josecarlos.fittrack.composables.generals.textHeader
import com.josecarlos.fittrack.ui.styles.TextSizes
import com.josecarlos.fittrack.utils.Responsive.getResponsiveHeight

@Composable
fun MetasScreen(){

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxSize().weight(0.35f)) {
            Column(Modifier.fillMaxSize().weight(0.5f)) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(
                        strokeWidth = 17.dp,
                        progress = 1f,
                        modifier = Modifier.size(getResponsiveHeight(0.2f))
                    )
                    Image(modifier = Modifier.size(getResponsiveHeight(0.12f)),painter =  painterResource(
                        R.drawable.fuego) , contentDescription = "")
                }
            }
            Column(Modifier.fillMaxSize().weight(0.5f)) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(
                        strokeWidth = 17.dp,
                        progress = 1f,
                        modifier = Modifier.size(getResponsiveHeight(0.2f))
                    )
                    Image(modifier = Modifier.size(getResponsiveHeight(0.12f)),painter =  painterResource(
                        R.drawable.crono) , contentDescription = "")
                }
            }
        }
        Row(Modifier.fillMaxSize().weight(0.05f)) {
            Divider(Modifier.fillMaxWidth(), thickness = 2.dp, color = Color.Black)
        }
        Row(Modifier.fillMaxSize().weight(0.6f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.fillMaxSize()) {
                Row(Modifier.fillMaxSize().weight(0.1f), horizontalArrangement = Arrangement.Center) {
                    textHeader("Mis metas", TextSizes.XXXLARGE)
                }
                Row(Modifier.fillMaxSize().weight(0.8f), horizontalArrangement = Arrangement.Center) {

                }
                Row (Modifier.fillMaxSize().weight(0.1f)){

                }
            }
        }
    }
    Box(Modifier.fillMaxSize()){
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
            floatButton(Icons.Filled.Add, Modifier.padding(16.dp), {})
        }
    }

}

@Preview
@Composable
fun prueba(){
    MetasScreen()
}