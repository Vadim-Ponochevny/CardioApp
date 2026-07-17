package com.vpnch.cardioapp.core.model.vitamins

data class VitaminIntake(
    val id: String,
    val intakeDayId: String,
    val vitaminId: String,
    val isTaken: Boolean,
    val takenAt: Long?,
    val updatedAt: Long,
)

