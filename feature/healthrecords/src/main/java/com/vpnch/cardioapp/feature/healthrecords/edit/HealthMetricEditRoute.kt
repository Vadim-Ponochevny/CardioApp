package com.vpnch.cardioapp.feature.healthrecords.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.R
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.healthrecords.HealthMetricKind
import com.vpnch.cardioapp.feature.healthrecords.components.MetricInstructionText
import com.vpnch.cardioapp.feature.healthrecords.components.MetricLimitsFooter
import com.vpnch.cardioapp.feature.healthrecords.measurementInstruction
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        TopAppBar(
            title = { Text(
                text = uiState.title,
                modifier = Modifier.padding(start = 8.dp)
            ) },
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
//                    tint = MaterialTheme.colorScheme.onSurfaceVariant
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
            else -> {
                val warning = if (uiState.isBloodPressure) {
                    uiState.bloodPressureWarning
                } else {
                    uiState.singleWarning
                }
                var warningVisible by remember { mutableStateOf(false) }
                val diastolicFocusRequester = remember { FocusRequester() }

                LaunchedEffect(
                    uiState.systolicInput,
                    uiState.diastolicInput,
                    uiState.singleInput,
                    uiState.isBloodPressure,
                ) {
                    warningVisible = false
                }

                fun submitBloodPressure() {
                    if (uiState.systolicInput.isNotBlank() && uiState.diastolicInput.isNotBlank()) {
                        warningVisible = warning != null
                    }
                }

                fun submitSingleMetric() {
                    if (uiState.singleInput.isNotBlank()) {
                        warningVisible = warning != null
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
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
                                label = uiState.systolicPlaceholder,
                                imeAction = ImeAction.Next,
                                onImeAction = { diastolicFocusRequester.requestFocus() },
                            )
                            Text(
                                text = "Нижнее",
                                style = CardioTheme.typography.bodyMedium,
                                color = CardioTheme.colors.textMain,
                            )
                            MetricInputField(
                                modifier = Modifier.focusRequester(diastolicFocusRequester),
                                value = uiState.diastolicInput,
                                onValueChange = onDiastolicChange,
                                label = uiState.diastolicPlaceholder,
                                onImeAction = ::submitBloodPressure,
                            )
                        } else {
                            MetricInputField(
                                value = uiState.singleInput,
                                onValueChange = onSingleChange,
                                label = uiState.singlePlaceholder,
                                onImeAction = ::submitSingleMetric,
                            )
                            uiState.metricKind.measurementInstruction?.let { instruction ->
                                MetricInstructionText(text = instruction)
                            }
                        }
                        MetricLimitsFooter(
                            showWarning = warningVisible,
                            warning = warning,
                        )
//
//                        // Добавляем Spacer для отступа перед кнопкой
//                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
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
                                containerColor = CardioTheme.colors.primary,
                                contentColor = CardioTheme.colors.onPrimary
                            ),
                            onClick = onSave,
                            enabled = uiState.canSave && !uiState.isSaving,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp),
                            shape = RoundedCornerShape(36.dp)
                        ) {
                            if (uiState.isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = CardioTheme.colors.onPrimary
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
            systolicPlaceholder = "117",
            diastolicPlaceholder = "77",
            bloodPressureWarning = FieldWarning.Critical,
            canSave = true,
        ),
        onBack = {},
        onSave = {},
        onSystolicChange = {},
        onDiastolicChange = {},
        onSingleChange = {},
    )
}
