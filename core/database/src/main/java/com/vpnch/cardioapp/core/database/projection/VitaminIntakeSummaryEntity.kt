package com.vpnch.cardioapp.core.database.projection

data class VitaminIntakeSummaryEntity(
    val id: String,
    val patientId: String,
    val name: String,
    val dose: String?,
    val intakeId: String?,
    val intakeDayId: String?,
    val intakeVitaminId: String?,
    val isTaken: Boolean?,
    val takenAt: Long?,
    val intakeUpdatedAt: Long?,
)
