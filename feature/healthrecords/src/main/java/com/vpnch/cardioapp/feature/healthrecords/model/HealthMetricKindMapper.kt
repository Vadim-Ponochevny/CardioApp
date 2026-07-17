package com.vpnch.cardioapp.feature.healthrecords.model

import com.vpnch.cardioapp.core.model.health.metrics.MetricType

internal fun HealthMetricKind.toMetricType(): MetricType = when (this) {
    HealthMetricKind.BloodPressure -> error("Blood pressure uses two fields")
    HealthMetricKind.RespiratoryRate -> MetricType.RespiratoryRate
    HealthMetricKind.HeartRate -> MetricType.HeartRate
    HealthMetricKind.OxygenSaturation -> MetricType.OxygenSaturation
    HealthMetricKind.INR -> MetricType.INR
}
