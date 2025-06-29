package com.josecarlos.fittrack.ui.views.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class screens{
    HOME,
    METAS,
    STADISTICS
}

class ViewModelMain(): ViewModel(){
    val _actualScreen: MutableStateFlow<screens> = MutableStateFlow(screens.HOME)
    val actualScreen: StateFlow<screens> = _actualScreen.asStateFlow()

    fun changeScreen(screen: screens){
        _actualScreen.value = screen
    }


}