package com.vpnch.cardioapp.feature.healthrecords.base

import androidx.lifecycle.ViewModel
import com.vpnch.cardioapp.core.domain.repository.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.core.domain.usecase.CheckHealthMetricLimitsUseCase
import com.vpnch.cardioapp.core.model.health.InrConverter
import com.vpnch.cardioapp.core.model.health.limits.BloodPressureLimits
import com.vpnch.cardioapp.core.model.health.limits.SingleMetricLimits
import com.vpnch.cardioapp.core.model.health.metrics.MetricStatus
import com.vpnch.cardioapp.core.model.health.metrics.MetricType
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.feature.healthrecords.utils.toPositiveInt
import java.util.concurrent.atomic.AtomicBoolean

abstract class HealthRecordBaseViewModel(
    protected val patientRepository: PatientRepository,
    protected val healthRecordRepository: HealthRecordRepository,
    protected val checkLimits: CheckHealthMetricLimitsUseCase,
) : ViewModel() {

    protected var singleLimits: Map<MetricType, SingleMetricLimits> = emptyMap()
    protected var bloodPressureLimits: BloodPressureLimits? = null
    protected val isPersisting = AtomicBoolean(false)

    // Throws on failure — caller wraps in runCatching and handles UiState accordingly.
    protected suspend fun loadLimits(ageGroup: AgeGroup) {
        singleLimits = healthRecordRepository
            .getSingleMetricLimits(ageGroup)
            .associateBy { it.metricType }
        bloodPressureLimits = healthRecordRepository.getBloodPressureLimits(ageGroup)
    }

    protected fun warningForBloodPressure(systolicInput: String, diastolicInput: String): MetricStatus? {
        val systolic = systolicInput.toPositiveInt() ?: return null
        val diastolic = diastolicInput.toPositiveInt() ?: return null
        return checkLimits.checkBloodPressure(systolic, diastolic, bloodPressureLimits).toWarning()
    }

    protected fun warningForSingleMetric(input: String, metricType: MetricType): MetricStatus? {
        val value = input.toPositiveInt() ?: return null
        val limits = singleLimits[metricType] ?: return null
        return checkLimits.checkSingleValue(value, limits).toWarning()
    }

    protected fun warningForInr(input: String): MetricStatus? {
        val value = InrConverter.toStoredInt(input) ?: return null
        val limits = singleLimits[MetricType.INR]?.takeIf { it.normalMax > 0 } ?: return null
        return checkLimits.checkSingleValue(value, limits).toWarning()
    }

    private fun MetricStatus.toWarning(): MetricStatus? = takeIf {
        it != MetricStatus.Normal && it != MetricStatus.Unknown
    }
}