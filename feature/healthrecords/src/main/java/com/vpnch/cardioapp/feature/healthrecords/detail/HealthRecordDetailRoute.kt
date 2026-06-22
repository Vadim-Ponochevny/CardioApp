package com.vpnch.cardioapp.feature.healthrecords.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.R
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = if (uiState.timeLabel.isNotEmpty()) {
                        "Запись ${uiState.timeLabel}"
                    } else {
                        "Запись"
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(68.dp)
                        .background(
                            color = CardioTheme.colors.onPrimary,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.back),
                        contentDescription = null,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF6F6F6)
            )
        )

        when {
            uiState.loadError -> {
                Text(
                    text = "Не удалось загрузить запись.",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                )
            }
            uiState.isLoading -> Unit
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
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

    // Определяем единицы измерения на основе kind
    val unitText = when (metric.kind) {
        HealthMetricKind.RespiratoryRate -> "ЧД/мин"
        HealthMetricKind.HeartRate -> "УД/мин"
        HealthMetricKind.OxygenSaturation -> "%"
        else -> null
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(36.dp), // Увеличил скругление углов
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(metric.title, style = CardioTheme.typography.actionLabel)
                if (metric.isOutOfNorm) {
                    MetricAlertBadge(
                        isCritical = metric.isCritical,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = metric.value,
                    style = CardioTheme.typography.inputValue,
                )
                if (unitText != null) {
                    Text(
                        text = unitText,
                        style = CardioTheme.typography.bodyLarge,
                        color = CardioTheme.colors.textSecondary,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }

            // Кастомизированная кнопка
            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardioTheme.colors.primary,
                    contentColor = CardioTheme.colors.onPrimary
                ),
                shape = RoundedCornerShape(36.dp), // Скругленные углы кнопки
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Text(
                    text = "Изменить",
                    style = CardioTheme.typography.actionLabel
                )
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
