package com.vpnch.cardioapp.core.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtils {

    // ============================
    // 1. Formatters and constants
    // ============================

    private const val DATE_KEY_PATTERN = "yyyy-MM-dd"
    private const val DAY_MONTH_PATTERN = "d MMMM"

    private val dateKeyFormat = SimpleDateFormat(DATE_KEY_PATTERN, Locale.US)
    private val dayMonthFormat = SimpleDateFormat(DAY_MONTH_PATTERN, Locale("ru", "RU"))

    // ============================
    // 2. Transformations
    // ============================

    fun currentDateKey(): String = dateKeyFormat.format(Date())

    fun timestampToDateKey(timestamp: Long): String = dateKeyFormat.format(Date(timestamp))

    fun Long.toDateKey(): String = timestampToDateKey(this)

    // ============================
    // 3. Formatting for the UI
    // ============================

    /** Returns the date in the "15 апреля" format". */
    fun formatDayMonthLabel(dateKey: String): String {
        return dayMonthFormat.format(parseDateKey(dateKey))
    }

    /** Форматирует заголовок для экрана списка записей. */
    fun formatHealthRecordsTitle(dateKey: String): String {
        return "Записи (${formatDayMonthLabel(dateKey)})"
    }

    /**
     * Склоняет слово "запись" в зависимости от числа.
     * Пример: 1 запись, 2 записи, 5 записей.
     */
    fun formatRecordsCount(count: Int): String {
        val word = when {
            count % 100 in 11..14 -> "записей"
            count % 10 == 1 -> "запись"
            count % 10 in 2..4 -> "записи"
            else -> "записей"
        }
        return "$count $word"
    }

    // ============================
    // 4. Вспомогательные (private)
    // ============================

    private fun parseDateKey(dateKey: String): Date {
        return requireNotNull(dateKeyFormat.parse(dateKey)) { "Invalid date key: $dateKey" }
    }

    private fun daysBetween(fromKey: String, toKey: String): Int {
        val diffMs = parseDateKey(toKey).time - parseDateKey(fromKey).time
        return TimeUnit.MILLISECONDS.toDays(diffMs).toInt()
    }
}