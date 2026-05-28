package com.example.health_remain_app.navigation


sealed class Screen(val route: String) {

    object Home : Screen("home")
    object Reminder : Screen("reminder")
    object History : Screen("history")
    object Profile : Screen("profile")
}
