package com.josecarlos.fittrack.data.dataClases

import com.josecarlos.fittrack.data.MetaRegType

open class Meta(
    open val id: String,
    open val type: String,
    open val registros: ArrayList<RegistroMeta>,
    open val metaReg: MetaReg,
)

data class MetaDiaria(
    override val id: String,
    override val type: String,
    override val registros: ArrayList<RegistroMeta>,
    override val metaReg: MetaReg,
    val activo:Boolean
):Meta(
    id, type, registros, metaReg
)

data class MetaCaduca(
    override val id: String,
    override val type: String,
    override val registros: ArrayList<RegistroMeta>,
    override val metaReg: MetaReg,
    val cancelado:Boolean,
    val finalizado:Boolean,
    val caducidad: Caducidad
):Meta(
    id, type, registros, metaReg
)

data class Caducidad(
    val fechaCancelacion:String,
    val fechaFin:String,
    val fechaInicio:String
)

data class MetaReg(
    val deporte:String,
    val tipo: MetaRegType,
    val valor: String
)

data class RegistroMeta(
    val fecha:String,
    val valor:String,

)

