package com.example.health_remain_app.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object ReminderScheduler {

    fun scheduleReminder(
        context: Context,
        intervalMinutes: Int,
        amount: Int
    ) {

        val intent =
            Intent(
                context,
                WaterReminderReceiver::class.java
            ).apply {

                putExtra(
                    "amount",
                    amount
                )
            }

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        val triggerTime =
            System.currentTimeMillis() +
                    intervalMinutes * 60 * 1000

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            intervalMinutes * 60 * 1000L,
            pendingIntent
        )
    }
}