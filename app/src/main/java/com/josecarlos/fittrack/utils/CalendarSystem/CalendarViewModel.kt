package com.josecarlos.fittrack.utils.CalendarSystem

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CalendarState(
    var anios: ArrayList<Anio> = arrayListOf(),
    var actualAnio: Anio = Anio(),
    var actualMes: Mes = Mes(),
    var calendarChargedInitial:Boolean = false,
    var showDay: Boolean = false,
    var showDay_Dia: Dia = Dia() ,
    var showDay_Mes: Mes = Mes(),
    var showDay_Anio: Anio= Anio(),
    var createNewRegistro: Boolean = false
)



class CalendarViewModel ():ViewModel(){
    private val _calendarState = MutableStateFlow<CalendarState>(CalendarState())
    val calendarState: StateFlow<CalendarState> = _calendarState

    fun showcreateNewRegistro(){
        _calendarState.value=_calendarState.value.copy(
            createNewRegistro = true
        )
    }

    fun hidecreateNewRegistro(){
        _calendarState.value=_calendarState.value.copy(
            createNewRegistro = false
        )
    }

    fun showDay(){
        _calendarState.value=_calendarState.value.copy(
            showDay = true
        )
    }
    fun hideDay(){
        _calendarState.value=_calendarState.value.copy(
            showDay = false
        )
    }

    fun chargeShowDayData(dia: Dia){
        _calendarState.value = _calendarState.value.copy(
            showDay_Dia = dia,
            showDay_Mes = _calendarState.value.actualMes,
            showDay_Anio = _calendarState.value.actualAnio
        )
    }

    fun chargeAnios(anios: ArrayList<Anio>){
        val mesActual = getMesActual()

        if (anios.isNotEmpty()) {
            val ultimoAnio = anios.last()
            val mes = ultimoAnio.meses.firstOrNull { m -> m.int == mesActual }

            if (mes != null) {
                _calendarState.value = _calendarState.value.copy(
                    anios = anios,
                    actualAnio = ultimoAnio,
                    actualMes = mes
                )
            } else {
                Log.e("CalendarViewModel", "No se encontró el mes actual (${mesActual}) en el último año")
            }
        } else {
            Log.e("CalendarViewModel", "Lista de años vacía en chargeAnios")
        }


    }

    fun changeMes(next:Boolean):Boolean{
        if (next){
            val nextMes = calendarState.value.actualMes.int+1
            if (nextMes <= 12){
                val newMes = calendarState.value.anios
                    .filter { a -> a.int == calendarState.value.actualAnio.int }
                    .first().meses.filter { m -> m.int == nextMes }
                    .first()
                _calendarState.value = _calendarState.value.copy(actualMes = newMes)
                return true
            }
            else{
                val nextAnio = calendarState.value.actualAnio.int+1
                val aniosExist = calendarState.value.anios.filter { a -> a.int == nextAnio }

                if (aniosExist.isEmpty()){
                    return false
                }
                else{
                    val newMes = calendarState.value.anios
                        .filter { a -> a.int == nextAnio }
                        .first().meses.first()
                    _calendarState.value = _calendarState.value.copy(actualMes = newMes, actualAnio = aniosExist.first())
                    return true
                }
            }
        }
        else{
            val prevMes = calendarState.value.actualMes.int-1
            if (prevMes >= 1){
                val newMes = calendarState.value.anios
                    .filter { a -> a.int == calendarState.value.actualAnio.int }
                    .first().meses.filter { m -> m.int == prevMes }
                    .first()
                _calendarState.value = _calendarState.value.copy(actualMes = newMes)
                return true
            }
            else{
                val prevAnio = calendarState.value.actualAnio.int-1
                val aniosExist = calendarState.value.anios.filter { a -> a.int == prevAnio }

                if (aniosExist.isEmpty()){
                    return false
                }
                else{
                    val newMes = calendarState.value.anios
                        .filter { a -> a.int == prevAnio }
                        .first().meses.last()
                    _calendarState.value = _calendarState.value.copy(actualMes = newMes, actualAnio = aniosExist.first())
                    return true
                }
            }
        }
    }
    fun setCharged(){
        _calendarState.value = _calendarState.value.copy(calendarChargedInitial = true)
    }

    fun setUnCharged(){
        _calendarState.value = _calendarState.value.copy(calendarChargedInitial = false)

    }
}

