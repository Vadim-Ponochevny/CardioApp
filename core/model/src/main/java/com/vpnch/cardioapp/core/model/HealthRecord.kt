package com.vpnch.cardioapp.core.model

data class HealthRecord(
    val id: String,
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
