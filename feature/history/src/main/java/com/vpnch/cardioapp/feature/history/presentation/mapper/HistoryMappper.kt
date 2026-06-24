package com.vpnch.cardioapp.feature.history.presentation.mapper

import com.vpnch.cardioapp.core.model.DateUtils.formatDayMonthLabel
import com.vpnch.cardioapp.core.model.DateUtils.formatRecordsCount
import com.vpnch.cardioapp.core.model.DateUtils.timestampToDateKey
import com.vpnch.cardioapp.core.model.HealthRecord
import com.vpnch.cardioapp.core.model.TakenVitaminOnDate
import com.vpnch.cardioapp.feature.history.presentation.HealthRecordHistorySection
import com.vpnch.cardioapp.feature.history.presentation.VitaminHistoryItem
import com.vpnch.cardioapp.feature.history.presentation.VitaminHistorySection

class HistoryMapper {
    fun mapRecordsToSections(records: List<HealthRecord>): List<HealthRecordHistorySection> {
        return records.groupBy { timestampToDateKey(it.createdAt) }
            .map { (dateKey, items) ->
                HealthRecordHistorySection(
                    dateKey = dateKey,
                    label = formatDayMonthLabel(dateKey),
                    recordsCountLabel = formatRecordsCount(items.size),
                )
            }
            .sortedByDescending { it.dateKey }
    }

    fun mapVitaminsToSections(takenVitamins: List<TakenVitaminOnDate>): List<VitaminHistorySection> {
        return takenVitamins.groupBy { it.date }
            .map { (dateKey, items) ->
                VitaminHistorySection(
                    dateKey = dateKey,
                    label = formatDayMonthLabel(dateKey),
                    vitamins = items.map { VitaminHistoryItem(it.vitaminId, it.vitaminName) },
                )
            }
            .sortedByDescending { it.dateKey }
    }
}