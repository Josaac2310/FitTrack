package com.josecarlos.fittrack.composables.specifics

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.josecarlos.fittrack.composables.generals.botBar
import com.josecarlos.fittrack.composables.generals.topBar
/*
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenMain(){
    var actualScreen by remember{ mutableIntStateOf(1) }

    Scaffold(Modifier.fillMaxSize().systemBarsPadding().navigationBarsPadding(), bottomBar = {botBar()}, topBar = { topBar() }) {
        when (actualScreen){
            1 -> {}
            2 -> {}
            3 -> {}
            else -> { }
        }
    }
}

*/