package com.vpnch.cardioapp.feature.healthrecords.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.healthrecords.HealthMetricKind
import com.vpnch.cardioapp.feature.healthrecords.components.MetricLimitsFooter
import com.vpnch.cardioapp.feature.healthrecords.create.FieldWarning
import com.vpnch.cardioapp.feature.healthrecords.create.components.MetricInputField

@Composable
fun HealthMetricEditRoute(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HealthMetricEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    HealthMetricEditScreen(
        uiState = uiState,
        onBack = onBack,
        onSave = { viewModel.save(onSuccess = onSaved) },
        onSystolicChange = viewModel::onSystolicChange,
        onDiastolicChange = viewModel::onDiastolicChange,
        onSingleChange = viewModel::onSingleChange,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HealthMetricEditScreen(
    uiState: HealthMetricEditUiState,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onSystolicChange: (String) -> Unit,
    onDiastolicChange: (String) -> Unit,
    onSingleChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(uiState.title) },
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

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        if (uiState.isBloodPressure) {
                            Text(
                                text = "Верхнее",
                                style = CardioTheme.typography.bodyMedium,
                                color = CardioTheme.colors.textMain,
                            )
                            MetricInputField(
                                value = uiState.systolicInput,
                                onValueChange = onSystolicChange,
                                label = "110",
                            )
                            Text(
                                text = "Нижнее",
                                style = CardioTheme.typography.bodyMedium,
                                color = CardioTheme.colors.textMain,
                            )
                            MetricInputField(
                                value = uiState.diastolicInput,
                                onValueChange = onDiastolicChange,
                                label = "70",
                            )
                        } else {
                            MetricInputField(
                                value = uiState.singleInput,
                                onValueChange = onSingleChange,
                                label = uiState.singleLabel,
                            )
                        }
                        MetricLimitsFooter(
                            expectedRangeLabel = uiState.expectedRangeLabel,
                            showWarning = if (uiState.isBloodPressure) {
                                uiState.bloodPressureWarning != null
                            } else {
                                uiState.singleWarning != null
                            },
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        if (uiState.saveError != null) {
                            Text(
                                text = uiState.saveError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                            )
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CardioTheme.colors.primary,      // Основной цвет
                                contentColor = CardioTheme.colors.onPrimary       // Цвет текста
                            ),
                            onClick = onSave,
                            enabled = uiState.canSave && !uiState.isSaving,
                            modifier = Modifier.fillMaxWidth().height(72.dp),
                        ) {
                            if (uiState.isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                )
                            } else {
                                Text(
                                    text = "Сохранить",
                                    style = CardioTheme.typography.buttonPrimary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@CardioPreview
@Composable
private fun HealthMetricEditScreenPreview() {
    HealthMetricEditScreen(
        uiState = HealthMetricEditUiState(
            metricKind = HealthMetricKind.BloodPressure,
            title = "Давление",
            isBloodPressure = true,
            systolicInput = "120",
            diastolicInput = "80",
            expectedRangeLabel = "Ожидается от 110/70 до 125/85",
            bloodPressureWarning = FieldWarning,
            canSave = true,
        ),
        onBack = {},
        onSave = {},
        onSystolicChange = {},
        onDiastolicChange = {},
        onSingleChange = {},
    )
}
