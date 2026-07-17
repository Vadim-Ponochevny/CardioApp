package com.vpnch.cardioapp.feature.healthrecords.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.CardioTopBar
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.healthrecords.detail.components.MetricCard
import com.vpnch.cardioapp.feature.healthrecords.detail.model.MetricItem
import com.vpnch.cardioapp.feature.healthrecords.model.HealthMetricKind

private const val TITLE_RECORD = "Запись"
private const val MSG_LOAD_ERROR = "Не удалось загрузить запись."

private val SCREEN_HORIZONTAL_PADDING = 16.dp
private val SCREEN_VERTICAL_PADDING = 8.dp
private val METRIC_CARD_SPACING = 12.dp
private val ERROR_PADDING = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthRecordDetailScreen(
    uiState: HealthRecordDetailUiState,
    onBack: () -> Unit,
    onEditMetric: (metricRouteKey: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CardioTheme.colors.background),
    ) {
        CardioTopBar(
            title = if (uiState.timeLabel.isNotEmpty()) {
                "$TITLE_RECORD ${uiState.timeLabel}"
            } else {
                TITLE_RECORD
            },
            onBack = onBack,
        )

        when {
            uiState.loadError -> {
                Text(
                    text = MSG_LOAD_ERROR,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(ERROR_PADDING),
                    color = MaterialTheme.colorScheme.error,
                )
            }
            uiState.isLoading -> Unit
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = SCREEN_VERTICAL_PADDING)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(METRIC_CARD_SPACING),
                ) {
                    uiState.metrics.forEach { metric ->
                        MetricCard(
                            metric = metric,
                            onEdit = { onEditMetric(metric.kind.routeKey) },
                            modifier = Modifier.padding(horizontal = SCREEN_HORIZONTAL_PADDING),
                        )
                    }
                }
            }
        }
    }
}

@CardioPreview
@Composable
private fun HealthRecordDetailScreenPreview() {
    HealthRecordDetailScreen(
        uiState = HealthRecordDetailUiState(
            isLoading = false,
            timeLabel = "17:00",
            metrics = listOf(
                MetricItem(HealthMetricKind.BloodPressure, "Давление", "120/80", false),
                MetricItem(HealthMetricKind.RespiratoryRate, "Дыхание", "18", true),
                MetricItem(HealthMetricKind.HeartRate, "Пульс", "90", false),
                MetricItem(HealthMetricKind.OxygenSaturation, "Кислород", "98", false),
            ),
        ),
        onBack = {},
        onEditMetric = {},
    )
}