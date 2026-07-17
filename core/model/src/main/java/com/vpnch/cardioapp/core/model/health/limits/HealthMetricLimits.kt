package com.vpnch.cardioapp.core.model.health.limits

import com.vpnch.cardioapp.core.model.patient.AgeGroup

sealed interface HealthMetricLimits {
    val id: String
    val ageGroup: AgeGroup
}

