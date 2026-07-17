package com.vpnch.cardioapp.core.model.health.limits

import com.vpnch.cardioapp.core.model.health.metrics.MetricType

data class MetricLimitsBundle(
    val singleLimits: Map<MetricType, SingleMetricLimits>,
    val bloodPressureLimits: BloodPressureLimits?,
)
