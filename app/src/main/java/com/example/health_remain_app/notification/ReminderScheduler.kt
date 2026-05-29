package com.example.health_remain_app.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

object ReminderScheduler {

    fun scheduleReminder(
        context: Context,
        intervalMinutes: Int,
        amount: Int
    ) {
        val intent = Intent(context, WaterReminderReceiver::class.java).apply {
            putExtra("amount", amount)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = System.currentTimeMillis() + intervalMinutes * 60 * 1000L

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            intervalMinutes * 60 * 1000L,
            pendingIntent
        )
    }

    // New function specifically for testing the 1-minute mock notification
    fun scheduleTestReminder(context: Context) {
        val intent = Intent(context, WaterReminderReceiver::class.java).apply {
            putExtra("amount", 250)
            putExtra("isTest", true)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            999, // Unique request code for test
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = System.currentTimeMillis() + 60 * 1000L // 1 minute from now

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }
}