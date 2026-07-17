package com.vpnch.cardioapp.core.model.health

const val MISSING_METRIC_LABEL = "—"

fun Int?.formatAsHealthMetric(): String = this?.toString() ?: MISSING_METRIC_LABEL

fun formatBloodPressure(systolic: Int?, diastolic: Int?): String {
    return "${systolic.formatAsHealthMetric()}/${diastolic.formatAsHealthMetric()}"
}

