package com.vpnch.cardioapp.feature.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.PatientRepository
import com.vpnch.cardioapp.core.domain.VitaminRepository
import com.vpnch.cardioapp.feature.history.presentation.mapper.HistoryMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HistoryViewModel @Inject constructor(
    patientRepository: PatientRepository,
    healthRecordRepository: HealthRecordRepository,
    vitaminRepository: VitaminRepository,
    private val historyMapper: HistoryMapper,
) : ViewModel() {

    private val patient = patientRepository.observeCurrentPatient()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = patient.flatMapLatest { currentPatient ->
        if (currentPatient == null) {
            flowOf(HistoryUiState(isLoading = false))
        } else {
            combine(
                healthRecordRepository.observeAllRecords(currentPatient.id),
                vitaminRepository.observeTakenVitaminsHistory(currentPatient.id),
            ) { records, takenVitamins ->
                HistoryUiState(
                    isLoading = false,
                    healthRecordSections = historyMapper.mapRecordsToSections(records),
                    vitaminSections = historyMapper.mapVitaminsToSections(takenVitamins),
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState(),
    )
}
