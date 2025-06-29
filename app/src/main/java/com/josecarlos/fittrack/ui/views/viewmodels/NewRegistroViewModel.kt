package com.josecarlos.fittrack.ui.views.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.josecarlos.fittrack.data.Deportes
import com.josecarlos.fittrack.data.Familias
import com.josecarlos.fittrack.data.dataClases.Registro
import com.josecarlos.fittrack.data.getFamiliaDeporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NewRegistro(
    var actualStep:Int = 1,
    var registroNuevo: Registro = Registro(),
    var actualFamilia: Familias = Familias.CARDIO,
    var kcal: Int = 0,
    var actualPeso: Float = 0f ,
    var actualDuracion: Int = 0
)

class NewRegistroViewModel():ViewModel(){
    val _actualState: MutableStateFlow<NewRegistro> = MutableStateFlow(NewRegistro())
    val actualState: StateFlow<NewRegistro> = _actualState.asStateFlow()


    fun changeStep(step: Int){
        _actualState.value = _actualState.value.copy(actualStep = step)
    }

    fun updateRegistro(deporte: Deportes) {
        _actualState.update { currentState ->
            currentState.copy(
                actualStep = 3,
                registroNuevo = currentState.registroNuevo.copy(
                    tipo = deporte,
                    familia = getFamiliaDeporte(deporte)
                )
            )
        }
    }

    fun changeFamilia(familia: Familias){
        _actualState.value = _actualState.value.copy(actualFamilia = familia)
    }

    fun updateKcal(kcal:Int){
        _actualState.value = _actualState.value.copy(kcal = kcal)
    }

    fun updatePeso(peso:Float){
        _actualState.value = _actualState.value.copy(actualPeso = peso)
    }

    fun updateDuracion(tiempo:Int){
        _actualState.value = _actualState.value.copy(actualDuracion = tiempo)
    }
}




