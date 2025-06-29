package com.josecarlos.fittrack.mvc.model

import com.josecarlos.fittrack.data.MetaType
import com.josecarlos.fittrack.data.dataClases.Caducidad
import com.josecarlos.fittrack.data.dataClases.Meta
import com.josecarlos.fittrack.data.dataClases.MetaCaduca
import com.josecarlos.fittrack.data.dataClases.MetaDiaria
import com.josecarlos.fittrack.data.dataClases.MetaReg
import com.josecarlos.fittrack.data.dataClases.Registro
import com.josecarlos.fittrack.data.dataClases.RegistroMeta
import com.josecarlos.fittrack.data.getMetaRegTypeOfString
import com.josecarlos.fittrack.mvc.controller.FirebaseInstance
import com.josecarlos.fittrack.utils.CalendarSystem.getCurrentDateFormatted
import kotlinx.coroutines.tasks.await


object CRUDMetas {

    suspend fun insertMeta(type: MetaType, meta:Meta, email:String):Boolean{
        val fb = FirebaseInstance.getInstance_Firestore()

        try {
            if (type == MetaType.DIARIO){
                val meta_ = meta as MetaDiaria
                val data = hashMapOf(
                    "tipo" to "diario",
                    "activo" to true,
                    "meta" to mapOf(
                        "deporte" to meta_.metaReg.deporte,
                        "tipo" to meta_.metaReg.tipo,
                        "valor" to meta_.metaReg.valor
                    )
                )

                val id = CRUDUsuario.getUserId(email)

                val res = fb.collection("Usuarios")
                    .document(id!!)
                    .collection("Metas")
                    .add(data)
                    .await()
                return true
            }else{
                val meta_ = meta as MetaCaduca
                val data = hashMapOf(
                    "cancelado" to false,
                    "finalizado" to false,
                    "caducidad" to mapOf(
                        "fecha_cancelacion" to "",
                        "fecha_fin" to meta_.caducidad.fechaFin,
                        "fecha_inicio" to meta_.caducidad.fechaInicio
                    ),
                    "meta" to mapOf(
                        "deporte" to meta.metaReg.deporte,
                        "tipo" to meta.metaReg.tipo,
                        "valor" to meta.metaReg.valor
                    ),
                    "tipo" to "caduco"
                )

                val id = CRUDUsuario.getUserId(email)

                val res= fb.collection("Usuarios")
                    .document(id!!)
                    .collection("Metas")
                    .add(data)
                    .await()
                return true
            }
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }

    }

    suspend fun getMetasOfUsuario(email: String):ArrayList<Meta>{
        val fb = FirebaseInstance.getInstance_Firestore()
        val id = CRUDUsuario.getUserId(email)
        val res = arrayListOf<Meta>()

        val docs = fb.collection("Usuarios")
            .document(id!!)
            .collection("Metas")
            .get()
            .await()

        docs.forEach { doc ->
            val type = doc.get("tipo")

            if (type!!.equals("diario")){
                val registrosArray = arrayListOf<RegistroMeta>()
                val metaRegMap = doc.get("meta") as Map<String, String>

                var registrosMap = mapOf<String, String>()
                val registrosExist = doc.contains("registros")
                if (registrosExist){
                    registrosMap= doc.get("registros") as Map<String, String>
                    registrosMap.forEach { c, v ->
                        val reg = RegistroMeta(fecha = c, valor = v, )
                        registrosArray.add(reg)
                    }
                }
                val meta = MetaDiaria(
                    id = doc.id,
                    type = doc.get("tipo").toString(),
                    registros = registrosArray,
                    metaReg = MetaReg(
                                deporte = metaRegMap.get("deporte").toString(),
                                tipo = getMetaRegTypeOfString(metaRegMap.get("tipo").toString())!!,
                                valor =metaRegMap.get("valor").toString()
                            ),
                    activo = doc.get("activo") as Boolean,
                )
                res.add(meta)

            }
            else{

                val registrosArray = arrayListOf<RegistroMeta>()
                val metaRegMap = doc.get("meta") as Map<String, String>
                val caducidadMap = doc.get("caducidad") as Map<String, String>

                var registrosMap = mapOf<String, String>()
                val registrosExist = doc.contains("registros")
                if (registrosExist){
                    registrosMap= doc.get("registros") as Map<String, String>
                    registrosMap.forEach { c, v ->
                        val reg = RegistroMeta(fecha = c, valor = v )
                        registrosArray.add(reg)
                    }
                }

                val meta = MetaCaduca(
                    id = doc.id,
                    type = doc.get("tipo").toString(),
                    registros = registrosArray,
                    metaReg = MetaReg(
                        deporte = metaRegMap.get("deporte").toString(),
                        tipo = getMetaRegTypeOfString(metaRegMap.get("tipo").toString())!!,
                        valor =metaRegMap.get("valor").toString()
                    ),
                    caducidad = Caducidad(
                        fechaCancelacion = caducidadMap.get("fecha_cancelacion").toString(),
                        fechaInicio = caducidadMap.get("fecha_inicio").toString(),
                        fechaFin = caducidadMap.get("fecha_fin").toString()
                    ),
                    cancelado = doc.get("cancelado") as Boolean,
                    finalizado = doc.get("finalizado") as Boolean
                )
                res.add(meta)

            }
        }
        return res
    }

