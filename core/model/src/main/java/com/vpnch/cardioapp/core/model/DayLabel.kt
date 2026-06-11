package com.vpnch.cardioapp.core.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

private const val DATE_KEY_PATTERN = "yyyy-MM-dd"
private val dateKeyFormat = SimpleDateFormat(DATE_KEY_PATTERN, Locale.US)
private val dayMonthFormat = SimpleDateFormat("d MMMM", Locale("ru", "RU"))

fun currentDateKey(): String = dateKeyFormat.format(Date())

fun timestampToDateKey(timestamp: Long): String = dateKeyFormat.format(Date(timestamp))

fun Long.toDateKey(): String = timestampToDateKey(this)

/** «Сегодня», «Вчера», «Позавчера» или «15 апреля». */
fun formatDaySectionLabel(
    dateKey: String,
    referenceDateKey: String = currentDateKey(),
): String {
    return when (daysBetween(dateKey, referenceDateKey)) {
        0 -> "Сегодня"
        1 -> "Вчера"
        2 -> "Позавчера"
        else -> formatDayMonthLabel(dateKey)
    }
}

fun formatHealthRecordsTitle(dateKey: String): String {
    return "Записи (${formatDaySectionLabel(dateKey)})"
}

fun formatRecordsCount(count: Int): String {
    val word = when {
        count % 100 in 11..14 -> "записей"
        count % 10 == 1 -> "запись"
        count % 10 in 2..4 -> "записи"
        else -> "записей"
    }
    return "$count $word"
}

/** Например: «15 апреля», «3 марта». */
fun formatDayMonthLabel(dateKey: String): String {
    return dayMonthFormat.format(parseDateKey(dateKey))
}

private fun parseDateKey(dateKey: String): Date {
    return requireNotNull(dateKeyFormat.parse(dateKey)) { "Invalid date key: $dateKey" }
}

private fun daysBetween(fromKey: String, toKey: String): Int {
    val diffMs = parseDateKey(toKey).time - parseDateKey(fromKey).time
    return TimeUnit.MILLISECONDS.toDays(diffMs).toInt()
}
