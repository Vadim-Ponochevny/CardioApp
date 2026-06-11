package com.vpnch.cardioapp.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.PatientRepository
import com.vpnch.cardioapp.core.domain.VitaminRepository
import com.vpnch.cardioapp.core.model.currentDateKey
import com.vpnch.cardioapp.core.model.formatDaySectionLabel
import com.vpnch.cardioapp.core.model.formatRecordsCount
import com.vpnch.cardioapp.core.model.timestampToDateKey
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
) : ViewModel() {

    private val referenceDateKey = currentDateKey()
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
                    healthRecordSections = records.toHealthRecordSections(referenceDateKey),
                    vitaminSections = takenVitamins.toVitaminSections(referenceDateKey),
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState(),
    )
}

data class HistoryUiState(
    val isLoading: Boolean = true,
    val healthRecordSections: List<HealthRecordHistorySection> = emptyList(),
    val vitaminSections: List<VitaminHistorySection> = emptyList(),
)

data class HealthRecordHistorySection(
    val dateKey: String,
    val label: String,
    val recordsCountLabel: String,
)

data class VitaminHistorySection(
    val dateKey: String,
    val label: String,
    val vitamins: List<VitaminHistoryItem>,
)

data class VitaminHistoryItem(
    val id: String,
    val name: String,
)

private fun List<com.vpnch.cardioapp.core.model.HealthRecord>.toHealthRecordSections(
    referenceDateKey: String,
): List<HealthRecordHistorySection> {
    return groupBy { timestampToDateKey(it.createdAt) }
        .map { (dateKey, records) ->
            HealthRecordHistorySection(
                dateKey = dateKey,
                label = formatDaySectionLabel(dateKey, referenceDateKey),
                recordsCountLabel = formatRecordsCount(records.size),
            )
        }
        .sortedByDescending { it.dateKey }
}

private fun List<com.vpnch.cardioapp.core.model.TakenVitaminOnDate>.toVitaminSections(
    referenceDateKey: String,
): List<VitaminHistorySection> {
    return groupBy { it.date }
        .map { (dateKey, items) ->
            VitaminHistorySection(
                dateKey = dateKey,
                label = formatDaySectionLabel(dateKey, referenceDateKey),
                vitamins = items.map { VitaminHistoryItem(it.vitaminId, it.vitaminName) },
            )
        }
        .sortedByDescending { it.dateKey }
}
