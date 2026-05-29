package com.example.health_remain_app.notification

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.health_remain_app.MainActivity
import com.example.health_remain_app.R
import com.example.health_remain_app.data.firebase.RealtimeDatabaseManager
import com.example.health_remain_app.data.model.WaterRecord
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class WaterReminderReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_DONE = "com.example.health_remain_app.ACTION_DONE"
        const val ACTION_LATER = "com.example.health_remain_app.ACTION_LATER"
        const val NOTIFICATION_ID = 1001
    }

    override fun onReceive(context: Context, intent: Intent) {
        val amount = intent.getIntExtra("amount", 250)

        when (intent.action) {
            ACTION_DONE -> {
                handleDoneAction(context, amount)
                return
            }
            ACTION_LATER -> {
                handleLaterAction(context)
                return
            }
            else -> {
                showNotification(context, amount)
            }
        }
    }

    private fun showNotification(context: Context, amount: Int) {
        val prefs = context.getSharedPreferences("health_prefs", Context.MODE_PRIVATE)
        val goal = prefs.getInt("water_goal", 2400)
        
        // Calculate progress
        val historyData = prefs.getString("history_data", "") ?: ""
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val todayTotal = historyData.split(";")
            .filter { it.startsWith(today) }
            .sumOf { 
                val parts = it.split("|")
                if (parts.size == 3) parts[2].toInt() else 0 
            }
        
        val progress = if (goal > 0) (todayTotal * 100 / goal) else 0

        val remoteViews = RemoteViews(context.packageName, R.layout.notification_custom)
        remoteViews.setTextViewText(R.id.notif_msg, "Drink ${amount}ml of water")
        remoteViews.setProgressBar(R.id.notif_progress, 100, progress, false)
        remoteViews.setTextViewText(R.id.notif_progress_text, "$progress%")

        // Done Action
        val doneIntent = Intent(context, WaterReminderReceiver::class.java).apply {
            action = ACTION_DONE
            putExtra("amount", amount)
        }
        val donePendingIntent = PendingIntent.getBroadcast(
            context, 1, doneIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        remoteViews.setOnClickPendingIntent(R.id.btn_done, donePendingIntent)

        // Later Action
        val laterIntent = Intent(context, WaterReminderReceiver::class.java).apply {
            action = ACTION_LATER
        }
        val laterPendingIntent = PendingIntent.getBroadcast(
            context, 2, laterIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        remoteViews.setOnClickPendingIntent(R.id.btn_later, laterPendingIntent)

        val builder = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.waterdrop)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(donePendingIntent) // Clicking notification also adds water
            .setAutoCancel(true)
            .setOngoing(false)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun handleDoneAction(context: Context, amount: Int) {
        val prefs = context.getSharedPreferences("health_prefs", Context.MODE_PRIVATE)
        val historyData = prefs.getString("history_data", "") ?: ""
        
        val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val newRecord = "$currentDate|$currentTime|$amount"
        
        // Save locally
        val newHistory = if (historyData.isEmpty()) newRecord else "$historyData;$newRecord"
        prefs.edit().putString("history_data", newHistory).apply()

        // Sync with Firebase
        val auth = FirebaseAuth.getInstance()
        auth.currentUser?.uid?.let { userId ->
            val record = WaterRecord(date = currentDate, time = currentTime, amount = amount)
            RealtimeDatabaseManager.saveWaterRecord(userId, record)
        }
        
        // Open the app to show progress
        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(launchIntent)

        // Cancel notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun handleLaterAction(context: Context) {
        // Schedule another reminder in 15 minutes
        ReminderScheduler.scheduleReminder(context, 15, 250)
        
        // Cancel current notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }
}
