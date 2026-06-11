package com.vpnch.cardioapp.feature.healthrecords.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.EvaluateHealthRecordLimitsUseCase
import com.vpnch.cardioapp.core.domain.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.MetricLimitsBundle
import com.vpnch.cardioapp.core.domain.PatientRepository
import com.vpnch.cardioapp.core.model.HealthRecord
import com.vpnch.cardioapp.core.model.formatAsHealthMetric
import com.vpnch.cardioapp.core.model.formatBloodPressure
import com.vpnch.cardioapp.core.model.isOutOfNorm
import com.vpnch.cardioapp.feature.healthrecords.HealthMetricKind
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class HealthRecordDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    patientRepository: PatientRepository,
    healthRecordRepository: HealthRecordRepository,
    private val evaluateLimits: EvaluateHealthRecordLimitsUseCase,
) : ViewModel() {

    private val recordId: String = checkNotNull(savedStateHandle[RECORD_ID_ARG])
    private val limitsBundle = MutableStateFlow<MetricLimitsBundle?>(null)

    init {
        viewModelScope.launch {
            val patient = patientRepository.getCurrentPatient() ?: return@launch
            limitsBundle.value = MetricLimitsBundle(
                singleLimits = healthRecordRepository
                    .getSingleMetricLimits(patient.ageGroup)
                    .associateBy { it.metricType },
                bloodPressureLimits = healthRecordRepository.getBloodPressureLimits(patient.ageGroup),
            )
        }
    }

    val uiState = combine(
        healthRecordRepository.observeRecord(recordId),
        limitsBundle,
    ) { record, limits ->
        if (record == null) {
            HealthRecordDetailUiState(isLoading = false, loadError = true)
        } else {
            HealthRecordDetailUiState(
                isLoading = false,
                timeLabel = formatRecordTime(record.createdAt),
                metrics = record.toMetricItems(limits, evaluateLimits),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HealthRecordDetailUiState(),
    )

    companion object {
        const val RECORD_ID_ARG = "recordId"
    }
}

data class HealthRecordDetailUiState(
    val isLoading: Boolean = true,
    val loadError: Boolean = false,
    val timeLabel: String = "",
    val metrics: List<MetricItem> = emptyList(),
)

data class MetricItem(
    val kind: HealthMetricKind,
    val title: String,
    val value: String,
    val isOutOfNorm: Boolean,
)

private fun HealthRecord.toMetricItems(
    limits: MetricLimitsBundle?,
    evaluateLimits: EvaluateHealthRecordLimitsUseCase,
): List<MetricItem> {
    val evaluation = limits?.let {
        evaluateLimits.evaluate(this, it.singleLimits, it.bloodPressureLimits)
    }

    return listOf(
        MetricItem(
            kind = HealthMetricKind.BloodPressure,
            title = "Давление",
            value = formatBloodPressure(systolicPressure, diastolicPressure),
            isOutOfNorm = evaluation?.bloodPressure.isOutOfNorm(),
        ),
        MetricItem(
            kind = HealthMetricKind.RespiratoryRate,
            title = "Дыхание",
            value = respiratoryRate.formatAsHealthMetric(),
            isOutOfNorm = evaluation?.respiratoryRate.isOutOfNorm(),
        ),
        MetricItem(
            kind = HealthMetricKind.HeartRate,
            title = "Пульс",
            value = heartRate.formatAsHealthMetric(),
            isOutOfNorm = evaluation?.heartRate.isOutOfNorm(),
        ),
        MetricItem(
            kind = HealthMetricKind.OxygenSaturation,
            title = "Кислород",
            value = oxygenSaturation.formatAsHealthMetric(),
            isOutOfNorm = evaluation?.oxygenSaturation.isOutOfNorm(),
        ),
    )
}

private fun formatRecordTime(timestamp: Long): String {
    return SimpleDateFormat("HH:mm", Locale("ru", "RU")).format(Date(timestamp))
}
