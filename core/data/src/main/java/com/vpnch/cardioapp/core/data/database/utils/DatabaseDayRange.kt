package com.vpnch.cardioapp.core.data.database.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

internal data class DatabaseDayRange(
    val startOfDay: Long,
    val startOfNextDay: Long,
)

internal fun getDatabaseDayRangeFromDateString(date: String): DatabaseDayRange {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val start = requireNotNull(formatter.parse(date)).time
    val calendar = Calendar.getInstance().apply {
        timeInMillis = start
        add(Calendar.DAY_OF_YEAR, 1)
    }

    return DatabaseDayRange(
        startOfDay = start,
        startOfNextDay = calendar.timeInMillis,
    )
}
