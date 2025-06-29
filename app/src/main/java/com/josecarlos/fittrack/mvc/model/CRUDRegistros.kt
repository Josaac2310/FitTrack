package com.josecarlos.fittrack.mvc.model

import android.util.Log
import com.josecarlos.fittrack.data.dataClases.Registro
import com.josecarlos.fittrack.mvc.controller.CalcFormulas
import com.josecarlos.fittrack.mvc.controller.FirebaseInstance
import kotlinx.coroutines.tasks.await

object CRUDRegistros {

    suspend fun getAllRegistrosOfUser(email:String):ArrayList<Registro>?{
        val fb = FirebaseInstance.getInstance_Firestore()
        val id = CRUDUsuario.getUserId(email)

        if (id == null){
            return null
        }else{
            val array = arrayListOf<Registro>()
            val res = fb.collection("Usuarios")
                .document(id)
                .collection("Registros")
                .get()
                .await()

            for (doc in res) {
                val data = doc.data
                val registro = Registro(
                    id = doc.id,
                    fecha = data.get("fecha").toString(),
                    duracion = data.get("duracion").toString().toFloat(),
                    tipo = CalcFormulas.obtenerDeporteDesdeString(data.get("deporte").toString())!!,
                    metadata = data.get("metadata") as Map<String, Any>,
                    familia = CalcFormulas.obtenerFamiliaDesdeString(data.get("familia").toString())!!
                )
                array.add(registro)
            }
            return array
        }
    }

    suspend fun createRegistro(registro: Registro, email: String):Boolean{
        val fb = FirebaseInstance.getInstance_Firestore()
        val id = CRUDUsuario.getUserId(email)

        if (id == null){
            return false
        }else{
            val reg = hashMapOf(
                "fecha" to registro.fecha,
                "duracion" to registro.duracion,
                "familia" to registro.familia.toString(),
                "deporte" to registro.tipo.toString(),
                "metadata" to registro.metadata
            )

            val res = fb.collection("Usuarios")
                .document(id)
                .collection("Registros")
                .add(reg)
                .await()

            if (res == null){
                return false
            }else{
                return true
            }
        }
    }

    suspend fun updateRegistro(registro: Registro, email:String ):Boolean{
        val fb = FirebaseInstance.getInstance_Firestore()
        val id = CRUDUsuario.getUserId(email)

        if (id == null){
            return false
        }else{

            val reg = hashMapOf(
                "fecha" to registro.fecha,
                "duracion" to registro.duracion,
                "familia" to registro.familia.toString(),
                "deporte" to registro.tipo.toString(),
                "metadata" to registro.metadata
            )

            try {
                val res = fb.collection("Usuarios")
                    .document(id)
                    .collection("Registros")
                    .document(registro.id)
                    .set(reg)
                    .await()
                return true
            }catch (e:Exception){
                e.printStackTrace()
                return false
            }
        }
    }

    suspend fun deleteRegistro(registro: Registro, email: String):Boolean{
        val fb = FirebaseInstance.getInstance_Firestore()
        val id = CRUDUsuario.getUserId(email)

        Log.d("DeleteRegistro", "ID del usuario: $id")
        Log.d("DeleteRegistro", "ID del registro a borrar: ${registro.id}")

        if (id == null){
            Log.d("DeleteRegistro", "No se pudo obtener el ID del usuario")
            return false
        }else{
            try {
                val res = fb.collection("Usuarios")
                    .document(id)
                    .collection("Registros")
                    .document(registro.id)
                    .delete()
                    .await()

                Log.d("DeleteRegistro", "Registro borrado correctamente")
                return true
            }catch (e:Exception){
                Log.e("DeleteRegistro", "Error al borrar: ${e.message}")
                e.printStackTrace()
                return false
            }
        }
    }
}