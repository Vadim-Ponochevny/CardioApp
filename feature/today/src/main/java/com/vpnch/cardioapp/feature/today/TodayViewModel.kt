package com.vpnch.cardioapp.feature.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.EvaluateHealthRecordLimitsUseCase
import com.vpnch.cardioapp.core.domain.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.MetricLimitsBundle
import com.vpnch.cardioapp.core.domain.PatientRepository
import com.vpnch.cardioapp.core.domain.SurveyRepository
import com.vpnch.cardioapp.core.domain.VitaminRepository
import com.vpnch.cardioapp.core.model.HealthRecord
import com.vpnch.cardioapp.core.model.formatAsHealthMetric
import com.vpnch.cardioapp.core.model.formatBloodPressure
import com.vpnch.cardioapp.core.model.isOutOfNorm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    patientRepository: PatientRepository,
    private val vitaminRepository: VitaminRepository,
    healthRecordRepository: HealthRecordRepository,
    surveyRepository: SurveyRepository,
    private val evaluateLimits: EvaluateHealthRecordLimitsUseCase,
) : ViewModel() {

    private val today = currentDate()
    private val patient = patientRepository.observeCurrentPatient()
    private val limitsBundle = MutableStateFlow<MetricLimitsBundle?>(null)

    init {
        viewModelScope.launch {
            val currentPatient = patientRepository.getCurrentPatient() ?: return@launch
            limitsBundle.value = MetricLimitsBundle(
                singleLimits = healthRecordRepository
                    .getSingleMetricLimits(currentPatient.ageGroup)
                    .associateBy { it.metricType },
                bloodPressureLimits = healthRecordRepository.getBloodPressureLimits(currentPatient.ageGroup),
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = combine(
        patient.flatMapLatest { currentPatient ->
            currentPatient?.let {
                healthRecordRepository.observeLatestRecord(it.id)
            } ?: flowOf(null)
        },
        patient.flatMapLatest { currentPatient ->
            currentPatient?.let {
                vitaminRepository.observeVitaminIntakes(it.id, today)
            } ?: flowOf(emptyList())
        },
        surveyRepository.observeActiveSurveyLink(),
        limitsBundle,
    ) { latestRecord, vitamins, surveyLink, limits ->
        TodayUiState(
            isLoading = false,
            latestRecord = latestRecord?.toSummary(limits, evaluateLimits),
            vitaminIntakes = vitamins,
            surveyLink = surveyLink,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TodayUiState(),
    )

    fun setVitaminTaken(patientId: String, vitaminId: String, isTaken: Boolean) {
        viewModelScope.launch {
            vitaminRepository.setVitaminTaken(patientId, today, vitaminId, isTaken)
        }
    }

    private fun currentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }
}

private fun HealthRecord.toSummary(
    limits: MetricLimitsBundle?,
    evaluateLimits: EvaluateHealthRecordLimitsUseCase,
): LatestHealthRecordSummary {
    val evaluation = limits?.let {
        evaluateLimits.evaluate(this, it.singleLimits, it.bloodPressureLimits)
    }

    return LatestHealthRecordSummary(
        recordId = id,
        timeLabel = formatRecordTime(createdAt),
        metrics = listOf(
            TodayMetricItem(
                title = "Давление",
                value = formatBloodPressure(systolicPressure, diastolicPressure),
                iconRes = TodayMetricIcons.BLOOD_PRESSURE,
                isOutOfNorm = evaluation?.bloodPressure.isOutOfNorm(),
            ),
            TodayMetricItem(
                title = "Дыхание",
                value = respiratoryRate.formatAsHealthMetric(),
                iconRes = TodayMetricIcons.RESPIRATORY,
                isOutOfNorm = evaluation?.respiratoryRate.isOutOfNorm(),
            ),
            TodayMetricItem(
                title = "Пульс",
                value = heartRate.formatAsHealthMetric(),
                iconRes = TodayMetricIcons.HEART_RATE,
                isOutOfNorm = evaluation?.heartRate.isOutOfNorm(),
            ),
            TodayMetricItem(
                title = "Кислород",
                value = oxygenSaturation.formatAsHealthMetric(),
                iconRes = TodayMetricIcons.OXYGEN,
                isOutOfNorm = evaluation?.oxygenSaturation.isOutOfNorm(),
            ),
        ),
        hasOutOfNorm = evaluation?.hasOutOfNorm == true,
    )
}

private fun formatRecordTime(timestamp: Long): String {
    return SimpleDateFormat("HH:mm", Locale("ru", "RU")).format(Date(timestamp))
}
