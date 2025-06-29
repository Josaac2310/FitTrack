package com.josecarlos.fittrack.ui.styles

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

enum class TextSizes{ XXXXLARGE,XXXLARGE ,XXLARGE ,XLARGE, LARGE, MEDIUM, SMALL, XSMALL }
enum class Iconos{ MAIN_HOME, MAIN_GOALS, MAIN_STATICS, MENU_LATERAL, BACKMENU }
enum class BackColors{ PRIMARY, SECONDARY, TERTIARY, LAST}

object styles {
    val sizes_header= mapOf(
        TextSizes.XXXXLARGE to 42.sp,
        TextSizes.XXXLARGE to 35.sp,
        TextSizes.XXLARGE to 30.sp,
        TextSizes.XLARGE to 25.sp,
        TextSizes.LARGE to 22.sp,
        TextSizes.MEDIUM to 20.sp,
        TextSizes.SMALL to 18.sp,
        TextSizes.XSMALL to 16.sp
    )
    val sizes_text= mapOf(
        TextSizes.XLARGE to 18.sp,
        TextSizes.LARGE to 15.sp,
        TextSizes.MEDIUM to 12.sp,
        TextSizes.SMALL to 10.sp,
        TextSizes.XSMALL to 8.sp
    )
    val icons= mapOf(
        Iconos.MAIN_HOME to Icons.Filled.Home,
        Iconos.MAIN_GOALS to Icons.Filled.Checklist,
        Iconos.MAIN_STATICS to Icons.Filled.BarChart,
        Iconos.MENU_LATERAL to Icons.Filled.Menu,
        Iconos.BACKMENU to Icons.Filled.KeyboardDoubleArrowLeft
    )
    val backColors= mapOf(
        BackColors.PRIMARY to Color(0xFF388E3C),
        BackColors.SECONDARY to Color(0xFF4CAF50),
        BackColors.TERTIARY to Color(0xFF81C784),
        BackColors.LAST to Color(0xFFC8E6C9)
    )
}

