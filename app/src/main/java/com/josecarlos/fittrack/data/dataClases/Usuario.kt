package com.josecarlos.fittrack.data.dataClases

data class Usuario(
    val username:String="",
    val nombre:String="",
    val apellidos:String="",
    val fecha_nacimiento:String="",
    val email:String="",
    val altura:Float=0.0f,
    val peso:Float=0.0f,
    val sexo:String=""
)
