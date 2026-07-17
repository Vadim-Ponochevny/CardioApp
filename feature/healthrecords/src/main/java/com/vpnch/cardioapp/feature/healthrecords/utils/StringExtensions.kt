package com.vpnch.cardioapp.feature.healthrecords.utils

internal fun String.filterDigits(): String = filter { it.isDigit() }.take(3)

internal fun String.filterDecimal(): String {
    val s = filter { it.isDigit() || it == '.' }
    val dotIdx = s.indexOf('.')
    return if (dotIdx >= 0) s.substring(0, dotIdx + 1) + s.substring(dotIdx + 1).filter { it.isDigit() } else s
}

internal fun String.toPositiveInt(): Int? = toIntOrNull()?.takeIf { it > 0 }
