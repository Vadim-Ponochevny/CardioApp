package com.vpnch.cardioapp.feature.history.presentation

data class HistoryUiState(
    val isLoading: Boolean = true,
    val streakDays: Int = 0,
    val healthRecordSections: List<HealthRecordHistorySection> = emptyList(),
    val vitaminSections: List<VitaminHistorySection> = emptyList(),
)

enum class RecordDotStatus { Ok, Warning, Critical }

data class HealthRecordHistorySection(
    val dateKey: String,
    val dayLabel: String,
    val monthLabel: String,
    val recordsCountLabel: String,
    val recordDots: List<RecordDotStatus>,
)

data class VitaminHistorySection(
    val dateKey: String,
    val label: String,
    val vitamins: List<VitaminHistoryItem>,
)

data class VitaminHistoryItem(
    val id: String,
    val name: String,
)
