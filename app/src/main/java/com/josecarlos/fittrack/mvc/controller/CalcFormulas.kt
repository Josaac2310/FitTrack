package com.josecarlos.fittrack.mvc.controller

import android.util.Log
import com.josecarlos.fittrack.data.Deportes
import com.josecarlos.fittrack.data.Familias

object CalcFormulas {
    val mapDeportes = mapOf<Deportes, Familias?>(
        Deportes.CAMINAR to Familias.CARDIO,
        Deportes.CORRER to Familias.CARDIO,
        Deportes.BICICLETA to Familias.CARDIO,
        Deportes.FLEXIONES to Familias.CALISTENIA,
        Deportes.DOMINADAS to Familias.CALISTENIA,
        Deportes.SENTADILLAS to Familias.CALISTENIA,
        Deportes.ABDOMINALES to Familias.CALISTENIA,
        Deportes.BOXEO to Familias.COMBATE,
        Deportes.KARATE_TAEKWONDO to Familias.COMBATE,
        Deportes.KICKBOXING_MUAYTHAI to Familias.COMBATE,
        Deportes.FUTBOL to Familias.EQUIPO,
        Deportes.BALONCESTO to Familias.EQUIPO,
        Deportes.TENNIS to Familias.EQUIPO,
        Deportes.VOLLEY to Familias.EQUIPO,
        Deportes.BALONMANO to Familias.EQUIPO,
        Deportes.NATACION to Familias.ACUATICO,
        Deportes.REMO to Familias.ACUATICO,
        Deportes.ESCALADA to Familias.AVENTURA,
        Deportes.SENDERISMO to Familias.AVENTURA,
        Deportes.ELIPTICA to Familias.GIMNASIO,
        Deportes.BICICLETA_ESTATICA to Familias.GIMNASIO,
        Deportes.PESAS to Familias.GIMNASIO,
        Deportes.MANCUERNA_UNA to Familias.GIMNASIO,
        Deportes.MANCUERNAS_DOS to Familias.GIMNASIO,
        Deportes.CINTA_DE_CORRER to Familias.GIMNASIO,
        Deportes.SACO_DE_BOXEO to Familias.GIMNASIO
    )

    fun getTipoDeporte(deporte: Deportes): Familias{
        return mapDeportes.get(deporte)!!
    }

    fun obtenerNombreAmigable(enumValor: Enum<*>): String {
        return enumValor.name
            .lowercase()
            .replace('_', ' ')
            .replaceFirstChar { it.uppercaseChar() }
    }

    fun calcularKcalQuemadas(met: Float, peso: Float, min: Int): Int{
        return ((met*peso*min)/60).toInt()
    }

    fun getKcalxMin(Kcal: Int, min: Int): Int {
        return Kcal/min
    }

    fun getLongitudPasos(alturaCm: Int): Float {
        val longitudPasoCm = alturaCm * 0.413f
        return longitudPasoCm / 100f
    }

    fun getTotalPasos(distanciaRecorrida:Int, longitudPaso: Float): Float{
        Log.d("pasos", distanciaRecorrida.toString()+" - "+ longitudPaso.toString())
        return distanciaRecorrida/longitudPaso
    }

    fun getRitmoMedio(min: Int, distanciaEnKm: Float): Float {
        return distanciaEnKm / min.toFloat() // Devuelve km/min
    }





    fun obtenerFamiliaDesdeString(valor: String): Familias? {
        val valorNormalizado = valor.uppercase().replace(" ", "_")
        return Familias.entries.find { it.name == valorNormalizado }
    }

    fun obtenerDeporteDesdeString(valor: String): Deportes? {
        val valorNormalizado = valor.uppercase().replace(" ", "_")
        return Deportes.entries.find { it.name == valorNormalizado }
    }

    fun getMetOfDeporte(deporte: Deportes): Double {
        return when (deporte) {
            Deportes.CAMINAR -> 3.5
            Deportes.CORRER -> 9.8
            Deportes.BICICLETA -> 7.5
            Deportes.FLEXIONES -> 8.0
            Deportes.DOMINADAS -> 8.0
            Deportes.SENTADILLAS -> 5.0
            Deportes.ABDOMINALES -> 4.0
            Deportes.BOXEO -> 12.8
            Deportes.KARATE_TAEKWONDO -> 10.3
            Deportes.KICKBOXING_MUAYTHAI -> 10.0
            Deportes.FUTBOL -> 7.0
            Deportes.BALONCESTO -> 8.0
            Deportes.TENNIS -> 7.3
            Deportes.VOLLEY -> 3.5
            Deportes.BALONMANO -> 8.0
            Deportes.NATACION -> 8.0
            Deportes.REMO -> 7.0
            Deportes.ESCALADA -> 8.0
            Deportes.SENDERISMO -> 6.0
            Deportes.ELIPTICA -> 5.0
            Deportes.BICICLETA_ESTATICA -> 6.8
            Deportes.PESAS -> 6.0
            Deportes.MANCUERNA_UNA -> 3.5
            Deportes.MANCUERNAS_DOS -> 4.5
            Deportes.CINTA_DE_CORRER -> 8.0
            Deportes.SACO_DE_BOXEO -> 6.0
        }
    }
}