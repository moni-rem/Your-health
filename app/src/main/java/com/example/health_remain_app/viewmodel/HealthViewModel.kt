package com.example.health_remain_app.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.health_remain_app.data.firebase.RealtimeDatabaseManager
import com.example.health_remain_app.data.model.UserProfile
import com.example.health_remain_app.data.model.WaterRecord
import com.example.health_remain_app.notification.ReminderScheduler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class HealthViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("health_prefs", Context.MODE_PRIVATE)
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    var userProfile by mutableStateOf(UserProfile())
        private set

    private var _waterGoal by mutableIntStateOf(prefs.getInt("water_goal", 2400))
    val waterGoal: Int get() = _waterGoal

    var history = mutableStateListOf<WaterRecord>()
        private set

    private var _reminderInterval by mutableIntStateOf(prefs.getInt("reminder_interval", 30))
    val reminderInterval: Int get() = _reminderInterval

    init {
        loadHistory()
        loadUserProfile()
    }

    private fun loadUserProfile() {
        auth.currentUser?.uid?.let { userId ->
            RealtimeDatabaseManager.getUserProfile(userId) { profile ->
                profile?.let {
                    userProfile = it
                    checkAndResetStreak(it)
                    if (!prefs.contains("water_goal")) {
                        _waterGoal = calculateRecommendedWater().toInt()
                    }
                }
            }
        }
    }

    private fun checkAndResetStreak(profile: UserProfile) {
        if (profile.lastGoalReachedDate.isEmpty()) return
        
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val lastReached = try { sdf.parse(profile.lastGoalReachedDate) } catch (e: Exception) { null }
        val today = Calendar.getInstance().apply { 
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        if (lastReached != null) {
            val diff = today.time - lastReached.time
            val daysDiff = TimeUnit.MILLISECONDS.toDays(diff)
            
            if (daysDiff > 1L) {
                auth.currentUser?.uid?.let { userId ->
                    userProfile = userProfile.copy(currentStreak = 0)
                    RealtimeDatabaseManager.updateStreak(userId, 0, userProfile.bestStreak)
                }
            }
        }
    }

    fun updateUserProfile(profile: UserProfile) {
        userProfile = profile
        auth.currentUser?.uid?.let { userId ->
            RealtimeDatabaseManager.saveUserProfile(userId, profile)
        }
        _waterGoal = calculateRecommendedWater().toInt()
        saveGoalToFirestore()
    }

    fun uploadProfilePicture(uri: Uri, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("profile_pictures/$userId.jpg")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val updatedProfile = userProfile.copy(profilePictureUrl = downloadUri.toString())
                    updateUserProfile(updatedProfile)
                    onComplete(true)
                }
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun calculateBMI(): Float {
        if (userProfile.height <= 0) return 0f
        val heightInMeters = userProfile.height / 100f
        return userProfile.weight / (heightInMeters * heightInMeters)
    }

    fun calculateRecommendedWater(): Float {
        var baseWater = userProfile.weight * 35f
        baseWater += when (userProfile.activityLevel) {
            "Active" -> 500f
            "Moderate" -> 250f
            else -> 0f
        }
        if (userProfile.gender == "Male") baseWater += 200f
        return baseWater
    }

    fun setWaterGoal(goal: Int) {
        _waterGoal = goal
        prefs.edit().putInt("water_goal", goal).apply()
        saveGoalToFirestore()
    }

    fun updateReminderInterval(minutes: Int) {
        _reminderInterval = minutes
        prefs.edit().putInt("reminder_interval", minutes).apply()
        
        // Reschedule alarm with new interval
        ReminderScheduler.scheduleReminder(
            getApplication(),
            minutes,
            _defaultDrinkAmount
        )
    }

    fun drinkWater(amount: Int = 250) {
        val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

        val record = WaterRecord(date = currentDate, time = currentTime, amount = amount)
        history.add(record)
        saveHistory()

        auth.currentUser?.uid?.let { userId ->
            RealtimeDatabaseManager.saveWaterRecord(userId, record)
        }

        checkGoalReached(currentDate)
    }

    private fun checkGoalReached(today: String) {
        val totalToday = getTodayTotal()
        if (totalToday >= waterGoal && userProfile.lastGoalReachedDate != today) {
            updateStreak(today)
        }
    }

    private fun updateStreak(today: String) {
        auth.currentUser?.uid?.let { userId ->
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val lastReached = try { sdf.parse(userProfile.lastGoalReachedDate) } catch (e: Exception) { null }
            val todayDate = sdf.parse(today)

            var newCurrentStreak = 1
            if (lastReached != null && todayDate != null) {
                val diff = todayDate.time - lastReached.time
                val daysDiff = TimeUnit.MILLISECONDS.toDays(diff)
                if (daysDiff == 1L) {
                    newCurrentStreak = userProfile.currentStreak + 1
                }
            }

            val newBestStreak = maxOf(newCurrentStreak, userProfile.bestStreak)
            userProfile = userProfile.copy(
                currentStreak = newCurrentStreak,
                bestStreak = newBestStreak,
                lastGoalReachedDate = today
            )
            RealtimeDatabaseManager.updateStreak(userId, newCurrentStreak, newBestStreak)
            RealtimeDatabaseManager.saveUserProfile(userId, userProfile)
        }
    }

    private fun saveHistory() {
        val serialized = history.joinToString(";") { "${it.date}|${it.time}|${it.amount}" }
        prefs.edit().putString("history_data", serialized).apply()
    }

    private fun loadHistory() {
        val data = prefs.getString("history_data", "") ?: ""
        if (data.isNotEmpty()) {
            val items = data.split(";")
            history.clear()
            items.forEach { item ->
                val parts = item.split("|")
                if (parts.size == 3) {
                    history.add(WaterRecord(date = parts[0], time = parts[1], amount = parts[2].toInt()))
                }
            }
        }
    }

    fun saveGoalToFirestore() {
        auth.currentUser?.uid?.let { userId ->
            RealtimeDatabaseManager.saveWaterGoal(userId, waterGoal)
        }
    }

    fun getTodayTotal(): Int {
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        return history.filter { it.date == today }.sumOf { it.amount }
    }

    fun getWeeklyTotal(): Int {
        val calendar = Calendar.getInstance()
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)
        return history.filter {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val recordDate = try { sdf.parse(it.date) } catch (e: Exception) { null }
            if (recordDate == null) false else {
                val recordCalendar = Calendar.getInstance()
                recordCalendar.time = recordDate
                recordCalendar.get(Calendar.WEEK_OF_YEAR) == currentWeek && 
                recordCalendar.get(Calendar.YEAR) == currentYear
            }
        }.sumOf { it.amount }
    }

    fun getMonthlyTotal(): Int {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        return history.filter {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val recordDate = try { sdf.parse(it.date) } catch (e: Exception) { null }
            if (recordDate == null) false else {
                val recordCalendar = Calendar.getInstance()
                recordCalendar.time = recordDate
                recordCalendar.get(Calendar.MONTH) == currentMonth && 
                recordCalendar.get(Calendar.YEAR) == currentYear
            }
        }.sumOf { it.amount }
    }

    fun getAverageWeekly(): Int = getWeeklyTotal() / 7
    fun getAverageMonthly(): Int = getMonthlyTotal() / 30

    fun getLast7DaysPercentages(): List<Int> {
        val last7Days = mutableListOf<Int>()
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val grouped = history.groupBy { it.date }
        
        for (i in 0 until 7) {
            val dateStr = sdf.format(calendar.time)
            val total = grouped[dateStr]?.sumOf { it.amount } ?: 0
            last7Days.add(((total.toFloat() / waterGoal) * 100).toInt().coerceAtMost(100))
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        return last7Days.reversed()
    }

    fun getLast30DaysPercentages(): List<Int> {
        val last30Days = mutableListOf<Int>()
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val grouped = history.groupBy { it.date }
        
        for (i in 0 until 30) {
            val dateStr = sdf.format(calendar.time)
            val total = grouped[dateStr]?.sumOf { it.amount } ?: 0
            last30Days.add(((total.toFloat() / waterGoal) * 100).toInt().coerceAtMost(100))
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        return last30Days.reversed()
    }

    private var _defaultDrinkAmount by mutableIntStateOf(prefs.getInt("default_drink_amount", 250))
    val defaultDrinkAmount: Int get() = _defaultDrinkAmount

    fun setDefaultDrinkAmount(amount: Int) {
        _defaultDrinkAmount = amount
        prefs.edit().putInt("default_drink_amount", amount).apply()
    }
}