package com.vpnch.cardioapp.feature.healthrecords.detail.model

import com.vpnch.cardioapp.feature.healthrecords.model.HealthMetricKind

data class MetricItem(
    val kind: HealthMetricKind,
    val title: String,
    val value: String,
    val isOutOfNorm: Boolean,
    val isCritical: Boolean = false,
)