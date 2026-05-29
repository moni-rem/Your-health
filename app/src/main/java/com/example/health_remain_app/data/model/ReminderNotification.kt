package com.example.health_remain_app.data.model

data class ReminderNotification(
    val amount: Int,
    val progress: Int,
    val goal: Int,
    val consumed: Int
)