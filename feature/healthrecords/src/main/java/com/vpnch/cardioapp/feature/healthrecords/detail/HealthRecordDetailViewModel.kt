package com.vpnch.cardioapp.feature.healthrecords.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.usecase.EvaluateHealthRecordLimitsUseCase
import com.vpnch.cardioapp.core.domain.repository.HealthRecordRepository
import com.vpnch.cardioapp.core.model.health.limits.MetricLimitsBundle
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.core.model.common.DateUtils
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.vpnch.cardioapp.core.model.health.HealthRecord
import com.vpnch.cardioapp.feature.healthrecords.detail.mapper.toMetricItems
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class HealthRecordDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val patientRepository: PatientRepository,
    private val healthRecordRepository: HealthRecordRepository,
    private val evaluateLimits: EvaluateHealthRecordLimitsUseCase,
) : ViewModel() {

    private val recordId: String = checkNotNull(savedStateHandle[RECORD_ID_ARG])

    private val limitsBundle = MutableStateFlow<MetricLimitsBundle?>(null)

    init {
        loadLimits()
    }

    val uiState: StateFlow<HealthRecordDetailUiState> = combine(
        healthRecordRepository.observeRecord(recordId),
        limitsBundle,
    ) { record, limits ->
        buildUiState(record, limits)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = HealthRecordDetailUiState(isLoading = true),
    )


    private fun loadLimits() {
        viewModelScope.launch {
            val patient = patientRepository.getCurrentPatient() ?: return@launch
            val ageGroup = if (patient.useCustomLimits) AgeGroup.Custom else patient.ageGroup

            limitsBundle.value = MetricLimitsBundle(
                singleLimits = healthRecordRepository
                    .getSingleMetricLimits(ageGroup)
                    .associateBy { it.metricType },
                bloodPressureLimits = healthRecordRepository.getBloodPressureLimits(ageGroup),
            )
        }
    }

    private fun buildUiState(
        record: HealthRecord?,
        limits: MetricLimitsBundle?,
    ): HealthRecordDetailUiState {
        return if (record == null) {
            HealthRecordDetailUiState(
                isLoading = false,
                loadError = true,
            )
        } else {
            HealthRecordDetailUiState(
                isLoading = false,
                timeLabel = DateUtils.formatTime(record.createdAt),
                metrics = record.toMetricItems(limits, evaluateLimits),
            )
        }
    }

    companion object {
        const val RECORD_ID_ARG = "recordId"
        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}