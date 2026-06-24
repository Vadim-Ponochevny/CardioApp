package com.vpnch.cardioapp

import androidx.lifecycle.ViewModel
import com.vpnch.cardioapp.core.domain.PatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class CardioAppViewModel @Inject constructor(
    patientRepository: PatientRepository,
) : ViewModel() {
    val isProfileComplete: Flow<Boolean> = patientRepository.isProfileComplete()
}
