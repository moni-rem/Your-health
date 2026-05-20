package com.example.health_remain_app.worker



import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.health_remain_app.R

class WaterReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        showNotification()

        return Result.success()
    }

    private fun showNotification() {

        val channelId = "water_reminder_channel"

        val notificationManager =
            applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        // Create channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "Water Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
            .setContentTitle("💧 Drink Water")
            .setContentText("Time to stay hydrated!")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        notificationManager.notify(1, notification)
    }
}