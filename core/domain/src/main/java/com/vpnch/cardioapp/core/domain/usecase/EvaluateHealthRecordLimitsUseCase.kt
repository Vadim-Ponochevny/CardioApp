package com.vpnch.cardioapp.core.domain.usecase

import com.vpnch.cardioapp.core.model.health.HealthRecord
import com.vpnch.cardioapp.core.model.health.HealthRecordEvaluation
import com.vpnch.cardioapp.core.model.health.limits.BloodPressureLimits
import com.vpnch.cardioapp.core.model.health.limits.SingleMetricLimits
import com.vpnch.cardioapp.core.model.health.metrics.MetricType
import javax.inject.Inject

class EvaluateHealthRecordLimitsUseCase @Inject constructor(
    private val checkLimits: CheckHealthMetricLimitsUseCase,
) {
    fun evaluate(
        record: HealthRecord,
        singleLimits: Map<MetricType, SingleMetricLimits>,
        bloodPressureLimits: BloodPressureLimits?,
    ): HealthRecordEvaluation {
        val systolic = record.systolicPressure
        val diastolic = record.diastolicPressure

        val bloodPressure = if (systolic != null && diastolic != null) {
            checkLimits.checkBloodPressure(
                systolic,
                diastolic,
                bloodPressureLimits,
            )
        } else {
            null
        }

        return HealthRecordEvaluation(
            bloodPressure = bloodPressure,
            respiratoryRate = null,
            heartRate = record.heartRate?.let {
                checkLimits.checkSingleValue(it, singleLimits[MetricType.HeartRate])
            },
            oxygenSaturation = record.oxygenSaturation?.let {
                checkLimits.checkSingleValue(it, singleLimits[MetricType.OxygenSaturation])
            },
            inr = record.inr?.let { inrValue ->
                // Only evaluate if limits are configured (normalMax > 0 means limits were set)
                val inrLimits = singleLimits[MetricType.INR]?.takeIf { it.normalMax > 0 }
                inrLimits?.let { checkLimits.checkSingleValue(inrValue, it) }
            },
        )
    }
}

