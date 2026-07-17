package com.vpnch.cardioapp.core.model.health.limits

fun BloodPressureLimits.normalSystolicPlaceholder(): String {
    return normalMidpoint(normalSystolicMin, normalSystolicMax).toString()
}

fun BloodPressureLimits.normalDiastolicPlaceholder(): String {
    return normalMidpoint(normalDiastolicMin, normalDiastolicMax).toString()
}

fun SingleMetricLimits.normalPlaceholder(): String {
    return normalMidpoint(normalMin, normalMax).toString()
}

private fun normalMidpoint(min: Int, max: Int): Int = (min + max) / 2
