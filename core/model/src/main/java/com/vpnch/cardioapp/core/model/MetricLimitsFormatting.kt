package com.vpnch.cardioapp.core.model

fun BloodPressureLimits.expectedRangeLabel(): String {
    return "Ожидается от $normalSystolicMin/$normalDiastolicMin до $normalSystolicMax/$normalDiastolicMax"
}

fun SingleMetricLimits.expectedRangeLabel(): String {
    return "Ожидается от $normalMin до $normalMax"
}
