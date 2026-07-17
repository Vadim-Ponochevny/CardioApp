package com.vpnch.cardioapp.feature.healthrecords.edit

import com.vpnch.cardioapp.core.model.health.metrics.MetricStatus
import com.vpnch.cardioapp.feature.healthrecords.model.HealthMetricKind

data class HealthMetricEditUiState(
    val metricKind: HealthMetricKind,
    val title: String,
    val isBloodPressure: Boolean = false,
    val isInr: Boolean = false,
    val isLoaded: Boolean = false,
    val showWarningOnLoad: Boolean = false,
    val systolicInput: String = "",
    val diastolicInput: String = "",
    val singleInput: String = "",
    val singleLabel: String = "",
    val bloodPressureWarning: MetricStatus? = null,
    val singleWarning: MetricStatus? = null,
    val systolicPlaceholder: String = "",
    val diastolicPlaceholder: String = "",
    val singlePlaceholder: String = "",
    val isSaving: Boolean = false,
    val canSave: Boolean = false,
    val loadError: Boolean = false,
    val saveError: String? = null,
)
