package com.example.health_remain_app.data.model

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val profilePictureUrl: String = "",
    val gender: String = "Male",
    val weight: Float = 70f, // kg
    val height: Float = 170f, // cm
    val activityLevel: String = "Moderate", // Sedentary, Moderate, Active
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val lastGoalReachedDate: String = "",
    val achievements: List<String> = emptyList()
)