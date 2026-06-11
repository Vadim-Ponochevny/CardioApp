package com.vpnch.cardioapp.core.domain

import com.vpnch.cardioapp.core.model.BloodPressureLimits
import com.vpnch.cardioapp.core.model.HealthRecord
import com.vpnch.cardioapp.core.model.HealthRecordEvaluation
import com.vpnch.cardioapp.core.model.MetricType
import com.vpnch.cardioapp.core.model.SingleMetricLimits
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
                systolic, // Теперь смарт-каст работает идеально
                diastolic,
                bloodPressureLimits,
            )
        } else {
            null
        }

        return HealthRecordEvaluation(
            bloodPressure = bloodPressure,
            respiratoryRate = record.respiratoryRate?.let {
                checkLimits.checkSingleValue(it, singleLimits[MetricType.RespiratoryRate])
            },
            heartRate = record.heartRate?.let {
                checkLimits.checkSingleValue(it, singleLimits[MetricType.HeartRate])
            },
            oxygenSaturation = record.oxygenSaturation?.let {
                checkLimits.checkSingleValue(it, singleLimits[MetricType.OxygenSaturation])
            },
        )
    }
}

data class MetricLimitsBundle(
    val singleLimits: Map<MetricType, SingleMetricLimits>,
    val bloodPressureLimits: BloodPressureLimits?,
)
