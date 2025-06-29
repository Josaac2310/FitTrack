package com.josecarlos.fittrack.mvc.model

import android.util.Log
import com.josecarlos.fittrack.data.FirebaseInstances
import com.josecarlos.fittrack.data.MetaRegType
import com.josecarlos.fittrack.data.MetaType
import com.josecarlos.fittrack.data.dataClases.Meta
import com.josecarlos.fittrack.data.dataClases.MetaDiaria
import com.josecarlos.fittrack.data.dataClases.MetaReg
import com.josecarlos.fittrack.data.dataClases.Usuario
import com.josecarlos.fittrack.mvc.controller.FirebaseInstance
import kotlinx.coroutines.tasks.await

object CRUDUsuario {

    suspend fun getUser(email:String): Usuario? {
        val fb = FirebaseInstance.getInstance_Firestore()
        Log.d("FIREBASE", fb.collection("Usuarios").get().await().size().toString())
        val result =
            fb.collection("Usuarios")
                .whereEqualTo("email", email)
                .get()
                .await()
        Log.d("FIREBASE", "Resultado de la consulta: ${result.size()} documentos")

        if (result.isEmpty) {
            Log.d("FIREBASE", "No se encontraron documentos para el email: $email")
            return null
        }else{
            val data= result.first().data
            val mapSalud=data.get("datos_salud") as Map<String, String>
            val usuario = Usuario(
                nombre = data.get("nombre").toString(),
                apellidos = data.get("apellidos").toString(),
                email = data.get("email").toString(),
                fecha_nacimiento = data.get("fecha_nacimiento").toString(),
                username = data.get("username").toString(),
                altura = mapSalud.get("altura")!!.toFloat(),
                peso = mapSalud.get("peso")!!.toFloat(),
                sexo = mapSalud.get("sexo")!!.toString()
            )
            return usuario
        }
    }

    suspend fun createUser(usuario:Usuario):Boolean{

        val fb = FirebaseInstance.getInstance_Firestore()

        if (userExists(usuario.email)){
            return false
        }else{
            val user = hashMapOf(
                "nombre" to usuario.nombre,
                "apellidos" to usuario.apellidos,
                "username" to usuario.username,
                "email" to usuario.email,
                "fecha_nacimiento" to usuario.fecha_nacimiento,
                "datos_salud" to mapOf(
                    "altura" to usuario.altura.toString(),
                    "peso" to usuario.peso.toString(),
                    "sexo" to usuario.sexo
                )
            )
            val res= fb.collection("Usuarios")
                .add(user)
                .await()
            if (res==null){
                return false
            }else{
                CRUDMetas.insertMeta(MetaType.DIARIO, MetaDiaria(
                    id = "",
                    type = "calorias",
                    registros = arrayListOf(),
                    activo = true,
                    metaReg = MetaReg(
                        deporte = "general",
                        tipo = MetaRegType.CALORIAS,
                        valor = "300"
                    )
                ) , usuario.email)
                CRUDMetas.insertMeta(MetaType.DIARIO, MetaDiaria(
                    id = "",
                    type = "tiempo",
                    registros = arrayListOf(),
                    activo = true,
                    metaReg = MetaReg(
                        deporte = "general",
                        tipo = MetaRegType.TIEMPO,
                        valor = "30"
                    )
                ) , usuario.email)
                return true
            }
        }
    }

    suspend fun userExists(email:String): Boolean {
        val fb = FirebaseInstance.getInstance_Firestore()
        Log.d("FIREBASE", fb.collection("Usuarios").get().await().size().toString())
        val result =
            fb.collection("Usuarios")
                .whereEqualTo("email", email)
                .get()
                .await()
        Log.d("FIREBASE", "Resultado de la consulta: ${result.size()} documentos")

        if (result.isEmpty) {
            Log.d("FIREBASE", "No se encontraron documentos para el email: $email")
            return false
        }else{
            return true
        }
    }

    suspend fun getUserId(email: String):String?{
        val fb = FirebaseInstance.getInstance_Firestore()
        Log.d("FIREBASE", fb.collection("Usuarios").get().await().size().toString())
        val result =
            fb.collection("Usuarios")
                .whereEqualTo("email", email)
                .get()
                .await()
        Log.d("FIREBASE", "Resultado de la consulta: ${result.size()} documentos")

        if (result.isEmpty) {
            Log.d("FIREBASE", "No se encontraron documentos para el email: $email")
            return null
        }else{
            return result.first().id
        }
    }
}