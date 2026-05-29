package com.example.health_remain_app.notification

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.health_remain_app.MainActivity
import com.example.health_remain_app.R

class WaterReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val amount =
            intent.getIntExtra("amount", 250)

        val openIntent =
            Intent(
                context,
                MainActivity::class.java
            )

        val openPendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                openIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

        val notification =
            NotificationCompat.Builder(
                context,
                NotificationHelper.CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("💧 Stay Hydrated!")
                .setContentText(
                    "Drink ${amount}ml of water"
                )
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "Drink ${amount}ml of water now."
                        )
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setContentIntent(
                    openPendingIntent
                )
                .setAutoCancel(true)
                .build()

        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {

            NotificationManagerCompat
                .from(context)
                .notify(
                    1001,
                    notification
                )
        }
    }
}