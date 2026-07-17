package com.vpnch.cardioapp.core.database.entity.health.limits

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "blood_pressure_limits",
    indices = [Index(value = ["ageGroup"], unique = true)],
)
data class BloodPressureLimitEntity(
    @PrimaryKey val id: String,
    val ageGroup: String,
    val normalSystolicMin: Int,
    val normalSystolicMax: Int,
    val normalDiastolicMin: Int,
    val normalDiastolicMax: Int,
    val doctorSoonSystolicLow: Int,
    val doctorSoonSystolicHigh: Int,
    val doctorSoonDiastolicLow: Int,
    val doctorSoonDiastolicHigh: Int,
)
