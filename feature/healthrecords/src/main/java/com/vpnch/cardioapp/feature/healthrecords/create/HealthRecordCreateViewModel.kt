package com.vpnch.cardioapp.feature.healthrecords.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.CheckHealthMetricLimitsUseCase
import com.vpnch.cardioapp.core.domain.EvaluateHealthRecordLimitsUseCase
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
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HealthRecordCreateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val patientRepository: PatientRepository,
    private val healthRecordRepository: HealthRecordRepository,
    private val checkLimits: CheckHealthMetricLimitsUseCase,
    private val evaluateLimits: EvaluateHealthRecordLimitsUseCase,
) : ViewModel() {

    private val editingRecordId: String? = savedStateHandle.get<String>(RECORD_ID_ARG)
    private var persistedRecordId: String? = editingRecordId
    private val isPersisting = AtomicBoolean(false)

    private var singleLimits: Map<MetricType, SingleMetricLimits> = emptyMap()
    private var bloodPressureLimits: BloodPressureLimits? = null

    private val _uiState = MutableStateFlow(HealthRecordCreateUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val patient = patientRepository.getCurrentPatient() ?: return@launch
            runCatching {
                singleLimits = healthRecordRepository
                    .getSingleMetricLimits(patient.ageGroup)
                    .associateBy { it.metricType }
                bloodPressureLimits = healthRecordRepository.getBloodPressureLimits(patient.ageGroup)
            }

            if (editingRecordId != null) {
                loadRecord(editingRecordId)
            } else {
                _uiState.update { it.recalculate() }
            }
        }
    }

    fun onSystolicChange(value: String) {
        updateInputs { it.copy(systolicInput = value.filterDigits()) }
    }

    fun onDiastolicChange(value: String) {
        updateInputs { it.copy(diastolicInput = value.filterDigits()) }
    }

    fun onRespiratoryChange(value: String) {
        updateInputs { it.copy(respiratoryInput = value.filterDigits()) }
    }

    fun onHeartRateChange(value: String) {
        updateInputs { it.copy(heartRateInput = value.filterDigits()) }
    }

    fun onOxygenChange(value: String) {
        updateInputs { it.copy(oxygenInput = value.filterDigits()) }
    }

    fun onPrimaryAction() {
        val state = _uiState.value
        if (!state.canProceed || state.isSaving) return

        if (state.currentPage < HealthRecordCreatePage.entries.lastIndex) {
            _uiState.update {
                it.copy(currentPage = it.currentPage + 1).recalculate()
            }
            return
        }

        saveRecord()
    }

    fun onToolbarBack(): Boolean {
        val state = _uiState.value
        if (state.currentPage > 0) {
            _uiState.update { it.copy(currentPage = it.currentPage - 1).recalculate() }
            return true
        }
        return false
    }

    fun shouldConfirmDraftOnExit(): Boolean {
        val state = _uiState.value
        return !state.isSaved && !state.isSaving && state.hasAnyInput() && state.isIncomplete()
    }

    fun saveDraftAndExit(onSuccess: () -> Unit, onFailure: () -> Unit = {}) {
        viewModelScope.launch {
            if (saveDraftIfNeeded()) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    private suspend fun loadRecord(recordId: String) {
        try {
            val record = healthRecordRepository.getRecord(recordId)
            if (record == null) {
                _uiState.update { it.copy(loadError = RECORD_NOT_FOUND_MESSAGE) }
                return
            }
            persistedRecordId = record.id
            _uiState.value = HealthRecordCreateUiState(
                currentPage = firstIncompletePage(record),
                systolicInput = record.systolicPressure?.toString().orEmpty(),
                diastolicInput = record.diastolicPressure?.toString().orEmpty(),
                respiratoryInput = record.respiratoryRate?.toString().orEmpty(),
                heartRateInput = record.heartRate?.toString().orEmpty(),
                oxygenInput = record.oxygenSaturation?.toString().orEmpty(),
            ).recalculate()
        } catch (_: Exception) {
            _uiState.update { it.copy(loadError = LOAD_ERROR_MESSAGE) }
        }
    }

    private fun saveRecord() {
        viewModelScope.launch {
            val state = _uiState.value
            val systolic = state.systolicInput.toPositiveInt() ?: return@launch
            val diastolic = state.diastolicInput.toPositiveInt() ?: return@launch
            val respiratory = state.respiratoryInput.toPositiveInt() ?: return@launch
            val heartRate = state.heartRateInput.toPositiveInt() ?: return@launch
            val oxygen = state.oxygenInput.toPositiveInt() ?: return@launch

            persistRecord(
                buildRecord = { patientId, existingRecord, now ->
                    HealthRecord(
                        id = existingRecord?.id ?: UUID.randomUUID().toString(),
                        patientId = patientId,
                        createdAt = existingRecord?.createdAt ?: now,
                        updatedAt = now,
                        systolicPressure = systolic,
                        diastolicPressure = diastolic,
                        respiratoryRate = respiratory,
                        heartRate = heartRate,
                        oxygenSaturation = oxygen,
                    )
                },
                onSuccess = { state, savedRecord ->
                    val needsDoctorAlert = evaluateLimits
                        .evaluate(savedRecord, singleLimits, bloodPressureLimits)
                        .hasDoctorSoon
                    state.copy(isSaved = true, savedWithDoctorAlert = needsDoctorAlert)
                },
            )
        }
    }

    private suspend fun saveDraftIfNeeded(): Boolean {
        val state = _uiState.value
        if (state.isSaved || !state.hasAnyInput() || !state.isIncomplete()) {
            return true
        }

        return persistRecord(
            buildRecord = { patientId, existingRecord, now ->
                HealthRecord(
                    id = existingRecord?.id ?: UUID.randomUUID().toString(),
                    patientId = patientId,
                    createdAt = existingRecord?.createdAt ?: now,
                    updatedAt = now,
                    systolicPressure = _uiState.value.systolicInput.toPositiveInt(),
                    diastolicPressure = _uiState.value.diastolicInput.toPositiveInt(),
                    respiratoryRate = _uiState.value.respiratoryInput.toPositiveInt(),
                    heartRate = _uiState.value.heartRateInput.toPositiveInt(),
                    oxygenSaturation = _uiState.value.oxygenInput.toPositiveInt(),
                )
            },
            onSuccess = { state, _ -> state },
        )
    }

    private suspend fun persistRecord(
        buildRecord: (patientId: String, existingRecord: HealthRecord?, now: Long) -> HealthRecord,
        onSuccess: (HealthRecordCreateUiState, HealthRecord) -> HealthRecordCreateUiState,
    ): Boolean {
        if (!isPersisting.compareAndSet(false, true)) {
            return false
        }

        _uiState.update { it.copy(isSaving = true, saveError = null) }

        return try {
            val patient = patientRepository.getCurrentPatient()
                ?: error("Patient profile is missing")
            val existingRecord = persistedRecordId?.let { healthRecordRepository.getRecord(it) }
            val now = System.currentTimeMillis()
            val record = buildRecord(patient.id, existingRecord, now)

            if (existingRecord == null) {
                healthRecordRepository.addRecord(record)
            } else {
                healthRecordRepository.updateRecord(record)
            }
            persistedRecordId = record.id

            _uiState.update { onSuccess(it, record).copy(isSaving = false, saveError = null) }
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

    private fun updateInputs(transform: (HealthRecordCreateUiState) -> HealthRecordCreateUiState) {
        _uiState.update { transform(it).recalculate().copy(saveError = null) }
    }

    private fun HealthRecordCreateUiState.recalculate(): HealthRecordCreateUiState {
        val bloodPressureWarning = warningForBloodPressure(systolicInput, diastolicInput)
        val respiratoryWarning = warningForSingleMetric(respiratoryInput, MetricType.RespiratoryRate)
        val heartRateWarning = warningForSingleMetric(heartRateInput, MetricType.HeartRate)
        val oxygenWarning = warningForSingleMetric(oxygenInput, MetricType.OxygenSaturation)

        val canProceed = when (currentPage) {
            0 -> systolicInput.toPositiveInt() != null && diastolicInput.toPositiveInt() != null
            1 -> respiratoryInput.toPositiveInt() != null
            2 -> heartRateInput.toPositiveInt() != null
            3 -> oxygenInput.toPositiveInt() != null
            else -> false
        }

        val primaryButtonLabel = if (currentPage == HealthRecordCreatePage.entries.lastIndex) {
            "Сохранить"
        } else {
            "Далее"
        }

        return copy(
            bloodPressureWarning = bloodPressureWarning,
            respiratoryWarning = respiratoryWarning,
            heartRateWarning = heartRateWarning,
            oxygenWarning = oxygenWarning,
            systolicPlaceholder = bloodPressureLimits?.normalSystolicPlaceholder().orEmpty(),
            diastolicPlaceholder = bloodPressureLimits?.normalDiastolicPlaceholder().orEmpty(),
            metricPlaceholder = metricPlaceholderForPage(currentPage),
            canProceed = canProceed,
            primaryButtonLabel = primaryButtonLabel,
        )
    }

    private fun warningForBloodPressure(
        systolicInput: String,
        diastolicInput: String,
    ): FieldWarning? {
        val systolic = systolicInput.toPositiveInt() ?: return null
        val diastolic = diastolicInput.toPositiveInt() ?: return null
        return warningForStatus(
            checkLimits.checkBloodPressure(systolic, diastolic, bloodPressureLimits),
        )
    }

    private fun warningForSingleMetric(
        input: String,
        metricType: MetricType,
    ): FieldWarning? {
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

    private fun metricPlaceholderForPage(page: Int): String {
        return when (page) {
            1 -> singleLimits[MetricType.RespiratoryRate]?.normalPlaceholder().orEmpty()
            2 -> singleLimits[MetricType.HeartRate]?.normalPlaceholder().orEmpty()
            3 -> singleLimits[MetricType.OxygenSaturation]?.normalPlaceholder().orEmpty()
            else -> ""
        }
    }

    private fun HealthRecordCreateUiState.hasAnyInput(): Boolean {
        return systolicInput.isNotBlank() ||
            diastolicInput.isNotBlank() ||
            respiratoryInput.isNotBlank() ||
            heartRateInput.isNotBlank() ||
            oxygenInput.isNotBlank()
    }

    private fun HealthRecordCreateUiState.isIncomplete(): Boolean {
        return systolicInput.toPositiveInt() == null ||
            diastolicInput.toPositiveInt() == null ||
            respiratoryInput.toPositiveInt() == null ||
            heartRateInput.toPositiveInt() == null ||
            oxygenInput.toPositiveInt() == null
    }

    private fun firstIncompletePage(record: HealthRecord): Int {
        if (record.systolicPressure == null || record.diastolicPressure == null) return 0
        if (record.respiratoryRate == null) return 1
        if (record.heartRate == null) return 2
        if (record.oxygenSaturation == null) return 3
        return 0
    }

    private fun String.filterDigits(): String = filter { it.isDigit() }

    private fun String.toPositiveInt(): Int? = toIntOrNull()?.takeIf { it > 0 }

    companion object {
        const val RECORD_ID_ARG = "recordId"
        private const val SAVE_ERROR_MESSAGE = "Не удалось сохранить запись. Попробуй ещё раз."
        private const val LOAD_ERROR_MESSAGE = "Не удалось загрузить запись. Попробуй ещё раз."
        private const val RECORD_NOT_FOUND_MESSAGE = "Запись не найдена."
    }
}
