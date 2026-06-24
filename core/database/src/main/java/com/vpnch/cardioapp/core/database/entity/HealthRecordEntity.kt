package com.vpnch.cardioapp.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "health_records",
    indices = [
        Index("patientId"),
        Index("createdAt"),
        Index(value = ["patientId", "createdAt"]),
    ],
)
data class HealthRecordEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val createdAt: Long,
    val updatedAt: Long,
    val systolicPressure: Int?,
    val diastolicPressure: Int?,
    val respiratoryRate: Int?,
    val heartRate: Int?,
    val oxygenSaturation: Int?,
    val inr: Int? = null,
)
