package com.vpnch.cardioapp.core.model

fun BloodPressureLimits.expectedRangeLabel(): String {
    return "Ожидается от $normalSystolicMin/$normalDiastolicMin до $normalSystolicMax/$normalDiastolicMax"
}

fun SingleMetricLimits.expectedRangeLabel(): String {
    return "Ожидается от $normalMin до $normalMax"
}

/** Одно ориентировочное значение из нормального коридора — для подсказки в поле ввода. */
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
