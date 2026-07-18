package com.vpnch.cardioapp.feature.history.presentation.mapper

import com.vpnch.cardioapp.core.domain.usecase.EvaluateHealthRecordLimitsUseCase
import com.vpnch.cardioapp.core.model.common.DateUtils.calculateStreak
import com.vpnch.cardioapp.core.model.common.DateUtils.formatDayNumber
import com.vpnch.cardioapp.core.model.common.DateUtils.formatMonthShort
import com.vpnch.cardioapp.core.model.common.DateUtils.formatRecordsCount
import com.vpnch.cardioapp.core.model.common.DateUtils.timestampToDateKey
import com.vpnch.cardioapp.core.model.health.HealthRecord
import com.vpnch.cardioapp.core.model.health.limits.MetricLimitsBundle
import com.vpnch.cardioapp.core.model.vitamins.TakenVitaminOnDate
import com.vpnch.cardioapp.feature.history.presentation.HealthRecordHistorySection
import com.vpnch.cardioapp.feature.history.presentation.RecordDotStatus
import com.vpnch.cardioapp.feature.history.presentation.VitaminHistoryItem
import com.vpnch.cardioapp.feature.history.presentation.VitaminHistorySection
import javax.inject.Inject

class HistoryMapper @Inject constructor(
    private val evaluateLimits: EvaluateHealthRecordLimitsUseCase,
) {
    fun calculateRecordStreak(records: List<HealthRecord>): Int {
        val dateKeys = records.map { timestampToDateKey(it.createdAt) }.toSet()
        return calculateStreak(dateKeys)
    }

    fun mapRecordsToSections(
        records: List<HealthRecord>,
        limitsBundle: MetricLimitsBundle?,
    ): List<HealthRecordHistorySection> {
        return records.groupBy { timestampToDateKey(it.createdAt) }
            .map { (dateKey, items) ->
                HealthRecordHistorySection(
                    dateKey = dateKey,
                    dayLabel = formatDayNumber(dateKey),
                    monthLabel = formatMonthShort(dateKey),
                    recordsCountLabel = formatRecordsCount(items.size),
                    recordDots = items.map { record -> record.toDotStatus(limitsBundle) },
                )
            }
            .sortedByDescending { it.dateKey }
    }

    fun mapVitaminsToSections(takenVitamins: List<TakenVitaminOnDate>): List<VitaminHistorySection> {
        return takenVitamins.groupBy { it.date }
            .map { (dateKey, items) ->
                VitaminHistorySection(
                    dateKey = dateKey,
                    label = "${formatDayNumber(dateKey)} ${formatMonthShort(dateKey)}",
                    vitamins = items.map { VitaminHistoryItem(it.vitaminId, it.vitaminName) },
                )
            }
            .sortedByDescending { it.dateKey }
    }

    private fun HealthRecord.toDotStatus(limitsBundle: MetricLimitsBundle?): RecordDotStatus {
        if (limitsBundle == null) return RecordDotStatus.Ok
        val evaluation = evaluateLimits.evaluate(
            record = this,
            singleLimits = limitsBundle.singleLimits,
            bloodPressureLimits = limitsBundle.bloodPressureLimits,
        )
        return when {
            evaluation.hasDoctorSoon -> RecordDotStatus.Critical
            evaluation.hasOutOfNorm -> RecordDotStatus.Warning
            else -> RecordDotStatus.Ok
        }
    }
}
