package com.example.health_remain_app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.health_remain_app.MainActivity
import com.example.health_remain_app.R

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {

        val channelId = "water_reminder"

        val notificationManager =
            applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "Water Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.description = "Drink water reminder"

            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(
            applicationContext,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        val notification =
            NotificationCompat.Builder(
                applicationContext,
                channelId
            )
                .setSmallIcon(R.mipmap.ic_launcher) // Changed to mipmap for better visibility
                .setContentTitle("Water Reminder 💧")
                .setContentText("Time to stay hydrated! Tap to record your water intake.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        notificationManager.notify(
            1,
            notification
        )

        return Result.success()
    }
}