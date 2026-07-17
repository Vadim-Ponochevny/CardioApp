package com.vpnch.cardioapp.feature.healthrecords

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.usecase.EvaluateHealthRecordLimitsUseCase
import com.vpnch.cardioapp.core.domain.repository.HealthRecordRepository
import com.vpnch.cardioapp.core.model.health.limits.MetricLimitsBundle
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.core.model.common.DateUtils.currentDateKey
import com.vpnch.cardioapp.core.model.common.DateUtils.formatDayMonthLabel
import com.vpnch.cardioapp.core.model.health.HealthRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HealthRecordsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    patientRepository: PatientRepository,
    private val healthRecordRepository: HealthRecordRepository,
    private val evaluateLimits: EvaluateHealthRecordLimitsUseCase,
) : ViewModel() {

    private val dateKey: String = savedStateHandle.get<String>(DATE_ARG) ?: currentDateKey()
    private val patient = patientRepository.currentPatient
    private val limitsBundle = MutableStateFlow<MetricLimitsBundle?>(null)

    init {
        viewModelScope.launch {
            val patient = patientRepository.getCurrentPatient() ?: return@launch
            val limitsAgeGroup = if (patient.useCustomLimits) AgeGroup.Custom else patient.ageGroup
            limitsBundle.value = MetricLimitsBundle(
                singleLimits = healthRecordRepository
                    .getSingleMetricLimits(limitsAgeGroup)
                    .associateBy { it.metricType },
                bloodPressureLimits = healthRecordRepository.getBloodPressureLimits(limitsAgeGroup),
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = combine(
        patient.flatMapLatest { currentPatient ->
            currentPatient?.let {
                healthRecordRepository.observeRecordsForDate(it.id, dateKey)
            } ?: flowOf(emptyList())
        },
        limitsBundle,
    ) { records, limits ->
        HealthRecordsUiState(
            dateKey = dateKey,
            title = "Записи",
            dateLabel = formatDayMonthLabel(dateKey),
            records = records.map { record -> toListItem(record, limits) },
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HealthRecordsUiState(dateKey = dateKey, dateLabel = formatDayMonthLabel(dateKey)),
    )

    private val _events = MutableSharedFlow<HealthRecordsEvent>()
    val events = _events.asSharedFlow()

    fun deleteRecord(recordId: String) {
        viewModelScope.launch {
            try {
                healthRecordRepository.deleteRecord(recordId)
            } catch (_: Exception) {
                _events.emit(HealthRecordsEvent.DeleteFailed)
            }
        }
    }

    private fun toListItem(record: HealthRecord, limits: MetricLimitsBundle?): HealthRecordListItem {
        val evaluation = limits?.let { bundle ->
            evaluateLimits.evaluate(record, bundle.singleLimits, bundle.bloodPressureLimits)
        }
        return HealthRecordListItem(
            record = record,
            hasOutOfNorm = evaluation?.hasOutOfNorm == true,
            hasCritical = evaluation?.hasDoctorSoon == true,
        )
    }

    companion object {
        const val DATE_ARG = "date"
    }
}
