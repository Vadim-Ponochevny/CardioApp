package com.vpnch.cardioapp.feature.healthrecords.edit

import androidx.lifecycle.SavedStateHandle
import com.vpnch.cardioapp.core.domain.repository.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.core.domain.usecase.CheckHealthMetricLimitsUseCase
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.core.model.health.limits.normalDiastolicPlaceholder
import com.vpnch.cardioapp.core.model.health.limits.normalPlaceholder
import com.vpnch.cardioapp.core.model.health.limits.normalSystolicPlaceholder
import com.vpnch.cardioapp.core.model.health.metrics.MetricType
import com.vpnch.cardioapp.feature.healthrecords.model.HealthMetricKind
import com.vpnch.cardioapp.feature.healthrecords.base.HealthRecordBaseViewModel
import com.vpnch.cardioapp.feature.healthrecords.ui.extensions.displayTitle
import com.vpnch.cardioapp.feature.healthrecords.model.toMetricType
import com.vpnch.cardioapp.feature.healthrecords.edit.mapper.applyToRecord
import com.vpnch.cardioapp.feature.healthrecords.edit.mapper.toEditUiState
import com.vpnch.cardioapp.feature.healthrecords.utils.filterDecimal
import com.vpnch.cardioapp.feature.healthrecords.utils.filterDigits
import com.vpnch.cardioapp.feature.healthrecords.utils.toPositiveInt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

@HiltViewModel
class HealthMetricEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    patientRepository: PatientRepository,
    healthRecordRepository: HealthRecordRepository,
    checkLimits: CheckHealthMetricLimitsUseCase,
) : HealthRecordBaseViewModel(patientRepository, healthRecordRepository, checkLimits) {

    private val recordId: String = checkNotNull(savedStateHandle[RECORD_ID_ARG])
    private val metricKind = HealthMetricKind.fromRouteKey(
        checkNotNull(savedStateHandle[METRIC_TYPE_ARG]),
    )

    private val _uiState = MutableStateFlow(
        HealthMetricEditUiState(
            metricKind = metricKind,
            title = metricKind.displayTitle,
        ),
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val patient = patientRepository.getCurrentPatient() ?: run {
                _uiState.update { it.copy(loadError = true) }
                return@launch
            }

            val limitsAgeGroup = if (patient.useCustomLimits) AgeGroup.Custom else patient.ageGroup
            runCatching { loadLimits(limitsAgeGroup) }

            val record = healthRecordRepository.getRecord(recordId)
            if (record == null) {
                _uiState.update { it.copy(loadError = true) }
                return@launch
            }

            val editState = record.toEditUiState(metricKind).recalculate()
            val hasWarning = editState.bloodPressureWarning != null || editState.singleWarning != null
            _uiState.value = editState.copy(isLoaded = true, showWarningOnLoad = hasWarning)
        }
    }

    fun onSystolicChange(value: String) {
        update { it.copy(systolicInput = value.filterDigits()) }
    }

    fun onDiastolicChange(value: String) {
        update { it.copy(diastolicInput = value.filterDigits()) }
    }

    fun onSingleChange(value: String) {
        val filtered = if (metricKind == HealthMetricKind.INR) value.filterDecimal() else value.filterDigits()
        update { it.copy(singleInput = filtered) }
    }

    fun save(onSuccess: () -> Unit) {
        viewModelScope.launch {
            persist(onSuccess)
        }
    }

    private suspend fun persist(onSuccess: () -> Unit): Boolean {
        val state = _uiState.value
        if (!state.canSave || state.loadError) return false
        if (!isPersisting.compareAndSet(false, true)) return false

        _uiState.update { it.copy(isSaving = true, saveError = null) }

        return try {
            val record = healthRecordRepository.getRecord(recordId) ?: error("Record is missing")
            val updatedRecord = state.applyToRecord(record)
            healthRecordRepository.saveRecord(updatedRecord)
            _uiState.update { it.copy(isSaving = false) }
            onSuccess()
            true
        } catch (_: Exception) {
            _uiState.update { it.copy(isSaving = false, saveError = SAVE_ERROR_MESSAGE) }
            false
        } finally {
            isPersisting.set(false)
        }
    }

    private fun update(transform: (HealthMetricEditUiState) -> HealthMetricEditUiState) {
        _uiState.update { transform(it).recalculate().copy(saveError = null, showWarningOnLoad = false) }
    }

    private fun HealthMetricEditUiState.recalculate(): HealthMetricEditUiState {
        val bloodPressureWarning = if (isBloodPressure) {
            warningForBloodPressure(systolicInput, diastolicInput)
        } else null

        val singleWarning = if (!isBloodPressure) {
            when {
                isInr -> warningForInr(singleInput)
                metricKind == HealthMetricKind.RespiratoryRate -> null
                else -> warningForSingleMetric(singleInput, metricKind.toMetricType())
            }
        } else null

        val canSave = when {
            isBloodPressure -> systolicInput.toPositiveInt() != null && diastolicInput.toPositiveInt() != null
            isInr -> singleInput.toDoubleOrNull()?.let { it > 0 } == true
            else -> singleInput.toPositiveInt() != null
        }

        val singlePlaceholder = when {
            isBloodPressure -> ""
            isInr -> singleLimits[MetricType.INR]?.takeIf { it.normalMax > 0 }?.let { l ->
                val min = l.normalMin / 10.0
                val max = l.normalMax / 10.0
                fun Double.fmt() = if (this == toLong().toDouble()) toLong().toString() else toString()
                "${min.fmt()}–${max.fmt()}"
            }.orEmpty()
            else -> singleLimits[metricKind.toMetricType()]?.normalPlaceholder().orEmpty()
        }

        return copy(
            bloodPressureWarning = bloodPressureWarning,
            singleWarning = singleWarning,
            systolicPlaceholder = bloodPressureLimits?.normalSystolicPlaceholder().orEmpty(),
            diastolicPlaceholder = bloodPressureLimits?.normalDiastolicPlaceholder().orEmpty(),
            singlePlaceholder = singlePlaceholder,
            canSave = canSave,
        )
    }

    companion object {
        const val RECORD_ID_ARG = "recordId"
        const val METRIC_TYPE_ARG = "metricType"
        private const val SAVE_ERROR_MESSAGE = "Не удалось сохранить. Попробуй ещё раз."
    }
}
