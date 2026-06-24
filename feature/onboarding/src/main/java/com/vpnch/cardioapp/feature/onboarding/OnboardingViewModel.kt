package com.vpnch.cardioapp.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.PatientRepository
import com.vpnch.cardioapp.core.model.AgeGroup
import com.vpnch.cardioapp.core.model.Patient
import com.vpnch.cardioapp.core.model.UserType
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
    val isComplete: Boolean = false,
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onPatientIdChange(value: String) = _uiState.update { it.copy(patientId = value) }

    fun onBirthDateChange(value: String) = _uiState.update { it.copy(birthDate = value) }

    fun continueOnboarding() {
        val state = _uiState.value
        if (state.patientId.isBlank() || state.birthDate.isBlank()) return
        viewModelScope.launch {
            patientRepository.savePatient(
                Patient(
                    id = state.patientId.trim(),
                    birthDate = state.birthDate.trim(),
                    ageGroup = AgeGroup.Age9To11,
                    userType = UserType.Parent,
                )
            )
            _uiState.update { it.copy(isComplete = true) }
        }
    }
}
