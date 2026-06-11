package com.vpnch.cardioapp.core.model

data class HealthRecordEvaluation(
    val bloodPressure: MetricStatus?,
    val respiratoryRate: MetricStatus?,
    val heartRate: MetricStatus?,
    val oxygenSaturation: MetricStatus?,
) {
    val hasOutOfNorm: Boolean
        get() = listOf(bloodPressure, respiratoryRate, heartRate, oxygenSaturation)
            .any { it.isOutOfNorm() }

    val hasDoctorSoon: Boolean
        get() = listOf(bloodPressure, respiratoryRate, heartRate, oxygenSaturation)
            .any { it == MetricStatus.DoctorSoon }
}

fun MetricStatus?.isOutOfNorm(): Boolean {
    return this == MetricStatus.Attention || this == MetricStatus.DoctorSoon
}
