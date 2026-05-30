package com.vpnch.cardioapp.core.model

sealed interface HealthMetricLimits {
    val id: String
    val ageGroup: AgeGroup
}

data class SingleMetricLimits(
    override val id: String,
    override val ageGroup: AgeGroup,
    val metricType: MetricType,
    val normalMin: Int,
    val normalMax: Int,
    val attentionMin: Int?,
    val attentionMax: Int?,
    val doctorSoonMin: Int?,
    val doctorSoonMax: Int?,
) : HealthMetricLimits

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
