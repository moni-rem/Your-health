package com.example.health_remain_app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors =
    darkColorScheme(
        primary = BlueLight,
        background = DarkBackground,
        surface = DarkCard,
        onBackground = Color.White,
        onSurface = Color.White,
        onSurfaceVariant = Color.Gray
    )

private val LightColors =
    lightColorScheme(
        primary = BlueLight,
        background = Color(0xFFF5FBFF),
        surface = Color.White,
        onBackground = Color(0xFF1F2937),
        onSurface = Color(0xFF1F2937),
        onSurfaceVariant = Color.Gray
    )

@Composable
fun HealthRemainTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}