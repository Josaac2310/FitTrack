package com.josecarlos.fittrack.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.SportsVolleyball
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.ui.text.toLowerCase
import com.josecarlos.fittrack.data.dataClases.MetaReg
import java.util.Locale

fun getIconForDeporte(deporte: Deportes) = when (deporte) {
    Deportes.CAMINAR -> Icons.Filled.DirectionsWalk
    Deportes.CORRER -> Icons.Filled.DirectionsRun
    Deportes.BICICLETA -> Icons.Filled.DirectionsBike
    Deportes.FLEXIONES -> Icons.Filled.FitnessCenter
    Deportes.DOMINADAS -> Icons.Filled.FitnessCenter
    Deportes.SENTADILLAS -> Icons.Filled.FitnessCenter
    Deportes.ABDOMINALES -> Icons.Filled.FitnessCenter
    Deportes.BOXEO -> Icons.Filled.SportsMma
    Deportes.KARATE_TAEKWONDO -> Icons.Filled.SelfImprovement
    Deportes.KICKBOXING_MUAYTHAI -> Icons.Filled.SportsMma
    Deportes.FUTBOL -> Icons.Filled.SportsSoccer
    Deportes.BALONCESTO -> Icons.Filled.SportsBasketball
    Deportes.TENNIS -> Icons.Filled.SportsTennis
    Deportes.VOLLEY -> Icons.Filled.SportsVolleyball
    Deportes.BALONMANO -> Icons.Filled.SportsHandball
    Deportes.NATACION -> Icons.Filled.Pool
    Deportes.REMO -> Icons.Filled.DirectionsBoat
    Deportes.ESCALADA -> Icons.Filled.Terrain
    Deportes.SENDERISMO -> Icons.Filled.Hiking
    Deportes.ELIPTICA -> Icons.Filled.FitnessCenter
    Deportes.BICICLETA_ESTATICA -> Icons.Filled.FitnessCenter
    Deportes.PESAS -> Icons.Filled.FitnessCenter
    Deportes.MANCUERNA_UNA -> Icons.Filled.FitnessCenter
    Deportes.MANCUERNAS_DOS -> Icons.Filled.FitnessCenter
    Deportes.CINTA_DE_CORRER -> Icons.Filled.DirectionsRun
    Deportes.SACO_DE_BOXEO -> Icons.Filled.SportsMma
}

enum class Deportes {
    CAMINAR,
    CORRER,
    BICICLETA,
    FLEXIONES,
    DOMINADAS,
    SENTADILLAS,
    ABDOMINALES,
    BOXEO,
    KARATE_TAEKWONDO,
    KICKBOXING_MUAYTHAI,
    FUTBOL,
    BALONCESTO,
    TENNIS,
    VOLLEY,
    BALONMANO,
    NATACION,
    REMO,
    ESCALADA,
    SENDERISMO,
    ELIPTICA,
    BICICLETA_ESTATICA,
    PESAS,
    MANCUERNA_UNA,
    MANCUERNAS_DOS,
    CINTA_DE_CORRER,
    SACO_DE_BOXEO
}

enum class Familias {
    CARDIO,
    CALISTENIA,
    COMBATE,
    EQUIPO,
    ACUATICO,
    AVENTURA,
    GIMNASIO
}

enum class FirebaseInstances{
    FIRESTORE,
    STORAGE,
    AUTH
}

enum class MetaType{
    DIARIO,
    CADUCO
}

enum class MetaRegType{
    CALORIAS,
    DISTANCIA,
    TIEMPO,
    CHECK,
    UNDEFINED
}

fun getMetaRegTypeOfString(s:String):MetaRegType?{
    if (s.lowercase() == MetaRegType.TIEMPO.toString().lowercase(Locale.getDefault())){
        return MetaRegType.TIEMPO
    }else if (s.lowercase() == MetaRegType.CALORIAS.toString().lowercase(Locale.getDefault())){
        return MetaRegType.CALORIAS
    }else if (s.lowercase() == MetaRegType.DISTANCIA.toString().lowercase(Locale.getDefault())){
        return MetaRegType.DISTANCIA
    }else if (s.lowercase() == MetaRegType.CHECK.toString().lowercase(Locale.getDefault())){
        return MetaRegType.CHECK
    }else {
        return MetaRegType.UNDEFINED
    }
}

fun getFamiliaDeporte(deporte: Deportes): Familias = when (deporte) {
    Deportes.CAMINAR,
    Deportes.CORRER,
    Deportes.BICICLETA,
    Deportes.ELIPTICA,
    Deportes.BICICLETA_ESTATICA,
    Deportes.CINTA_DE_CORRER,
    Deportes.SENDERISMO -> Familias.CARDIO

    Deportes.FLEXIONES,
    Deportes.DOMINADAS,
    Deportes.SENTADILLAS,
    Deportes.ABDOMINALES,
    Deportes.PESAS,
    Deportes.MANCUERNA_UNA,
    Deportes.MANCUERNAS_DOS -> Familias.CALISTENIA

    Deportes.BOXEO,
    Deportes.SACO_DE_BOXEO,
    Deportes.KARATE_TAEKWONDO,
    Deportes.KICKBOXING_MUAYTHAI -> Familias.COMBATE

    Deportes.FUTBOL,
    Deportes.BALONCESTO,
    Deportes.TENNIS,
    Deportes.VOLLEY,
    Deportes.BALONMANO -> Familias.EQUIPO

    Deportes.NATACION,
    Deportes.REMO -> Familias.ACUATICO

    Deportes.ESCALADA -> Familias.AVENTURA

    else -> Familias.GIMNASIO
}

fun getDeportesOfFamilia(familia: Familias): List<Deportes> {
    return when (familia) {
        Familias.CARDIO -> listOf(
            Deportes.CAMINAR,
            Deportes.CORRER,
            Deportes.BICICLETA,
            Deportes.ELIPTICA,
            Deportes.BICICLETA_ESTATICA,
            Deportes.CINTA_DE_CORRER,
            Deportes.SENDERISMO
        )

        Familias.CALISTENIA -> listOf(
            Deportes.FLEXIONES,
            Deportes.DOMINADAS,
            Deportes.SENTADILLAS,
            Deportes.ABDOMINALES,
            Deportes.PESAS,
            Deportes.MANCUERNA_UNA,
            Deportes.MANCUERNAS_DOS
        )

        Familias.COMBATE -> listOf(
            Deportes.BOXEO,
            Deportes.SACO_DE_BOXEO,
            Deportes.KARATE_TAEKWONDO,
            Deportes.KICKBOXING_MUAYTHAI
        )

        Familias.EQUIPO -> listOf(
            Deportes.FUTBOL,
            Deportes.BALONCESTO,
            Deportes.TENNIS,
            Deportes.VOLLEY,
            Deportes.BALONMANO
        )

        Familias.ACUATICO -> listOf(
            Deportes.NATACION,
            Deportes.REMO
        )

        Familias.AVENTURA -> listOf(
            Deportes.ESCALADA
        )

        Familias.GIMNASIO -> emptyList() // O puedes añadir aquí otros deportes si aplica
    }
}