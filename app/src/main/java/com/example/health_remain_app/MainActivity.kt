package com.example.health_remain_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.health_remain_app.navigation.AppNavigation
import androidx.work.*
import com.example.health_remain_app.worker.WaterReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppNavigation()
        }
        val workRequest =
            PeriodicWorkRequestBuilder<WaterReminderWorker>(
                2,
                TimeUnit.HOURS
            ).build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "water_reminder",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }
}
