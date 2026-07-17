package com.vpnch.cardioapp.feature.healthrecords.detail

import com.vpnch.cardioapp.feature.healthrecords.detail.model.MetricItem

data class HealthRecordDetailUiState(
    val isLoading: Boolean = true,
    val loadError: Boolean = false,
    val timeLabel: String = "",
    val metrics: List<MetricItem> = emptyList(),
)

