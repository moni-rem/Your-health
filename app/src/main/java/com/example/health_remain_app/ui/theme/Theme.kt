package com.example.health_remain_app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColors =
    darkColorScheme(
        primary = BlueLight,
        background = DarkBackground,
        surface = DarkCard
    )

private val LightColors =
    lightColorScheme(
        primary = BlueLight,
        background = androidx.compose.ui.graphics.Color.White,
        surface = androidx.compose.ui.graphics.Color.White
    )

@Composable
fun HealthRemainTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme =
            if (darkTheme)
                DarkColors
            else
                LightColors,
        content = content
    )
}