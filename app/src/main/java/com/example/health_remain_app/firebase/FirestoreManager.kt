package com.example.health_remain_app.data.firebase

import com.example.health_remain_app.data.model.WaterRecord
import com.google.firebase.database.FirebaseDatabase

object RealtimeDatabaseManager {

    private val db =
        FirebaseDatabase.getInstance().reference

    fun saveWaterGoal(
        userId: String,
        goal: Int
    ) {

        db.child("users")
            .child(userId)
            .child("waterGoal")
            .setValue(goal)
    }

    fun saveWaterRecord(
        userId: String,
        record: WaterRecord
    ) {

        db.child("users")
            .child(userId)
            .child("history")
            .push()
            .setValue(record)
    }
}