    suspend fun insertRegistroIntoMeta(email: String, registro: RegistroMeta, metaId:String):Boolean{
        val fb = FirebaseInstance.getInstance_Firestore()
        val id = CRUDUsuario.getUserId(email)

        try {

            val res = fb.collection("Usuarios")
                .document(id!!)
                .collection("Metas")
                .document(metaId)
                .get()
                .await()

            if (res.contains("registros")){
                val map = res.get("registros") as Map<String, String>
                val mapAux = map.toMutableMap()
                mapAux.set(registro.fecha, registro.valor)

                fb.collection("Usuarios")
                    .document(id)
                    .collection("Metas")
                    .document(metaId)
                    .update("registros", mapAux)
                    .await()

                return true
            }
            else{
                val map = mapOf<String, String>()
                val mapAux = map.toMutableMap()
                mapAux.set(registro.fecha, registro.valor)

                fb.collection("Usuarios")
                    .document(id)
                    .collection("Metas")
                    .document(metaId)
                    .update("registros", mapAux)
                    .await()

                return true
            }

        }catch (e:Exception){
            e.printStackTrace()
            return false
        }
    }

    suspend fun activateOrDesactivateMeta(activar:Boolean ,email: String, idMeta: String):Boolean{
        val fb = FirebaseInstance.getInstance_Firestore()
        val id = CRUDUsuario.getUserId(email)

        try{
            fb.collection("Usuarios")
                .document(id!!)
                .collection("Metas")
                .document(idMeta)
                .update("activo", activar)
                .await()
            return true

        }
        catch (e:Exception){
            e.printStackTrace()
            return false
        }
    }

    suspend fun cancelarMeta(idMeta: String, email: String): Boolean{
        val fb = FirebaseInstance.getInstance_Firestore()
        val id = CRUDUsuario.getUserId(email)

        try{
            fb.collection("Usuarios")
                .document(id!!)
                .collection("Metas")
                .document(idMeta)
                .update("cancelado", true)
                .await()

            val actualFecha = getCurrentDateFormatted()
            val actualMap = fb.collection("Usuarios")
                .document(id!!)
                .collection("Metas")
                .document(idMeta)
                .get()
                .await()
                .get("caducidad")

            val newMap = actualMap as MutableMap<String, String>
            newMap.replace("fecha_cancelacion", actualFecha)

            fb.collection("Usuarios")
                .document(id!!)
                .collection("Metas")
                .document(idMeta)
                .update("caducidad", newMap)
                .await()

            return true
        }
        catch (e:Exception){
            e.printStackTrace()
            return false
        }
    }

    suspend fun deleteMeta(idMeta: String, email: String): Boolean {
        val fb = FirebaseInstance.getInstance_Firestore()
        val id = CRUDUsuario.getUserId(email)

        try {
            fb.collection("Usuarios")
                .document(id!!)
                .collection("Metas")
                .document(idMeta)
                .delete()
                .await()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

}


