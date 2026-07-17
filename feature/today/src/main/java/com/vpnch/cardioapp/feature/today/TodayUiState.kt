package com.vpnch.cardioapp.feature.today

import androidx.annotation.DrawableRes
import com.vpnch.cardioapp.core.model.survey.SurveyLink
import com.vpnch.cardioapp.core.model.vitamins.VitaminIntakeSummary

data class TodayUiState(
    val isLoading: Boolean = true,
    val latestRecord: LatestHealthRecordSummary? = null,
    val vitaminIntakes: List<VitaminIntakeSummary> = emptyList(),
    val surveyLink: SurveyLink? = null,
)

data class LatestHealthRecordSummary(
    val recordId: String,
    val timeLabel: String,
    val metrics: List<TodayMetricItem>,
    val hasOutOfNorm: Boolean,
    val hasCritical: Boolean,
)

data class TodayMetricItem(
    val title: String,
    val value: String,
    @DrawableRes val iconRes: Int,
    val isOutOfNorm: Boolean,
)
