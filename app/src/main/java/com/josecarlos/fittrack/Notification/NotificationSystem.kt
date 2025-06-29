package com.josecarlos.fittrack.Notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.josecarlos.fittrack.R
import java.util.concurrent.TimeUnit

class NotificationWorker(context: Context, workerParameters: WorkerParameters):Worker(context, workerParameters) {
    override fun doWork(): Result {
        mostrarNotificacion(context = applicationContext)
        return Result.success()
    }

    fun mostrarNotificacion(context: Context){
        val builder = NotificationCompat.Builder(context, "Canal_id")
            .setSmallIcon(R.drawable.logoapp)
            .setContentTitle(NotificationSystem.actualTitle)
            .setContentText(NotificationSystem.actualText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(NotificationSystem.actualId, builder.build())
    }

}

object NotificationSystem{
    var actualId = 0
    var actualTitle = "Recordatorio de actividad física"
    var actualText = "¿Has realizado actividad fisica hoy?\nRecuerda registrar diariamente tu actividad fisica, para que podamos seguir ayudandote a cumplir tus objetivos y metas"

    fun crearCanalNotificacion(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal Recordatorios"
            val descriptionText = "Canal para notificaciones periódicas"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Canal_id", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun crearNotificacionPrueba(context: Context){
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "Notificacion periodica",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

}

