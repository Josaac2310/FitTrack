package com.josecarlos.fittrack.utils

import android.content.Context
import android.content.Intent
import android.health.connect.datatypes.units.Percentage
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Responsive {

    @Composable
    fun getResponsiveHeight(percentage: Float): Dp{
        val configuration= LocalConfiguration.current
        val screenHeightDp= configuration.screenHeightDp.dp
        return screenHeightDp*percentage
    }

}

