package com.vpnch.cardioapp.feature.vitamins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.core.domain.repository.VitaminRepository
import com.vpnch.cardioapp.core.model.vitamins.Vitamin
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class VitaminsViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val vitaminRepository: VitaminRepository,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = patientRepository.currentPatient
        .flatMapLatest { patient ->
            patient?.let { vitaminRepository.observeVitamins(it.id) } ?: flowOf(emptyList())
        }
        .map { VitaminsUiState(vitamins = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VitaminsUiState())

    fun addVitamin(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            val patient = patientRepository.getCurrentPatient() ?: return@launch
            vitaminRepository.saveVitamin(
                Vitamin(
                    id = UUID.randomUUID().toString(),
                    patientId = patient.id,
                    name = name.trim(),
                    dose = null,
                )
            )
        }
    }

    fun deleteVitamin(vitaminId: String) {
        viewModelScope.launch {
            vitaminRepository.deleteVitamin(vitaminId)
        }
    }
}
