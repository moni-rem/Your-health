package com.example.health_remain_app.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.health_remain_app.data.firebase.RealtimeDatabaseManager
import com.example.health_remain_app.data.model.WaterRecord
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HealthViewModel(application: Application) :
    AndroidViewModel(application) {

    private val prefs =
        application.getSharedPreferences(
            "health_prefs",
            Context.MODE_PRIVATE
        )

    private val auth = FirebaseAuth.getInstance()

    // =========================
    // WATER GOAL
    // =========================

    private var _waterGoal by mutableIntStateOf(
        prefs.getInt("water_goal", 2400)
    )

    val waterGoal: Int
        get() = _waterGoal

    // =========================
    // HISTORY
    // =========================

    var history =
        mutableStateListOf<WaterRecord>()
        private set

    init {

        loadHistory()
    }

    // =========================
    // SET WATER GOAL
    // =========================

    fun setWaterGoal(goal: Int) {

        _waterGoal = goal

        prefs.edit()
            .putInt("water_goal", goal)
            .apply()
    }

    // =========================
    // DRINK WATER
    // =========================

    fun drinkWater(amount: Int = 250) {

        val currentDate =
            SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault()
            ).format(Date())

        val currentTime =
            SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            ).format(Date())

        val record = WaterRecord(
            date = currentDate,
            time = currentTime,
            amount = amount
        )

        history.add(record)

        saveHistory()

        // SAVE TO FIREBASE

        auth.currentUser?.uid?.let { userId ->

            RealtimeDatabaseManager.saveWaterRecord(
                userId,
                record
            )
        }
    }

    // =========================
    // SAVE HISTORY
    // =========================

    private fun saveHistory() {

        val serialized =
            history.joinToString(";") {

                "${it.date}|${it.time}|${it.amount}"
            }

        prefs.edit()
            .putString(
                "history_data",
                serialized
            )
            .apply()
    }

    // =========================
    // LOAD HISTORY
    // =========================

    private fun loadHistory() {

        val data =
            prefs.getString(
                "history_data",
                ""
            ) ?: ""

        if (data.isNotEmpty()) {

            val items = data.split(";")

            items.forEach { item ->

                val parts = item.split("|")

                if (parts.size == 3) {

                    history.add(
                        WaterRecord(
                            date = parts[0],
                            time = parts[1],
                            amount = parts[2].toInt()
                        )
                    )
                }
            }
        }
    }

    // =========================
    // FIREBASE GOAL
    // =========================

    fun saveGoalToFirestore() {

        auth.currentUser?.uid?.let { userId ->

            RealtimeDatabaseManager.saveWaterGoal(
                userId,
                waterGoal
            )
        }
    }

    // =========================
    // TODAY TOTAL
    // =========================

    fun getTodayTotal(): Int {

        val today =
            SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault()
            ).format(Date())

        return history
            .filter {

                it.date == today
            }
            .sumOf {

                it.amount
            }
    }

    // =========================
    // WEEKLY TOTAL
    // =========================

    fun getWeeklyTotal(): Int {

        val calendar =
            Calendar.getInstance()

        val currentWeek =
            calendar.get(
                Calendar.WEEK_OF_YEAR
            )

        val currentYear =
            calendar.get(
                Calendar.YEAR
            )

        return history.filter {

            val sdf =
                SimpleDateFormat(
                    "dd MMM yyyy",
                    Locale.getDefault()
                )

            val recordDate =
                sdf.parse(it.date)

            val recordCalendar =
                Calendar.getInstance()

            recordCalendar.time =
                recordDate!!

            val recordWeek =
                recordCalendar.get(
                    Calendar.WEEK_OF_YEAR
                )

            val recordYear =
                recordCalendar.get(
                    Calendar.YEAR
                )

            recordWeek == currentWeek &&
                    recordYear == currentYear

        }.sumOf {

            it.amount
        }
    }

    // =========================
    // MONTHLY TOTAL
    // =========================

    fun getMonthlyTotal(): Int {

        val calendar =
            Calendar.getInstance()

        val currentMonth =
            calendar.get(
                Calendar.MONTH
            )

        val currentYear =
            calendar.get(
                Calendar.YEAR
            )

        return history.filter {

            val sdf =
                SimpleDateFormat(
                    "dd MMM yyyy",
                    Locale.getDefault()
                )

            val recordDate =
                sdf.parse(it.date)

            val recordCalendar =
                Calendar.getInstance()

            recordCalendar.time =
                recordDate!!

            val recordMonth =
                recordCalendar.get(
                    Calendar.MONTH
                )

            val recordYear =
                recordCalendar.get(
                    Calendar.YEAR
                )

            recordMonth == currentMonth &&
                    recordYear == currentYear

        }.sumOf {

            it.amount
        }
    }

    // =========================
    // WEEKLY AVERAGE
    // =========================

    fun getAverageWeekly(): Int {

        return getWeeklyTotal() / 7
    }

    // =========================
    // MONTHLY AVERAGE
    // =========================

    fun getAverageMonthly(): Int {

        return getMonthlyTotal() / 30
    }

    // =========================
    // DAILY TOTALS
    // =========================

    fun getDailyTotals():
            Map<String, Int> {

        return history.groupBy {

            it.date

        }.mapValues { entry ->

            entry.value.sumOf {

                it.amount
            }
        }
    }

    // =========================
    // GOAL COMPLETION
    // =========================

    fun getGoalCompletionPercent(): Int {

        val grouped =
            history.groupBy {

                it.date
            }

        val totalDays =
            grouped.size

        if (totalDays == 0)
            return 0

        val completedDays =
            grouped.count { entry ->

                entry.value.sumOf {

                    it.amount

                } >= waterGoal
            }

        return (
                (
                        completedDays.toFloat()
                                / totalDays
                        ) * 100
                ).toInt()
    }


    //late 7 days
    fun getLast7DaysPercentages(): List<Int> {

        val grouped =
            history.groupBy { it.date }

        return grouped.values
            .toList()
            .takeLast(7)
            .map { records ->

                val total =
                    records.sumOf { it.amount }

                ((total.toFloat() / waterGoal) * 100)
                    .toInt()
                    .coerceAtMost(100)
            }
    }

    private var _defaultDrinkAmount by mutableIntStateOf(
        prefs.getInt("default_drink_amount", 250)
    )

    val defaultDrinkAmount: Int
        get() = _defaultDrinkAmount

    fun setDefaultDrinkAmount(amount: Int) {

        _defaultDrinkAmount = amount

        prefs.edit()
            .putInt(
                "default_drink_amount",
                amount
            )
            .apply()
    }
}