package com.vpnch.cardioapp.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.PatientRepository
import com.vpnch.cardioapp.core.model.AgeGroup
import com.vpnch.cardioapp.core.model.BloodPressureLimits
import com.vpnch.cardioapp.core.model.MetricType
import com.vpnch.cardioapp.core.model.Patient
import com.vpnch.cardioapp.core.model.SingleMetricLimits
import com.vpnch.cardioapp.core.model.UserType
import com.vpnch.cardioapp.core.model.ValveType
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
                customEdit(customLimits.singleMetricLimits, customLimits.bloodPressureLimits)
            } else {
                customEdit(limits.singleMetricLimits, limits.bloodPressureLimits)
            }
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
                    limitsForAgeGroup = limits,
                    customLimitsEdit = customEdit(limits.singleMetricLimits, limits.bloodPressureLimits),
                )
            }
        }
    }

    fun onTakesWarfarinChange(value: Boolean) = _uiState.update { it.copy(takesWarfarin = value) }

    fun onValveTypeChange(value: ValveType?) = _uiState.update { it.copy(valveType = value) }

    fun onUseCustomLimitsChange(value: Boolean) = _uiState.update { it.copy(useCustomLimits = value) }

    fun onCustomLimitsChange(edit: CustomLimitsEditState) = _uiState.update { it.copy(customLimitsEdit = edit) }

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
        val singleLimits = buildCustomSingleLimits(edit)
        val bpLimits = buildCustomBpLimits(edit)
        healthRecordRepository.upsertSingleMetricLimits(singleLimits)
        healthRecordRepository.upsertBloodPressureLimits(listOf(bpLimits))
    }

    private suspend fun loadLimitsFor(ageGroup: AgeGroup): AgeGroupLimitsUi {
        val single = healthRecordRepository.getSingleMetricLimits(ageGroup)
        val bp = healthRecordRepository.getBloodPressureLimits(ageGroup)
        return AgeGroupLimitsUi(single, bp)
    }

    private fun customEdit(
        singleLimits: List<SingleMetricLimits>,
        bp: BloodPressureLimits?,
    ): CustomLimitsEditState {
        val rr = singleLimits.find { it.metricType == MetricType.RespiratoryRate }
        val hr = singleLimits.find { it.metricType == MetricType.HeartRate }
        val spo2 = singleLimits.find { it.metricType == MetricType.OxygenSaturation }
        return CustomLimitsEditState(
            rrNormalMin = rr?.normalMin?.toString() ?: "",
            rrNormalMax = rr?.normalMax?.toString() ?: "",
            hrNormalMin = hr?.normalMin?.toString() ?: "",
            hrNormalMax = hr?.normalMax?.toString() ?: "",
            spO2NormalMin = spo2?.normalMin?.toString() ?: "",
            spO2NormalMax = spo2?.normalMax?.toString() ?: "",
            bpSystolicMin = bp?.normalSystolicMin?.toString() ?: "",
            bpSystolicMax = bp?.normalSystolicMax?.toString() ?: "",
            bpDiastolicMin = bp?.normalDiastolicMin?.toString() ?: "",
            bpDiastolicMax = bp?.normalDiastolicMax?.toString() ?: "",
        )
    }

    private fun buildCustomSingleLimits(edit: CustomLimitsEditState): List<SingleMetricLimits> {
        return listOf(
            SingleMetricLimits(
                id = "custom-respiratory",
                ageGroup = AgeGroup.Custom,
                metricType = MetricType.RespiratoryRate,
                normalMin = edit.rrNormalMin.toIntOrNull() ?: 0,
                normalMax = edit.rrNormalMax.toIntOrNull() ?: 0,
                attentionMin = null,
                attentionMax = null,
                doctorSoonMin = null,
                doctorSoonMax = null,
            ),
            SingleMetricLimits(
                id = "custom-heart-rate",
                ageGroup = AgeGroup.Custom,
                metricType = MetricType.HeartRate,
                normalMin = edit.hrNormalMin.toIntOrNull() ?: 0,
                normalMax = edit.hrNormalMax.toIntOrNull() ?: 0,
                attentionMin = null,
                attentionMax = null,
                doctorSoonMin = null,
                doctorSoonMax = null,
            ),
            SingleMetricLimits(
                id = "custom-oxygen",
                ageGroup = AgeGroup.Custom,
                metricType = MetricType.OxygenSaturation,
                normalMin = edit.spO2NormalMin.toIntOrNull() ?: 0,
                normalMax = edit.spO2NormalMax.toIntOrNull() ?: 0,
                attentionMin = null,
                attentionMax = null,
                doctorSoonMin = null,
                doctorSoonMax = null,
            ),
        )
    }

    private fun buildCustomBpLimits(edit: CustomLimitsEditState): BloodPressureLimits {
        val sysMin = edit.bpSystolicMin.toIntOrNull() ?: 0
        val sysMax = edit.bpSystolicMax.toIntOrNull() ?: 0
        val diaMin = edit.bpDiastolicMin.toIntOrNull() ?: 0
        val diaMax = edit.bpDiastolicMax.toIntOrNull() ?: 0
        return BloodPressureLimits(
            id = "custom-blood-pressure",
            ageGroup = AgeGroup.Custom,
            normalSystolicMin = sysMin,
            normalSystolicMax = sysMax,
            normalDiastolicMin = diaMin,
            normalDiastolicMax = diaMax,
            doctorSoonSystolicLow = sysMin - 10,
            doctorSoonSystolicHigh = sysMax + 10,
            doctorSoonDiastolicLow = diaMin - 10,
            doctorSoonDiastolicHigh = diaMax + 10,
        )
    }
}
