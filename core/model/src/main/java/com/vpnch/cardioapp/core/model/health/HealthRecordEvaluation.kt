package com.vpnch.cardioapp.core.model.health

import com.vpnch.cardioapp.core.model.health.metrics.MetricStatus

data class HealthRecordEvaluation(
    val bloodPressure: MetricStatus?,
    val respiratoryRate: MetricStatus?,
    val heartRate: MetricStatus?,
    val oxygenSaturation: MetricStatus?,
    val inr: MetricStatus? = null,
) {
    val hasOutOfNorm: Boolean
        get() = listOf(bloodPressure, respiratoryRate, heartRate, oxygenSaturation, inr)
            .any { it.isOutOfNorm() }

    val hasDoctorSoon: Boolean
        get() = listOf(bloodPressure, respiratoryRate, heartRate, oxygenSaturation, inr)
            .any { it == MetricStatus.DoctorSoon }
}

fun MetricStatus?.isOutOfNorm(): Boolean {
    return this == MetricStatus.Attention || this == MetricStatus.DoctorSoon
}
