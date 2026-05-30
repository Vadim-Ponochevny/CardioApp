package com.vpnch.cardioapp.feature.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.domain.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.PatientRepository
import com.vpnch.cardioapp.core.domain.SurveyRepository
import com.vpnch.cardioapp.core.domain.VitaminRepository
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.model.HealthRecord
import com.vpnch.cardioapp.core.model.SurveyLink
import com.vpnch.cardioapp.core.model.Vitamin
import com.vpnch.cardioapp.core.model.VitaminIntake
import com.vpnch.cardioapp.core.model.VitaminIntakeSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TodayUiState(
    val isLoading: Boolean = true,
    val todayRecords: List<HealthRecord> = emptyList(),
    val vitaminIntakes: List<VitaminIntakeSummary> = emptyList(),
    val surveyLink: SurveyLink? = null,
)

@HiltViewModel
class TodayViewModel @Inject constructor(
    patientRepository: PatientRepository,
    private val vitaminRepository: VitaminRepository,
    healthRecordRepository: HealthRecordRepository,
    surveyRepository: SurveyRepository,
) : ViewModel() {
    private val today = currentDate()
    private val patient = patientRepository.observeCurrentPatient()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = combine(
        patient.flatMapLatest { currentPatient ->
            currentPatient?.let {
                healthRecordRepository.observeRecordsForDate(it.id, today)
            } ?: flowOf(emptyList())
        },
        patient.flatMapLatest { currentPatient ->
            currentPatient?.let {
                vitaminRepository.observeVitaminIntakes(it.id, today)
            } ?: flowOf(emptyList())
        },
        surveyRepository.observeActiveSurveyLink(),
    ) { records, vitamins, surveyLink ->
        TodayUiState(
            isLoading = false,
            todayRecords = records,
            vitaminIntakes = vitamins,
            surveyLink = surveyLink,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TodayUiState(),
    )

    fun setVitaminTaken(patientId: String, vitaminId: String, isTaken: Boolean) {
        viewModelScope.launch {
            vitaminRepository.setVitaminTaken(patientId, today, vitaminId, isTaken)
        }
    }

    private fun currentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }
}

@Composable
fun TodayRoute(
    onOpenTodayRecords: () -> Unit,
    onAddHealthRecord: () -> Unit,
    onOpenVitamins: () -> Unit,
    onOpenSurvey: (SurveyLink) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: TodayViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    TodayScreen(
        uiState = uiState,
        onOpenTodayRecords = onOpenTodayRecords,
        onAddHealthRecord = onAddHealthRecord,
        onOpenVitamins = onOpenVitamins,
        onOpenSurvey = onOpenSurvey,
        onVitaminCheckedChange = { summary, checked ->
            viewModel.setVitaminTaken(summary.vitamin.patientId, summary.vitamin.id, checked)
        },
        modifier = modifier,
    )
}

@Composable
fun TodayScreen(
    uiState: TodayUiState,
    onOpenTodayRecords: () -> Unit,
    onAddHealthRecord: () -> Unit,
    onOpenVitamins: () -> Unit,
    onOpenSurvey: (SurveyLink) -> Unit,
    onVitaminCheckedChange: (VitaminIntakeSummary, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Сегодня",
            style = MaterialTheme.typography.headlineMedium,
        )
        HealthRecordsCard(
            recordsCount = uiState.todayRecords.size,
            onOpenTodayRecords = onOpenTodayRecords,
            onAddHealthRecord = onAddHealthRecord,
        )
        VitaminsCard(
            vitaminIntakes = uiState.vitaminIntakes,
            onOpenVitamins = onOpenVitamins,
            onVitaminCheckedChange = onVitaminCheckedChange,
        )
        SurveyCard(
            surveyLink = uiState.surveyLink,
            onOpenSurvey = onOpenSurvey,
        )
    }
}

@Composable
private fun HealthRecordsCard(
    recordsCount: Int,
    onOpenTodayRecords: () -> Unit,
    onAddHealthRecord: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Показатели", style = MaterialTheme.typography.titleLarge)
            Text("Сегодня добавлено записей: $recordsCount")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onOpenTodayRecords) {
                    Text("Записи за сегодня")
                }
                Button(onClick = onAddHealthRecord) {
                    Text("Добавить запись")
                }
            }
        }
    }
}

@Composable
private fun VitaminsCard(
    vitaminIntakes: List<VitaminIntakeSummary>,
    onOpenVitamins: () -> Unit,
    onVitaminCheckedChange: (VitaminIntakeSummary, Boolean) -> Unit,
) {
    val takenCount = vitaminIntakes.count { it.intake?.isTaken == true }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Витаминки", style = MaterialTheme.typography.titleLarge)
            Text("$takenCount из ${vitaminIntakes.size}")
            vitaminIntakes.forEach { summary ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = summary.intake?.isTaken == true,
                        onCheckedChange = { onVitaminCheckedChange(summary, it) },
                    )
                    Text(summary.vitamin.name)
                }
            }
            Button(onClick = onOpenVitamins) {
                Text("Посмотреть витаминки")
            }
        }
    }
}

@Composable
private fun SurveyCard(
    surveyLink: SurveyLink?,
    onOpenSurvey: (SurveyLink) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Опрос", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                enabled = surveyLink != null,
                onClick = { surveyLink?.let(onOpenSurvey) },
            ) {
                Text("Пройти опрос")
            }
        }
    }
}

@CardioPreview
@Composable
private fun TodayScreenPreview() {
    TodayScreen(
        uiState = TodayUiState(
            isLoading = false,
            todayRecords = emptyList(),
            vitaminIntakes = listOf(
                VitaminIntakeSummary(
                    vitamin = Vitamin(
                        id = "vitamin-1",
                        patientId = "patient",
                        name = "Витаминка утром",
                        dose = "1 шт.",
                    ),
                    intake = VitaminIntake(
                        id = "intake-1",
                        intakeDayId = "day",
                        vitaminId = "vitamin-1",
                        isTaken = true,
                        takenAt = 0L,
                        updatedAt = 0L,
                    ),
                ),
                VitaminIntakeSummary(
                    vitamin = Vitamin(
                        id = "vitamin-2",
                        patientId = "patient",
                        name = "Витаминка вечером",
                        dose = "1 шт.",
                    ),
                    intake = null,
                ),
            ),
            surveyLink = SurveyLink(
                id = "survey",
                title = "Опрос",
                url = "https://forms.yandex.ru/",
                isActive = true,
                createdAt = 0L,
                updatedAt = 0L,
            ),
        ),
        onOpenTodayRecords = {},
        onAddHealthRecord = {},
        onOpenVitamins = {},
        onOpenSurvey = {},
        onVitaminCheckedChange = { _, _ -> },
    )
}
