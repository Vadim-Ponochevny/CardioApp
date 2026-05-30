package com.vpnch.cardioapp.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vitamin_intake_days",
    indices = [Index(value = ["patientId", "date"], unique = true)],
)
data class VitaminIntakeDayEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val date: String,
    val createdAt: Long,
    val updatedAt: Long,
)
