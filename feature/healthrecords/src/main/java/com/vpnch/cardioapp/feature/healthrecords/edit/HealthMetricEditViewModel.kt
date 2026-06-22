package com.vpnch.cardioapp.feature.healthrecords.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.CheckHealthMetricLimitsUseCase
import com.vpnch.cardioapp.core.domain.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.PatientRepository
import com.vpnch.cardioapp.core.model.BloodPressureLimits
import com.vpnch.cardioapp.core.model.HealthRecord
import com.vpnch.cardioapp.core.model.MetricStatus
import com.vpnch.cardioapp.core.model.MetricType
import com.vpnch.cardioapp.core.model.SingleMetricLimits
import com.vpnch.cardioapp.core.model.normalDiastolicPlaceholder
import com.vpnch.cardioapp.core.model.normalPlaceholder
import com.vpnch.cardioapp.core.model.normalSystolicPlaceholder
import com.vpnch.cardioapp.feature.healthrecords.HealthMetricKind
import com.vpnch.cardioapp.feature.healthrecords.create.FieldWarning
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HealthMetricEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val patientRepository: PatientRepository,
    private val healthRecordRepository: HealthRecordRepository,
    private val checkLimits: CheckHealthMetricLimitsUseCase,
) : ViewModel() {

    private val recordId: String = checkNotNull(savedStateHandle[RECORD_ID_ARG])
    private val metricKind = HealthMetricKind.fromRouteKey(
        checkNotNull(savedStateHandle[METRIC_TYPE_ARG]),
    )
    private val isPersisting = AtomicBoolean(false)

    private var singleLimits: Map<MetricType, SingleMetricLimits> = emptyMap()
    private var bloodPressureLimits: BloodPressureLimits? = null
    private var loadedRecord: HealthRecord? = null

    private val _uiState = MutableStateFlow(
        HealthMetricEditUiState(
            metricKind = metricKind,
            title = metricKind.title,
        ),
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val patient = patientRepository.getCurrentPatient() ?: run {
                _uiState.update { it.copy(loadError = true) }
                return@launch
            }

            runCatching {
                singleLimits = healthRecordRepository
                    .getSingleMetricLimits(patient.ageGroup)
                    .associateBy { it.metricType }
                bloodPressureLimits = healthRecordRepository.getBloodPressureLimits(patient.ageGroup)
            }

            val record = healthRecordRepository.getRecord(recordId)
            if (record == null) {
                _uiState.update { it.copy(loadError = true) }
                return@launch
            }

            loadedRecord = record
            _uiState.value = record.toEditUiState(metricKind).recalculate()
        }
    }

    fun onSystolicChange(value: String) {
        update { it.copy(systolicInput = value.filterDigits()) }
    }

    fun onDiastolicChange(value: String) {
        update { it.copy(diastolicInput = value.filterDigits()) }
    }

    fun onSingleChange(value: String) {
        update { it.copy(singleInput = value.filterDigits()) }
    }

    fun save(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val saved = persist(onSuccess)
            if (!saved && _uiState.value.saveError == null) {
                // Validation failed before persist started.
            }
        }
    }

    private suspend fun persist(onSuccess: () -> Unit): Boolean {
        val state = _uiState.value
        if (!state.canSave || state.loadError) return false
        if (!isPersisting.compareAndSet(false, true)) return false

        _uiState.update { it.copy(isSaving = true, saveError = null) }

        return try {
            val record = loadedRecord ?: error("Record is missing")
            val updatedRecord = state.applyToRecord(record)
            healthRecordRepository.updateRecord(updatedRecord)
            loadedRecord = updatedRecord
            _uiState.update { it.copy(isSaving = false) }
            onSuccess()
            true
        } catch (_: Exception) {
            _uiState.update {
                it.copy(isSaving = false, saveError = SAVE_ERROR_MESSAGE)
            }
            false
        } finally {
            isPersisting.set(false)
        }
    }

    private fun update(transform: (HealthMetricEditUiState) -> HealthMetricEditUiState) {
        _uiState.update { transform(it).recalculate().copy(saveError = null) }
    }

    private fun HealthMetricEditUiState.recalculate(): HealthMetricEditUiState {
        val bloodPressureWarning = if (isBloodPressure) {
            warningForBloodPressure(systolicInput, diastolicInput)
        } else {
            null
        }
        val singleWarning = if (!isBloodPressure) {
            warningForSingleMetric(singleInput, metricKind.toMetricType())
        } else {
            null
        }

        val canSave = if (isBloodPressure) {
            systolicInput.toPositiveInt() != null && diastolicInput.toPositiveInt() != null
        } else {
            singleInput.toPositiveInt() != null
        }

        return copy(
            bloodPressureWarning = bloodPressureWarning,
            singleWarning = singleWarning,
            systolicPlaceholder = bloodPressureLimits?.normalSystolicPlaceholder().orEmpty(),
            diastolicPlaceholder = bloodPressureLimits?.normalDiastolicPlaceholder().orEmpty(),
            singlePlaceholder = if (isBloodPressure) {
                ""
            } else {
                singleLimits[metricKind.toMetricType()]?.normalPlaceholder().orEmpty()
            },
            canSave = canSave,
        )
    }

    private fun warningForBloodPressure(systolicInput: String, diastolicInput: String): FieldWarning? {
        val systolic = systolicInput.toPositiveInt() ?: return null
        val diastolic = diastolicInput.toPositiveInt() ?: return null
        return warningForStatus(
            checkLimits.checkBloodPressure(systolic, diastolic, bloodPressureLimits),
        )
    }

    private fun warningForSingleMetric(input: String, metricType: MetricType): FieldWarning? {
        val value = input.toPositiveInt() ?: return null
        val limits = singleLimits[metricType] ?: return null
        return warningForStatus(checkLimits.checkSingleValue(value, limits))
    }

    private fun warningForStatus(status: MetricStatus): FieldWarning? {
        return when (status) {
            MetricStatus.Normal, MetricStatus.Unknown -> null
            MetricStatus.Attention -> FieldWarning.Attention
            MetricStatus.DoctorSoon -> FieldWarning.Critical
        }
    }

    companion object {
        const val RECORD_ID_ARG = "recordId"
        const val METRIC_TYPE_ARG = "metricType"
        private const val SAVE_ERROR_MESSAGE = "Не удалось сохранить. Попробуй ещё раз."
    }
}

