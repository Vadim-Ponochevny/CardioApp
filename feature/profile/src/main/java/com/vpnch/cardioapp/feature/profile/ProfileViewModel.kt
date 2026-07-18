package com.vpnch.cardioapp.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.notification.NotificationScheduler
import com.vpnch.cardioapp.core.domain.repository.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.core.model.health.limits.BloodPressureLimits
import com.vpnch.cardioapp.core.model.health.metrics.MetricType
import com.vpnch.cardioapp.core.model.patient.Patient
import com.vpnch.cardioapp.core.model.health.limits.SingleMetricLimits
import com.vpnch.cardioapp.core.model.patient.UserType
import com.vpnch.cardioapp.core.model.patient.ValveType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val healthRecordRepository: HealthRecordRepository,
    private val notificationScheduler: NotificationScheduler,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val patient = patientRepository.getCurrentPatient()
            val initialAgeGroup = patient?.ageGroup ?: AgeGroup.Age9To11
            val limits = loadLimitsFor(initialAgeGroup)
            val customEdit = if (patient?.useCustomLimits == true) {
                val customLimits = loadLimitsFor(AgeGroup.Custom)
                mapToEditState(customLimits.singleMetricLimits, customLimits.bloodPressureLimits)
            } else {
                mapToEditState(limits.singleMetricLimits, limits.bloodPressureLimits)
            }
            val notificationsEnabled = patientRepository.getNotificationsEnabled()
            if (notificationsEnabled) notificationScheduler.schedule()
            _uiState.update {
                ProfileUiState(
                    isLoading = false,
                    patientId = patient?.id ?: "",
                    birthDate = patient?.birthDate ?: "",
                    ageGroup = initialAgeGroup,
                    takesWarfarin = patient?.takesWarfarin ?: false,
                    valveType = patient?.valveType,
                    useCustomLimits = patient?.useCustomLimits ?: false,
                    limitsForAgeGroup = limits,
                    customLimitsEdit = customEdit,
                    notificationsEnabled = notificationsEnabled,
                )
            }
        }
    }

    fun onPatientIdChange(value: String) = _uiState.update { it.copy(patientId = value) }

    fun onBirthDateChange(value: String) = _uiState.update { it.copy(birthDate = value) }

    fun onAgeGroupChange(ageGroup: AgeGroup) {
        viewModelScope.launch {
            val limits = loadLimitsFor(ageGroup)
            _uiState.update {
                it.copy(
                    ageGroup = ageGroup,
                    useCustomLimits = false,
                    limitsForAgeGroup = limits,
                    customLimitsEdit = mapToEditState(limits.singleMetricLimits, limits.bloodPressureLimits),
                )
            }
        }
    }

    fun onTakesWarfarinChange(value: Boolean) = _uiState.update { it.copy(takesWarfarin = value) }

    fun onValveTypeChange(value: ValveType?) = _uiState.update { it.copy(valveType = value) }

    fun onCustomLimitsChange(edit: CustomLimitsEditState) = _uiState.update { it.copy(customLimitsEdit = edit) }

    fun onNotificationsEnabledChange(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
        viewModelScope.launch {
            patientRepository.setNotificationsEnabled(enabled)
            if (enabled) notificationScheduler.schedule() else notificationScheduler.cancel()
        }
    }

    fun onOpenCustomLimits() = _uiState.update { it.copy(customLimitsStep = 0, isSingleMetricEdit = false) }

    fun onEditMetricDirectly(metricType: MetricType) {
        val step = when (metricType) {
            MetricType.HeartRate -> 0
            MetricType.BloodPressure -> 1
            MetricType.OxygenSaturation -> 2
            MetricType.INR -> 3
            else -> return
        }
        viewModelScope.launch {
            val state = _uiState.value
            // Pre-populate custom edit from age group limits if not yet customised
            val editState = if (state.useCustomLimits && state.customLimitsEdit != null) {
                state.customLimitsEdit
            } else {
                val limits = loadLimitsFor(state.ageGroup)
                mapToEditState(limits.singleMetricLimits, limits.bloodPressureLimits)
            }
            _uiState.update {
                it.copy(customLimitsStep = step, isSingleMetricEdit = true, customLimitsEdit = editState)
            }
        }
    }

    fun onCustomLimitsBack() {
        val state = _uiState.value
        val current = state.customLimitsStep ?: return
        if (state.isSingleMetricEdit || current == 0) {
            _uiState.update { it.copy(customLimitsStep = null, isSingleMetricEdit = false) }
        } else {
            _uiState.update { it.copy(customLimitsStep = current - 1) }
        }
    }

    fun onCustomLimitsNext() {
        val state = _uiState.value
        val current = state.customLimitsStep ?: return
        val maxStep = if (state.takesWarfarin) 3 else 2
        if (!state.isSingleMetricEdit && current < maxStep) {
            _uiState.update { it.copy(customLimitsStep = current + 1) }
        } else {
            viewModelScope.launch {
                saveCustomLimitsToDb(_uiState.value)
                _uiState.update { it.copy(customLimitsStep = null, isSingleMetricEdit = false, useCustomLimits = true) }
            }
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            val state = _uiState.value
            val existing = patientRepository.getCurrentPatient()

            if (state.useCustomLimits) {
                saveCustomLimitsToDb(state)
            }

            patientRepository.savePatient(
                Patient(
                    id = state.patientId.trim(),
                    birthDate = state.birthDate.trim(),
                    ageGroup = state.ageGroup,
                    userType = existing?.userType ?: UserType.Parent,
                    takesWarfarin = state.takesWarfarin,
                    valveType = state.valveType,
                    useCustomLimits = state.useCustomLimits,
                )
            )
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    private suspend fun saveCustomLimitsToDb(state: ProfileUiState) {
        val edit = state.customLimitsEdit ?: return
        healthRecordRepository.upsertSingleMetricLimits(buildCustomSingleLimits(edit))
        healthRecordRepository.upsertBloodPressureLimits(listOf(buildCustomBpLimits(edit)))
    }

    private suspend fun loadLimitsFor(ageGroup: AgeGroup): AgeGroupLimitsUi {
        val single = healthRecordRepository.getSingleMetricLimits(ageGroup)
        val bp = healthRecordRepository.getBloodPressureLimits(ageGroup)
        return AgeGroupLimitsUi(single, bp)
    }

    private fun mapToEditState(
        singleLimits: List<SingleMetricLimits>,
        bp: BloodPressureLimits?,
    ): CustomLimitsEditState {
        val hr = singleLimits.find { it.metricType == MetricType.HeartRate }
        val rr = singleLimits.find { it.metricType == MetricType.RespiratoryRate }
        val spO2 = singleLimits.find { it.metricType == MetricType.OxygenSaturation }
        val inr = singleLimits.find { it.metricType == MetricType.INR }
        // INR is stored × 10 in DB (2.5 → 25), convert back for display
        fun Int?.inrStr() = this?.let { (it / 10.0).let { v -> if (v == v.toLong().toDouble()) v.toLong().toString() else v.toString() } } ?: ""
        return CustomLimitsEditState(
            hrNormalMin = hr?.normalMin?.toString() ?: "",
            hrNormalMax = hr?.normalMax?.toString() ?: "",
            hrAttentionMin = hr?.attentionMin?.toString() ?: "",
            hrAttentionMax = hr?.attentionMax?.toString() ?: "",
            hrDoctorMax = hr?.doctorSoonMax?.toString() ?: "",
            spO2NormalMin = spO2?.normalMin?.toString() ?: "",
            spO2NormalMax = spO2?.normalMax?.toString() ?: "",
            spO2AttentionMin = spO2?.attentionMin?.toString() ?: "",
            spO2AttentionMax = spO2?.attentionMax?.toString() ?: "",
            spO2DoctorMax = spO2?.doctorSoonMax?.toString() ?: "",
            rrNormalMin = rr?.normalMin?.toString() ?: "",
            rrNormalMax = rr?.normalMax?.toString() ?: "",
            rrAttentionMin = rr?.attentionMin?.toString() ?: "",
            rrAttentionMax = rr?.attentionMax?.toString() ?: "",
            rrDoctorMax = rr?.doctorSoonMax?.toString() ?: "",
            bpSystolicMin = bp?.normalSystolicMin?.toString() ?: "",
            bpSystolicMax = bp?.normalSystolicMax?.toString() ?: "",
            bpDiastolicMin = bp?.normalDiastolicMin?.toString() ?: "",
            bpDiastolicMax = bp?.normalDiastolicMax?.toString() ?: "",
            inrNormalMin = inr?.normalMin.inrStr(),
            inrNormalMax = inr?.normalMax.inrStr(),
            inrAttentionMin = inr?.attentionMin.inrStr(),
            inrAttentionMax = inr?.attentionMax.inrStr(),
            inrDoctorMax = inr?.doctorSoonMax.inrStr(),
        )
    }

    private fun buildCustomSingleLimits(edit: CustomLimitsEditState): List<SingleMetricLimits> {
        fun String.inrToInt() = (toDoubleOrNull()?.times(10))?.toInt()
        return listOf(
            SingleMetricLimits(
                id = "custom-heart-rate", ageGroup = AgeGroup.Custom,
                metricType = MetricType.HeartRate,
                normalMin = edit.hrNormalMin.toIntOrNull() ?: 0,
                normalMax = edit.hrNormalMax.toIntOrNull() ?: 0,
                attentionMin = edit.hrAttentionMin.toIntOrNull(),
                attentionMax = edit.hrAttentionMax.toIntOrNull(),
                doctorSoonMin = null,
                doctorSoonMax = edit.hrDoctorMax.toIntOrNull(),
            ),
            SingleMetricLimits(
                id = "custom-oxygen", ageGroup = AgeGroup.Custom,
                metricType = MetricType.OxygenSaturation,
                normalMin = edit.spO2NormalMin.toIntOrNull() ?: 0,
                normalMax = edit.spO2NormalMax.toIntOrNull() ?: 0,
                attentionMin = edit.spO2AttentionMin.toIntOrNull(),
                attentionMax = edit.spO2AttentionMax.toIntOrNull(),
                doctorSoonMin = null,
                doctorSoonMax = edit.spO2DoctorMax.toIntOrNull(),
            ),
            SingleMetricLimits(
                id = "custom-respiratory", ageGroup = AgeGroup.Custom,
                metricType = MetricType.RespiratoryRate,
                normalMin = edit.rrNormalMin.toIntOrNull() ?: 0,
                normalMax = edit.rrNormalMax.toIntOrNull() ?: 0,
                attentionMin = edit.rrAttentionMin.toIntOrNull(),
                attentionMax = edit.rrAttentionMax.toIntOrNull(),
                doctorSoonMin = null,
                doctorSoonMax = edit.rrDoctorMax.toIntOrNull(),
            ),
            SingleMetricLimits(
                id = "custom-inr", ageGroup = AgeGroup.Custom,
                metricType = MetricType.INR,
                normalMin = edit.inrNormalMin.inrToInt() ?: 0,
                normalMax = edit.inrNormalMax.inrToInt() ?: 0,
                attentionMin = edit.inrAttentionMin.inrToInt(),
                attentionMax = edit.inrAttentionMax.inrToInt(),
                doctorSoonMin = null,
                doctorSoonMax = edit.inrDoctorMax.inrToInt(),
            ),
        )
    }

    private fun buildCustomBpLimits(edit: CustomLimitsEditState): BloodPressureLimits {
        val sysMin = edit.bpSystolicMin.toIntOrNull() ?: 0
        val sysMax = edit.bpSystolicMax.toIntOrNull() ?: 0
        val diaMin = edit.bpDiastolicMin.toIntOrNull() ?: 0
        val diaMax = edit.bpDiastolicMax.toIntOrNull() ?: 0
        return BloodPressureLimits(
            id = "custom-blood-pressure", ageGroup = AgeGroup.Custom,
            normalSystolicMin = sysMin, normalSystolicMax = sysMax,
            normalDiastolicMin = diaMin, normalDiastolicMax = diaMax,
            doctorSoonSystolicLow = sysMin, doctorSoonSystolicHigh = sysMax,
            doctorSoonDiastolicLow = diaMin, doctorSoonDiastolicHigh = diaMax,
        )
    }
}
