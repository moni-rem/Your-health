package com.example.health_remain_app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.health_remain_app.navigation.AppNavigation
import com.example.health_remain_app.notification.NotificationHelper
import com.example.health_remain_app.ui.theme.HealthRemainTheme
import com.example.health_remain_app.viewmodel.ThemeViewModel
import kotlin.jvm.java

class MainActivity : ComponentActivity() {

    private val themeViewModel by lazy {
        ViewModelProvider(this)[ThemeViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create notification channel
        NotificationHelper.createChannel(this)

        setContent {

            HealthRemainTheme(
                darkTheme = themeViewModel.isDarkMode
            ) {

                AppNavigation()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS
                ),
                1
            )
        }
    }
}