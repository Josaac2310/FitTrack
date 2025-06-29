package com.josecarlos.fittrack.composables.generals

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.josecarlos.fittrack.R
import com.josecarlos.fittrack.ui.styles.TextSizes
import com.josecarlos.fittrack.ui.styles.styles.sizes_header
import com.josecarlos.fittrack.ui.styles.styles.sizes_text

@Composable
fun textHeader(texto:String, size: TextSizes){
    Text(text= texto, fontFamily = FontFamily(Font(R.font.bebasneue)),
        fontSize = sizes_header.get(size)?: sizes_header.get(TextSizes.MEDIUM)!!)
}

@Composable
fun textText(texto:String, size: TextSizes){
    Text(text= texto, fontFamily = FontFamily(Font(R.font.opensans)),
        fontSize = sizes_text.get(size)?: sizes_text.get(TextSizes.MEDIUM)!!)
}

