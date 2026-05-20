package com.example.health_remain_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.health_remain_app.data.model.Meal


class HealthViewModel : ViewModel() {

    var waterIntake by mutableStateOf(0)
        private set

    val dailyGoal = 2000

    val meals = mutableStateListOf(
        Meal("Breakfast", false),
        Meal("Lunch", false),
        Meal("Dinner", false)
    )

    fun addWater(amount: Int) {
        waterIntake += amount
    }

    fun toggleMeal(index: Int) {
        meals[index] = meals[index].copy(
            completed = !meals[index].completed
        )
    }

    fun getProgress(): Float {
        return waterIntake.toFloat() / dailyGoal.toFloat()
    }
}