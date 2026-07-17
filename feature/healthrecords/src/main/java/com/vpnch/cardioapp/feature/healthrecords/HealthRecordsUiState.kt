package com.vpnch.cardioapp.feature.healthrecords

import com.vpnch.cardioapp.core.model.common.DateUtils.currentDateKey
import com.vpnch.cardioapp.core.model.common.DateUtils.formatDayMonthLabel
import com.vpnch.cardioapp.core.model.health.HealthRecord

data class HealthRecordsUiState(
    val dateKey: String = currentDateKey(),
    val title: String = "Записи",
    val dateLabel: String = formatDayMonthLabel(currentDateKey()),
    val records: List<HealthRecordListItem> = emptyList(),
    val isLoading: Boolean = true,
)

data class HealthRecordListItem(
    val record: HealthRecord,
    val hasOutOfNorm: Boolean,
    val hasCritical: Boolean = false,
)

sealed interface HealthRecordsEvent {
    data object DeleteFailed : HealthRecordsEvent
}
