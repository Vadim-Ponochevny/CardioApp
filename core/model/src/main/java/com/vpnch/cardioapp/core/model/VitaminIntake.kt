package com.vpnch.cardioapp.core.model

data class VitaminIntakeDay(
    val id: String,
    val patientId: String,
    val date: String,
    val createdAt: Long,
    val updatedAt: Long,
)

data class VitaminIntake(
    val id: String,
    val intakeDayId: String,
    val vitaminId: String,
    val isTaken: Boolean,
    val takenAt: Long?,
    val updatedAt: Long,
)

data class VitaminIntakeSummary(
    val vitamin: Vitamin,
    val intake: VitaminIntake?,
)
