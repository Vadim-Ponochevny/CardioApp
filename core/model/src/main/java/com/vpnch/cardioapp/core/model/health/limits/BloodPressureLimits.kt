package com.vpnch.cardioapp.core.model.health.limits

import com.vpnch.cardioapp.core.model.patient.AgeGroup

data class BloodPressureLimits(
    override val id: String,
    override val ageGroup: AgeGroup,
    val normalSystolicMin: Int,
    val normalSystolicMax: Int,
    val normalDiastolicMin: Int,
    val normalDiastolicMax: Int,
    val doctorSoonSystolicLow: Int,
    val doctorSoonSystolicHigh: Int,
    val doctorSoonDiastolicLow: Int,
    val doctorSoonDiastolicHigh: Int,
) : HealthMetricLimits