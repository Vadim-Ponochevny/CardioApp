package com.vpnch.cardioapp.core.model

const val MISSING_METRIC_LABEL = "—"

fun HealthRecord.isDraft(): Boolean {
    return systolicPressure == null ||
        diastolicPressure == null ||
        respiratoryRate == null ||
        heartRate == null ||
        oxygenSaturation == null
}

fun Int?.formatAsHealthMetric(): String = this?.toString() ?: MISSING_METRIC_LABEL

fun formatBloodPressure(systolic: Int?, diastolic: Int?): String {
    return "${systolic.formatAsHealthMetric()}/${diastolic.formatAsHealthMetric()}"
}

fun HealthRecord.firstIncompleteMetricRouteKey(): String? {
    if (systolicPressure == null || diastolicPressure == null) {
        return "blood_pressure"
    }
    if (respiratoryRate == null) return "respiratory"
    if (heartRate == null) return "heart_rate"
    if (oxygenSaturation == null) return "oxygen"
    return null
}
