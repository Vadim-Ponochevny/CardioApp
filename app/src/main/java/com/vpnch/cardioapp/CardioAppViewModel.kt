package com.vpnch.cardioapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.core.model.patient.Patient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CardioAppViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
) : ViewModel() {

    private val _isProfileComplete = MutableStateFlow<Boolean?>(null)
    val isProfileComplete: StateFlow<Boolean?> = _isProfileComplete

    init {
        viewModelScope.launch {
            // Wait for DataStore to load, then keep observing changes
            val initial = patientRepository.getCurrentPatient()
            _isProfileComplete.value = initial.isComplete()
            patientRepository.currentPatient.collect { patient ->
                _isProfileComplete.value = patient.isComplete()
            }
        }
    }

    private fun Patient?.isComplete(): Boolean =
        this != null && id.isNotBlank() && birthDate.isNotBlank()
}
