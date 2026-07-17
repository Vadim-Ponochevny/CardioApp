package com.vpnch.cardioapp.feature.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.repository.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.core.domain.repository.VitaminRepository
import com.vpnch.cardioapp.core.model.health.limits.MetricLimitsBundle
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.feature.history.presentation.mapper.HistoryMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel @Inject constructor(
    patientRepository: PatientRepository,
    private val healthRecordRepository: HealthRecordRepository,
    vitaminRepository: VitaminRepository,
    private val historyMapper: HistoryMapper,
) : ViewModel() {

    private val patient = patientRepository.currentPatient
    private val limitsBundle = MutableStateFlow<MetricLimitsBundle?>(null)

    init {
        viewModelScope.launch {
            val patient = patientRepository.getCurrentPatient() ?: return@launch
            val ageGroup = if (patient.useCustomLimits) AgeGroup.Custom else patient.ageGroup
            limitsBundle.value = MetricLimitsBundle(
                singleLimits = healthRecordRepository
                    .getSingleMetricLimits(ageGroup)
                    .associateBy { it.metricType },
                bloodPressureLimits = healthRecordRepository.getBloodPressureLimits(ageGroup),
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = patient.flatMapLatest { currentPatient ->
        if (currentPatient == null) {
            flowOf(Pair(emptyList(), emptyList()))
        } else {
            combine(
                healthRecordRepository.observeAllRecords(currentPatient.id),
                vitaminRepository.observeTakenVitaminsHistory(currentPatient.id),
            ) { records, takenVitamins -> Pair(records, takenVitamins) }
        }
    }.combine(limitsBundle) { (records, takenVitamins), limits ->
        HistoryUiState(
            isLoading = false,
            streakDays = historyMapper.calculateRecordStreak(records),
            healthRecordSections = historyMapper.mapRecordsToSections(records, limits),
            vitaminSections = historyMapper.mapVitaminsToSections(takenVitamins),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState(),
    )
}
