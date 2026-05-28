package com.example.health_remain_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.health_remain_app.data.model.WaterRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HealthViewModel : ViewModel() {

    var cups by mutableIntStateOf(0)
        private set

    private var _waterGoal by mutableIntStateOf(2000)
    val waterGoal: Int get() = _waterGoal

    var history = mutableStateListOf<WaterRecord>()
        private set

    fun setWaterGoal(goal: Int) {
        _waterGoal = goal
    }

    fun drinkWater() {

        cups++

        val currentDate =
            SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault()
            ).format(Date())

        history.add(
            WaterRecord(
                date = currentDate,
                amount = 250
            )
        )
    }

    fun getTodayTotal(): Int {

        val today =
            SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault()
            ).format(Date())

        return history
            .filter { it.date == today }
            .sumOf { it.amount }
    }

    fun getWeeklyTotal(): Int {

        return history.sumOf { it.amount }
    }

    fun getMonthlyTotal(): Int {

        return history.sumOf { it.amount }
    }
}
