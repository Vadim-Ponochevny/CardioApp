package com.vpnch.cardioapp.feature.healthrecords.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.feature.healthrecords.HealthMetricKind
import com.vpnch.cardioapp.feature.healthrecords.components.MetricAlertBadge

@Composable
fun HealthRecordDetailRoute(
    onBack: () -> Unit,
    onEditMetric: (metricRouteKey: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HealthRecordDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    HealthRecordDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onEditMetric = onEditMetric,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HealthRecordDetailScreen(
    uiState: HealthRecordDetailUiState,
    onBack: () -> Unit,
    onEditMetric: (metricRouteKey: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (uiState.timeLabel.isNotEmpty()) {
                            "Запись ${uiState.timeLabel}"
                        } else {
                            "Запись"
                        },
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when {
            uiState.loadError -> {
                Text(
                    text = "Не удалось загрузить запись.",
                    modifier = Modifier.padding(innerPadding).padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                )
            }

            uiState.isLoading -> Unit

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    uiState.metrics.forEach { metric ->
                        MetricCard(
                            metric = metric,
                            onEdit = { onEditMetric(metric.kind.routeKey) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    metric: MetricItem,
    onEdit: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(metric.title, style = MaterialTheme.typography.titleMedium)
                if (metric.isOutOfNorm) {
                    MetricAlertBadge(modifier = Modifier.padding(start = 8.dp))
                }
            }
            Text(
                text = metric.value,
                style = MaterialTheme.typography.headlineSmall,
            )
            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Изменить")
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
