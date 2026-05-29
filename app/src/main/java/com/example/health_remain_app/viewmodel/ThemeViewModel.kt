package com.example.health_remain_app.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class ThemeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val prefs =
        application.getSharedPreferences(
            "theme_prefs",
            Context.MODE_PRIVATE
        )

    var isDarkMode by mutableStateOf(
        prefs.getBoolean("dark_mode", false)
    )
        private set

    fun toggleDarkMode() {

        isDarkMode = !isDarkMode

        prefs.edit()
            .putBoolean(
                "dark_mode",
                isDarkMode
            )
            .apply()
    }

}