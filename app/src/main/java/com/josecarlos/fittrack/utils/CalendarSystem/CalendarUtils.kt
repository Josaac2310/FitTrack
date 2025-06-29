package com.josecarlos.fittrack.utils.CalendarSystem

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList


fun parseDateFromFirebase(date:String):LocalDate?{
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val fecha = LocalDate.parse(date, formatter)

    return fecha
}

fun getCurrentDateFormatted(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return currentDate.format(formatter)
}

fun getCurrentDateFormatted2(currentDate: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return currentDate.format(formatter)
}


fun getStringOfMes(mes:Int): String{

    return when (mes) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        12 -> "Diciembre"
        else -> "Mes inválido"
    }
}

fun diaEnEspanol(fecha: LocalDate): String {
    val datos=  fecha.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
    return datos
}

fun getMesActual(): Int{
    return LocalDate.now().monthValue
}

fun divideDaysOfMonth(dias:ArrayList<Dia>, diaInicial:Int): ArrayList<ArrayList<Dia?>>{
    var diasObtenidos = 0
    var arrayAux: ArrayList<ArrayList<Dia?>> = arrayListOf()
    var continua = true
    Log.d("pruebaprueba", diaInicial.toString())
    while (continua){
        if (diasObtenidos == 0){
            val daysToGet = 8-diaInicial
            var daysToInsert = arrayListOf<Dia?>()
            for (i in 0 until diaInicial-1){
                daysToInsert.add(null)
            }
            for (i in 0 until daysToGet){
                daysToInsert.add(dias[i])
                diasObtenidos++
            }
            arrayAux.add(daysToInsert)
        }
        else{
            val arrayAux_ = arrayListOf<Dia?>()
            val daysToGet = dias.size-diasObtenidos
            if (daysToGet < 7){
                for (i in diasObtenidos until diasObtenidos+daysToGet){
                    arrayAux_.add(dias[i])
                    diasObtenidos++
                }
                for (i in 0 until 7-daysToGet){
                    arrayAux_.add(null)
                }
                arrayAux.add(arrayAux_)
            }
            else{
                val diasObtenidosAux = diasObtenidos
                for (i in diasObtenidosAux until diasObtenidosAux+7){
                    arrayAux_.add(dias[i])
                    diasObtenidos++
                }
                arrayAux.add(arrayAux_)
            }

        }

        if (diasObtenidos < dias.size){
            continua = true
        }
        else{
            continua = false
        }
    }
    return arrayAux
}

fun getInitialDay(diaInicial: Dia): Int{
    return when(diaInicial.nombre){
        "lunes" -> 1
        "martes" -> 2
        "miércoles" -> 3
        "jueves" -> 4
        "viernes" -> 5
        "sábado" -> 6
        "domingo" -> 7
        else -> 1
    }
}

fun normalizarFecha(fecha: String): String {
    val partes = fecha.split("/")
    val dia = partes[0].padStart(2, '0')
    val mes = partes[1].padStart(2, '0')
    val anio = partes[2]
    return "$dia/$mes/$anio"
}

fun diasDeDiferencia(fecha1: String, fecha2: String): Int {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date1 = LocalDate.parse(normalizarFecha(fecha1), formatter)
    val date2 = LocalDate.parse(normalizarFecha(fecha2), formatter)
    return ChronoUnit.DAYS.between(date1, date2).toInt()
}
