package com.example.health_remain_app.data.firebase

import android.util.Log
import com.example.health_remain_app.data.model.UserProfile
import com.example.health_remain_app.data.model.WaterRecord
import com.google.firebase.database.FirebaseDatabase

object RealtimeDatabaseManager {

    private const val TAG = "RealtimeDatabase"
    private val db =
        FirebaseDatabase.getInstance("https://water-remain-app-default-rtdb.asia-southeast1.firebasedatabase.app").reference

    fun saveWaterGoal(userId: String, goal: Int) {
        db.child("users").child(userId).child("waterGoal")
            .setValue(goal)
            .addOnFailureListener { Log.e(TAG, "Error saving water goal", it) }
    }

    fun saveWaterRecord(userId: String, record: WaterRecord) {
        db.child("users").child(userId).child("history").push()
            .setValue(record)
            .addOnFailureListener { Log.e(TAG, "Error saving water record", it) }
    }

    fun saveUserProfile(userId: String, profile: UserProfile) {
        db.child("users").child(userId).child("profile")
            .setValue(profile)
            .addOnSuccessListener { Log.d(TAG, "Profile saved successfully!") }
            .addOnFailureListener { Log.e(TAG, "Error saving user profile", it) }
    }

    fun getUserProfile(userId: String, onResult: (UserProfile?) -> Unit) {
        db.child("users").child(userId).child("profile").get()
            .addOnSuccessListener {
                onResult(it.getValue(UserProfile::class.java))
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting user profile", it)
                onResult(null)
            }
    }
    
    fun updateStreak(userId: String, current: Int, best: Int) {
        val updates = mapOf(
            "profile/currentStreak" to current,
            "profile/bestStreak" to best
        )
        db.child("users").child(userId).updateChildren(updates)
            .addOnFailureListener { Log.e(TAG, "Error updating streak", it) }
    }

    fun addAchievement(userId: String, achievement: String) {
        db.child("users").child(userId).child("profile/achievements").get().addOnSuccessListener {
            val list = it.getValue(object : com.google.firebase.database.GenericTypeIndicator<List<String>>() {}) ?: emptyList()
            if (!list.contains(achievement)) {
                val newList = list + achievement
                db.child("users").child(userId).child("profile/achievements").setValue(newList)
            }
        }
    }
}
