package com.josecarlos.fittrack.data.dataClases

import com.josecarlos.fittrack.data.Deportes
import com.josecarlos.fittrack.data.Familias

data class Registro(
    val id:String="",
    val fecha:String="",
    val duracion:Float=0.0f,
    val familia:Familias=Familias.CARDIO,
    val tipo:Deportes=Deportes.CORRER,
    val metadata: Map<String, Any> = mapOf()
)


