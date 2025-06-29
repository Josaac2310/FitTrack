package com.josecarlos.fittrack.mvc.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.josecarlos.fittrack.R
import com.josecarlos.fittrack.ui.theme.FitTrackTheme

@Composable
fun LauncherScreen(){
    FitTrackTheme {
        Scaffold { innerPadding ->
            Column (Modifier.fillMaxSize().padding(innerPadding)) {
                Row(Modifier.fillMaxSize().weight(0.15f)) {}
                Row(Modifier.fillMaxSize().weight(0.35f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
                    Image(painterResource(R.drawable.logoapp), "")
                }
                Row(Modifier.fillMaxSize().weight(0.35f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) {
                    LinearProgressIndicator(Modifier.fillMaxHeight(0.02f))
                }
                Row(Modifier.fillMaxSize().weight(0.15f)) {}
            }
        }
    }
}