data class HealthMetricEditUiState(
    val metricKind: HealthMetricKind,
    val title: String,
    val isBloodPressure: Boolean = false,
    val systolicInput: String = "",
    val diastolicInput: String = "",
    val singleInput: String = "",
    val singleLabel: String = "",
    val bloodPressureWarning: FieldWarning? = null,
    val singleWarning: FieldWarning? = null,
    val systolicPlaceholder: String = "",
    val diastolicPlaceholder: String = "",
    val singlePlaceholder: String = "",
    val isSaving: Boolean = false,
    val canSave: Boolean = false,
    val loadError: Boolean = false,
    val saveError: String? = null,
)

private val HealthMetricKind.title: String
    get() = when (this) {
        HealthMetricKind.BloodPressure -> "Давление"
        HealthMetricKind.RespiratoryRate -> "Дыхание"
        HealthMetricKind.HeartRate -> "Пульс"
        HealthMetricKind.OxygenSaturation -> "Кислород"
    }

private fun HealthMetricKind.toMetricType(): MetricType {
    return when (this) {
        HealthMetricKind.BloodPressure -> error("Blood pressure uses two fields")
        HealthMetricKind.RespiratoryRate -> MetricType.RespiratoryRate
        HealthMetricKind.HeartRate -> MetricType.HeartRate
        HealthMetricKind.OxygenSaturation -> MetricType.OxygenSaturation
    }
}

private fun HealthRecord.toEditUiState(kind: HealthMetricKind): HealthMetricEditUiState {
    return when (kind) {
        HealthMetricKind.BloodPressure -> HealthMetricEditUiState(
            metricKind = kind,
            title = kind.title,
            isBloodPressure = true,
            systolicInput = systolicPressure?.toString().orEmpty(),
            diastolicInput = diastolicPressure?.toString().orEmpty(),
        )

        HealthMetricKind.RespiratoryRate -> HealthMetricEditUiState(
            metricKind = kind,
            title = kind.title,
            singleInput = respiratoryRate?.toString().orEmpty(),
            singleLabel = "22",
        )

        HealthMetricKind.HeartRate -> HealthMetricEditUiState(
            metricKind = kind,
            title = kind.title,
            singleInput = heartRate?.toString().orEmpty(),
            singleLabel = "70",
        )

        HealthMetricKind.OxygenSaturation -> HealthMetricEditUiState(
            metricKind = kind,
            title = kind.title,
            singleInput = oxygenSaturation?.toString().orEmpty(),
            singleLabel = "95",
        )
    }
}

private fun HealthMetricEditUiState.applyToRecord(record: HealthRecord): HealthRecord {
    val now = System.currentTimeMillis()
    return when (metricKind) {
        HealthMetricKind.BloodPressure -> record.copy(
            updatedAt = now,
            systolicPressure = systolicInput.toPositiveInt(),
            diastolicPressure = diastolicInput.toPositiveInt(),
        )

        HealthMetricKind.RespiratoryRate -> record.copy(
            updatedAt = now,
            respiratoryRate = singleInput.toPositiveInt(),
        )

        HealthMetricKind.HeartRate -> record.copy(
            updatedAt = now,
            heartRate = singleInput.toPositiveInt(),
        )

        HealthMetricKind.OxygenSaturation -> record.copy(
            updatedAt = now,
            oxygenSaturation = singleInput.toPositiveInt(),
        )
    }
}

private fun String.filterDigits(): String = filter { it.isDigit() }

private fun String.toPositiveInt(): Int? = toIntOrNull()?.takeIf { it > 0 }
