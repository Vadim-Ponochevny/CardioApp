package com.vpnch.cardioapp.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.core.model.patient.Patient
import com.vpnch.cardioapp.core.model.patient.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val patientId: String = "",
    val birthDate: String = "",
    val currentStep: Int = 0,
    val isComplete: Boolean = false,
    val saveError: String? = null,
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onPatientIdChange(value: String) = _uiState.update { it.copy(patientId = value) }

    fun onBirthDateChange(value: String) = _uiState.update { it.copy(birthDate = value) }

    fun onBack() {
        val step = _uiState.value.currentStep
        if (step > 0) _uiState.update { it.copy(currentStep = step - 1) }
    }

    fun continueOnboarding() {
        val state = _uiState.value
        if (state.currentStep < 2) {
            _uiState.update { it.copy(currentStep = state.currentStep + 1) }
            return
        }
        if (state.patientId.isBlank() || state.birthDate.isBlank()) return
        viewModelScope.launch {
            runCatching {
                patientRepository.savePatient(
                    Patient(
                        id = state.patientId.trim(),
                        birthDate = state.birthDate.trim(),
                        ageGroup = AgeGroup.Age9To11,
                        userType = UserType.Parent,
                    )
                )
            }.onSuccess {
                _uiState.update { it.copy(isComplete = true) }
            }.onFailure {
                _uiState.update { s -> s.copy(saveError = "Не удалось сохранить. Попробуй ещё раз.") }
            }
        }
    }
}
