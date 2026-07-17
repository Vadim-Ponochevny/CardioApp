package com.vpnch.cardioapp.feature.healthrecords.create

import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.analytics.Analytics
import com.vpnch.cardioapp.core.domain.analytics.AnalyticsEvent
import com.vpnch.cardioapp.core.domain.repository.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.core.domain.usecase.CheckHealthMetricLimitsUseCase
import com.vpnch.cardioapp.core.domain.usecase.EvaluateHealthRecordLimitsUseCase
import com.vpnch.cardioapp.core.model.health.HealthRecord
import com.vpnch.cardioapp.core.model.health.limits.normalDiastolicPlaceholder
import com.vpnch.cardioapp.core.model.health.limits.normalPlaceholder
import com.vpnch.cardioapp.core.model.health.limits.normalSystolicPlaceholder
import com.vpnch.cardioapp.core.model.health.metrics.MetricStatus
import com.vpnch.cardioapp.core.model.health.metrics.MetricType
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.core.model.health.InrConverter
import com.vpnch.cardioapp.feature.healthrecords.base.HealthRecordBaseViewModel
import com.vpnch.cardioapp.feature.healthrecords.create.model.HealthRecordCreatePage
import com.vpnch.cardioapp.feature.healthrecords.utils.filterDecimal
import com.vpnch.cardioapp.feature.healthrecords.utils.filterDigits
import com.vpnch.cardioapp.feature.healthrecords.utils.toPositiveInt
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HealthRecordCreateViewModel @Inject constructor(
    // 1. Зависимости
    patientRepository: PatientRepository,
    healthRecordRepository: HealthRecordRepository,
    checkLimits: CheckHealthMetricLimitsUseCase,
    private val evaluateLimits: EvaluateHealthRecordLimitsUseCase,
    private val analytics: Analytics,
) : HealthRecordBaseViewModel(patientRepository, healthRecordRepository, checkLimits) {

    private var persistedRecordId: String? = null
    private val _uiState = MutableStateFlow(HealthRecordCreateUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val patient = patientRepository.getCurrentPatient() ?: return@launch
            val limitsAgeGroup = if (patient.useCustomLimits) AgeGroup.Custom else patient.ageGroup
            runCatching { loadLimits(limitsAgeGroup) }.onFailure {
                _uiState.update { it.copy(loadError = LOAD_LIMITS_ERROR_MESSAGE) }
                return@launch
            }
            _uiState.update { it.copy(showInrPage = patient.takesWarfarin).recalculate() }
        }
    }

    // ==================== Public methods (UI API) ====================

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

    fun onInrChange(value: String) {
        updateInputs { it.copy(inrInput = value.filterDecimal()) }
    }

    fun onPrimaryAction() {
        val state = _uiState.value
        if (!state.canProceed || state.isSaving) return

        val pages = state.availablePages
        val currentIndex = pages.indexOf(state.currentPage)
        if (currentIndex < state.lastPageIndex) {
            _uiState.update { it.copy(currentPage = pages[currentIndex + 1]).recalculate() }
            return
        }

        saveRecord()
    }

    fun onToolbarBack(): Boolean {
        val state = _uiState.value
        val pages = state.availablePages
        val currentIndex = pages.indexOf(state.currentPage)
        if (currentIndex > 0) {
            _uiState.update { it.copy(currentPage = pages[currentIndex - 1]).recalculate() }
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
            if (saveDraftIfNeeded()) onSuccess() else onFailure()
        }
    }

    // ==================== Saving the record ====================

    private fun saveRecord() {
        viewModelScope.launch {
            val state = _uiState.value
            if (!isRecordValid(state)) return@launch
            persistRecord(
                buildRecord = { patientId, existingRecord, now ->
                    buildRecordFromState(state, patientId, existingRecord, now)
                },
                onSuccess = { currentState, savedRecord ->
                    handleSaveSuccess(currentState, savedRecord)
                },
            )
        }
    }

    private suspend fun saveDraftIfNeeded(): Boolean {
        val state = _uiState.value
        if (state.isSaved || !state.hasAnyInput() || !state.isIncomplete()) return true
        return persistRecord(
            buildRecord = { patientId, existingRecord, now ->
                buildRecordFromState(state, patientId, existingRecord, now)
            },
            onSuccess = { currentState, _ -> currentState },
        )
    }

    private suspend fun persistRecord(
        buildRecord: (patientId: String, existingRecord: HealthRecord?, now: Long) -> HealthRecord,
        onSuccess: (HealthRecordCreateUiState, HealthRecord) -> HealthRecordCreateUiState,
    ): Boolean {
        if (!isPersisting.compareAndSet(false, true)) return false
        _uiState.update { it.copy(isSaving = true, saveError = null) }
        return try {
            val patient = patientRepository.getCurrentPatient()
                ?: error("Patient profile is missing")
            val existingRecord = persistedRecordId?.let { healthRecordRepository.getRecord(it) }
            val now = System.currentTimeMillis()
            val record = buildRecord(patient.id, existingRecord, now)
            healthRecordRepository.saveRecord(record)
            persistedRecordId = record.id
            _uiState.update { onSuccess(it, record).copy(isSaving = false, saveError = null) }
            true
        } catch (_: Exception) {
            _uiState.update { it.copy(isSaving = false, saveError = SAVE_ERROR_MESSAGE) }
            false
        } finally {
            isPersisting.set(false)
        }
    }

    private fun isRecordValid(state: HealthRecordCreateUiState): Boolean =
        state.systolicInput.toPositiveInt() != null &&
            state.diastolicInput.toPositiveInt() != null &&
            state.respiratoryInput.toPositiveInt() != null &&
            state.heartRateInput.toPositiveInt() != null &&
            state.oxygenInput.toPositiveInt() != null &&
            (!state.showInrPage || InrConverter.toStoredInt(state.inrInput) != null)

    private fun handleSaveSuccess(
        currentState: HealthRecordCreateUiState,
        savedRecord: HealthRecord,
    ): HealthRecordCreateUiState {
        val evaluation = evaluateLimits.evaluate(savedRecord, singleLimits, bloodPressureLimits)
        analytics.report(AnalyticsEvent.MeasurementAdded)
        if (evaluation.hasOutOfNorm || evaluation.hasDoctorSoon) {
            analytics.report(AnalyticsEvent.AbnormalValueDetected)
        }
        return currentState.copy(isSaved = true, savedWithDoctorAlert = evaluation.hasDoctorSoon)
    }

    private fun buildRecordFromState(
        state: HealthRecordCreateUiState,
        patientId: String,
        existingRecord: HealthRecord?,
        now: Long,
    ): HealthRecord = HealthRecord(
        id = existingRecord?.id ?: UUID.randomUUID().toString(),
        patientId = patientId,
        createdAt = existingRecord?.createdAt ?: now,
        updatedAt = now,
        systolicPressure = state.systolicInput.toPositiveInt(),
        diastolicPressure = state.diastolicInput.toPositiveInt(),
        respiratoryRate = state.respiratoryInput.toPositiveInt(),
        heartRate = state.heartRateInput.toPositiveInt(),
        oxygenSaturation = state.oxygenInput.toPositiveInt(),
        inr = if (state.showInrPage) InrConverter.toStoredInt(state.inrInput) else null,
    )

    // ==================== State recalculation ====================

    private fun updateInputs(transform: (HealthRecordCreateUiState) -> HealthRecordCreateUiState) {
        _uiState.update { transform(it).recalculate().copy(saveError = null, isSaved = false) }
    }

    private fun HealthRecordCreateUiState.recalculate(): HealthRecordCreateUiState {
        val warnings = calculateWarnings()
        val placeholders = getPlaceholders()
        return copy(
            bloodPressureWarning = warnings.bloodPressure,
            respiratoryWarning = warnings.respiratory,
            heartRateWarning = warnings.heartRate,
            oxygenWarning = warnings.oxygen,
            inrWarning = warnings.inr,
            systolicPlaceholder = placeholders.systolic,
            diastolicPlaceholder = placeholders.diastolic,
            metricPlaceholder = placeholders.metric,
            canProceed = isCurrentPageValid(),
            primaryButtonLabel = if (isLastPage()) "Сохранить" else "Далее",
        )
    }

    private fun HealthRecordCreateUiState.calculateWarnings(): WarningBundle = WarningBundle(
        bloodPressure = warningForBloodPressure(systolicInput, diastolicInput),
        respiratory = null,
        heartRate = warningForSingleMetric(heartRateInput, MetricType.HeartRate),
        oxygen = warningForSingleMetric(oxygenInput, MetricType.OxygenSaturation),
        inr = if (showInrPage) warningForInr(inrInput) else null,
    )

    private data class WarningBundle(
        val bloodPressure: MetricStatus?,
        val respiratory: MetricStatus?,
        val heartRate: MetricStatus?,
        val oxygen: MetricStatus?,
        val inr: MetricStatus?,
    )

    private fun HealthRecordCreateUiState.isCurrentPageValid(): Boolean = when (currentPage) {
        HealthRecordCreatePage.BloodPressure ->
            systolicInput.toPositiveInt() != null && diastolicInput.toPositiveInt() != null
        HealthRecordCreatePage.RespiratoryRate -> respiratoryInput.toPositiveInt() != null
        HealthRecordCreatePage.HeartRate -> heartRateInput.toPositiveInt() != null
        HealthRecordCreatePage.OxygenSaturation -> oxygenInput.toPositiveInt() != null
        HealthRecordCreatePage.INR -> inrInput.toDoubleOrNull()?.let { it > 0 } == true
    }

    private fun HealthRecordCreateUiState.isLastPage(): Boolean =
        availablePages.indexOf(currentPage) == lastPageIndex

    private fun HealthRecordCreateUiState.getPlaceholders(): Placeholders = Placeholders(
        systolic = bloodPressureLimits?.normalSystolicPlaceholder().orEmpty(),
        diastolic = bloodPressureLimits?.normalDiastolicPlaceholder().orEmpty(),
        metric = metricPlaceholderForPage(currentPage),
    )

    private data class Placeholders(
        val systolic: String,
        val diastolic: String,
        val metric: String,
    )

    private fun metricPlaceholderForPage(page: HealthRecordCreatePage): String = when (page) {
        HealthRecordCreatePage.RespiratoryRate ->
            singleLimits[MetricType.RespiratoryRate]?.normalPlaceholder().orEmpty()
        HealthRecordCreatePage.HeartRate ->
            singleLimits[MetricType.HeartRate]?.normalPlaceholder().orEmpty()
        HealthRecordCreatePage.OxygenSaturation ->
            singleLimits[MetricType.OxygenSaturation]?.normalPlaceholder().orEmpty()
        HealthRecordCreatePage.INR -> formatInrPlaceholder()
        else -> ""
    }

    private fun formatInrPlaceholder(): String {
        val limits = singleLimits[MetricType.INR]?.takeIf { it.normalMax > 0 } ?: return ""
        val min = limits.normalMin / 10.0
        val max = limits.normalMax / 10.0
        fun Double.fmt() = if (this == toLong().toDouble()) toLong().toString() else toString()
        return "${min.fmt()}–${max.fmt()}"
    }

    // ==================== State checks ====================

    private fun HealthRecordCreateUiState.hasAnyInput(): Boolean =
        systolicInput.isNotBlank() || diastolicInput.isNotBlank() ||
            respiratoryInput.isNotBlank() || heartRateInput.isNotBlank() ||
            oxygenInput.isNotBlank() || inrInput.isNotBlank()

    private fun HealthRecordCreateUiState.isIncomplete(): Boolean =
        systolicInput.toPositiveInt() == null ||
            diastolicInput.toPositiveInt() == null ||
            respiratoryInput.toPositiveInt() == null ||
            heartRateInput.toPositiveInt() == null ||
            oxygenInput.toPositiveInt() == null ||
            (showInrPage && InrConverter.toStoredInt(inrInput) == null)

    // ==================== Константы ====================

    companion object {
        private const val SAVE_ERROR_MESSAGE = "Не удалось сохранить запись. Попробуй ещё раз."
        private const val LOAD_LIMITS_ERROR_MESSAGE = "Не удалось загрузить нормы показателей."
    }
}
