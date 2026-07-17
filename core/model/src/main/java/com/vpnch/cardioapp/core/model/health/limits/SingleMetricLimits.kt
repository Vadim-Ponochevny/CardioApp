package com.vpnch.cardioapp.core.model.health.limits

import com.vpnch.cardioapp.core.model.health.metrics.MetricType
import com.vpnch.cardioapp.core.model.patient.AgeGroup

data class SingleMetricLimits(
    override val id: String,
    override val ageGroup: AgeGroup,
    val metricType: MetricType,
    val normalMin: Int,
    val normalMax: Int,
    val attentionMin: Int?,
    val attentionMax: Int?,
    val doctorSoonMin: Int?,
    val doctorSoonMax: Int?,
) : HealthMetricLimits

