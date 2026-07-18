package com.vpnch.cardioapp.core.model.common

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtils {

    // ============================
    // 1. Formatters and constants
    // ============================

    private const val DATE_KEY_PATTERN = "yyyy-MM-dd"
    private const val DAY_MONTH_PATTERN = "d MMMM"
    private const val TIME_PATTERN = "HH:mm"

    private const val DAY_PATTERN = "d"
    private const val MONTH_SHORT_PATTERN = "MMM"

    private val dateKeyFormat = SimpleDateFormat(DATE_KEY_PATTERN, Locale.US)
    private val dayMonthFormat = SimpleDateFormat(DAY_MONTH_PATTERN, Locale("ru", "RU"))
    private val dayFormat = SimpleDateFormat(DAY_PATTERN, Locale("ru", "RU"))
    private val monthShortFormat = SimpleDateFormat(MONTH_SHORT_PATTERN, Locale("ru", "RU"))
    private val timeFormat = SimpleDateFormat(TIME_PATTERN, Locale("ru", "RU"))

    // ============================
    // 2. Transformations
    // ============================

    fun currentDateKey(): String = dateKeyFormat.format(Date())

    fun timestampToDateKey(timestamp: Long): String = dateKeyFormat.format(Date(timestamp))

    // ============================
    // 3. Formatting for the UI
    // ============================

    /** Returns the date in the "15 апреля" format". */
    fun formatDayMonthLabel(dateKey: String): String {
        return dayMonthFormat.format(parseDateKey(dateKey))
    }

    /** Returns the day number, e.g. "15". */
    fun formatDayNumber(dateKey: String): String {
        return dayFormat.format(parseDateKey(dateKey))
    }

    /** Returns the abbreviated month name, e.g. "апр". */
    fun formatMonthShort(dateKey: String): String {
        return monthShortFormat.format(parseDateKey(dateKey)).trimEnd('.')
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

    fun formatTime(timestamp: Long): String = timeFormat.format(Date(timestamp))

    /**
     * Counts consecutive days with records, ending at today or yesterday.
     * If today has no records yet, starts counting from yesterday so the
     * streak doesn't reset to 0 mid-day.
     */
    fun calculateStreak(dateKeys: Set<String>): Int {
        if (dateKeys.isEmpty()) return 0
        val cal = Calendar.getInstance()
        val todayKey = dateKeyFormat.format(cal.time)
        // If no record today, start counting from yesterday
        if (!dateKeys.contains(todayKey)) {
            cal.add(Calendar.DAY_OF_YEAR, -1)
        }
        var streak = 0
        while (true) {
            val key = dateKeyFormat.format(cal.time)
            if (!dateKeys.contains(key)) break
            streak++
            cal.add(Calendar.DAY_OF_YEAR, -1)
        }
        return streak
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