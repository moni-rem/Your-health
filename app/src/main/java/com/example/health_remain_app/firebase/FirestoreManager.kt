package com.example.health_remain_app.firebase

import com.example.health_remain_app.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreManager {
    private val db = FirebaseFirestore.getInstance()

    fun saveUserProfile(userId: String, profile: UserProfile) {
        db.collection("users").document(userId).set(profile)
    }

    fun getUserProfile(userId: String, onResult: (UserProfile?) -> Unit) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                onResult(document.toObject(UserProfile::class.java))
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun updateStreak(userId: String, current: Int, best: Int, lastReachedDate: String) {
        db.collection("users").document(userId).update(
            mapOf(
                "currentStreak" to current,
                "bestStreak" to best,
                "lastGoalReachedDate" to lastReachedDate
            )
        )
    }

    fun updateAchievements(userId: String, achievements: List<String>) {
        db.collection("users").document(userId).update("achievements", achievements)
    }
}
