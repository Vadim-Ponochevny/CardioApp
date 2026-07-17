package com.vpnch.cardioapp.core.model.health

fun Int.formatAsInr(): String {
    val v = this / 10.0
    return if (v == v.toLong().toDouble()) v.toLong().toString() else v.toString()
}

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
