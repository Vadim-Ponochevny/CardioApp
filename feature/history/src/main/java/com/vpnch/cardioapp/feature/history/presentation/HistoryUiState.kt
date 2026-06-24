package com.vpnch.cardioapp.feature.history.presentation

data class HistoryUiState(
    val isLoading: Boolean = true,
    val healthRecordSections: List<HealthRecordHistorySection> = emptyList(),
    val vitaminSections: List<VitaminHistorySection> = emptyList(),
)

data class HealthRecordHistorySection(
    val dateKey: String,
    val label: String,
    val recordsCountLabel: String,
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