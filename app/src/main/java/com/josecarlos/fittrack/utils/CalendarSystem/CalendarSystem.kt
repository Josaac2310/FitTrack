package com.josecarlos.fittrack.utils.CalendarSystem

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log

import com.josecarlos.fittrack.data.dataClases.Registro
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.Date


data class Mes(
    val nombre:String = "",
    var dias:ArrayList<Dia> = arrayListOf(),
    val int: Int = 0
)

data class Anio(
    var meses:ArrayList<Mes> = arrayListOf(),
    val nombre:String = "",
    val int: Int = 0
)

data class Dia(
    val registros: ArrayList<Registro> = arrayListOf(),
    val nombre:String = "",
    val int: Int = 0,
    val date:LocalDate = LocalDate.now()
)


object CalendarSystem {
    var calendarCharged = false
    var anios :ArrayList<Anio> = arrayListOf()

    fun chargeCalendar(){
        anios = arrayListOf()

        //cargamos años desde el 2025 hasta el actual
        val currentYear = LocalDate.now().year
        val startYear = 2025
        val yearsInt = (startYear..currentYear).toList()
        val years:ArrayList<Anio> = arrayListOf()

        //cargar los meses
        yearsInt.forEach { year ->
            val yearAux = Anio(arrayListOf(), year.toString(), year)
            val meses = arrayListOf<Mes>()
            for (i in 1..12){
                val mes = Mes(getStringOfMes(i), arrayListOf<Dia>(), i)
                val daysOfMes = YearMonth.of(year, i).lengthOfMonth()
                val dias = arrayListOf<Dia>()

                for (d in 1..daysOfMes){
                    val date = LocalDate.of(year, i, d)
                    val dia = Dia(arrayListOf(), diaEnEspanol(date), d, date)
                    dias.add(dia)
                }
                mes.dias=dias
                meses.add(mes)
            }
            yearAux.meses=meses
            years.add(yearAux)
        }

        years.forEach { y ->
            Log.d("prueba2", "anio cargado: "+y.int)
        }
        anios = years
        calendarCharged = true
    }

    fun initData(registros:ArrayList<Registro> = arrayListOf()){
        if (calendarCharged){
            anios.forEach { y ->
                Log.d("prueba3", "anio cargado: "+y.int)
            }
            registros.forEach { reg ->
                val fecha = parseDateFromFirebase(reg.fecha)

                // Extraemos datos de la fecha
                val year = fecha!!.year
                val month = fecha!!.monthValue
                val day = fecha!!.dayOfMonth

                // Vamos filtrando de la lista anios
                val aniosAux = anios.filter { a -> a.int == year }
                if (aniosAux.isEmpty()){
                    Log.d("prueba3","anio no encontrado : "+year )
                }
                else{
                    val anio = aniosAux.first()
                    val mesesAux = anio.meses.filter { m -> m.int == month }

                    if (mesesAux.isEmpty()){
                        Log.d("prueba3","mes no encontrado : "+month )
                    }
                    else{
                        val mes = mesesAux.first()
                        val diasAux = mes.dias.filter { d -> d.int == day }
                        if (diasAux.isEmpty()){
                            Log.d("prueba3","dia no encontrado : "+day )
                        }
                        else{
                            val dia = diasAux.first()
                            dia.registros.add(reg)
                            Log.d("prueba3","registro añadido correctamente en el dia: "+day)

                        }
                    }
                }
            }
        }
        else{
            Log.d("prueba3","Error, calendario no cargado")
        }
    }

    fun getTodayDia(): Dia? {
        if (!calendarCharged) {
            Log.d("CalendarSystem", "Calendario no ha sido cargado.")
            return null
        }

        val today = LocalDate.now()
        val year = today.year
        val month = today.monthValue
        val day = today.dayOfMonth

        val anio = anios.find { it.int == year } ?: return null
        val mes = anio.meses.find { it.int == month } ?: return null
        val dia = mes.dias.find { it.int == day }

        return dia
    }

    fun getLunesOfYears(): Pair<Int, ArrayList<Pair<String, String>>>{

        var lista = arrayListOf<Pair<String, String>>()
        var actualLunes = LocalDate.now().dayOfMonth
        var diference = 100
        var punter = 0
        var punterObj = 0


        anios.forEach { a->
            a.meses.forEach { m->
                val lunesAux = m.dias.filter { d-> d.date.dayOfWeek == DayOfWeek.WEDNESDAY }
                var i = 1
                lunesAux.forEach { l->
                    if(a.int == LocalDate.now().year){
                        if(m.int == LocalDate.now().monthValue){
                            if(Math.abs(actualLunes - l.date.dayOfMonth) < diference){
                                diference = Math.abs(actualLunes - l.date.dayOfMonth)
                                punterObj = punter
                            }
                        }
                    }
                    val d = Pair(getCurrentDateFormatted2(l.date), "Semana "+i+" de "+m.nombre+" ("+a.int.toString()+")")
                    lista.add(d)
                    i++
                    punter++
                }

            }
        }

        return Pair(punterObj,lista)

    }


    fun generarInformePDF(context: Context, date:String){
        val docPdf = PdfDocument()
        val paginaInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val pagina = docPdf.startPage(paginaInfo)
        val canvas: Canvas = pagina.canvas
        val paint = Paint()
        paint.textSize = 32f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("Informe de actividad deportiva (${date})", 80f, 50f, paint)
        docPdf.finishPage(pagina)
        var counter = 2
        anios.forEach { a ->
            a.meses.forEach { m ->
                m.dias.forEach { d ->
                    if (d.registros.isNotEmpty()) {
                        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, counter).create()
                        counter++

                        val page = docPdf.startPage(pageInfo)
                        val canvas = page.canvas

                        // Encabezado
                        paint.textSize = 20f
                        paint.textAlign = Paint.Align.CENTER
                        canvas.drawText(
                            "${d.int} de ${m.nombre} del ${a.int}",
                            pageInfo.pageWidth / 2f,
                            50f,
                            paint
                        )

                        // Detalles de registros
                        paint.textSize = 13f
                        paint.textAlign = Paint.Align.LEFT

                        val startX = 40f
                        var startY = 90f
                        val lineSpacing = 20f

                        d.registros.forEach { r ->
                            val line = "(${r.fecha}), ${r.tipo}, ${r.duracion}"
                            canvas.drawText(line, startX, startY, paint)
                            startY += lineSpacing
                        }

                        docPdf.finishPage(page)
                    }
                }
            }
        }
        val outPutStream = ByteArrayOutputStream()
        docPdf.writeTo(outPutStream)
        docPdf.close()
        guardarPdfEnDownloads(context, "Informe_FitTrack_"+date, outPutStream.toByteArray() )

    }

}

fun guardarPdfEnDownloads(context: Context, nombreArchivo: String, pdfData: ByteArray) {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
    }

    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

    if (uri != null) {
        resolver.openOutputStream(uri).use { outputStream: OutputStream? ->
            outputStream?.write(pdfData)
            outputStream?.flush()
        }
        println("PDF guardado en Downloads con éxito")
        abrirPdf(context, uri)
    } else {
        println("Error al crear el archivo PDF en Downloads")
    }
}

fun abrirPdf(context: Context, pdfUri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(pdfUri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
    }

    // Verificar que haya alguna app que pueda abrir PDFs
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        // No hay app para abrir PDF
        println("No hay ninguna app para abrir PDF instalada")
    }
}





