package com.vpnch.cardioapp.feature.healthrecords.create

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.R
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.healthrecords.HealthMetricKind
import com.vpnch.cardioapp.feature.healthrecords.components.MetricInstructionText
import com.vpnch.cardioapp.feature.healthrecords.components.MetricLimitsFooter
import com.vpnch.cardioapp.feature.healthrecords.measurementInstruction
import com.vpnch.cardioapp.feature.healthrecords.create.components.MetricInputField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthRecordCreateScreen(
    uiState: HealthRecordCreateUiState,
    onBack: () -> Unit,
    onPrimaryAction: () -> Unit,
    onSystolicChange: (String) -> Unit,
    onDiastolicChange: (String) -> Unit,
    onRespiratoryChange: (String) -> Unit,
    onHeartRateChange: (String) -> Unit,
    onOxygenChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        HealthRecordCreateTopBar(
            title = uiState.currentTitle,
            onBack = onBack,
        )
        HealthRecordCreatePageContent(
            uiState = uiState,
            onSystolicChange = onSystolicChange,
            onDiastolicChange = onDiastolicChange,
            onRespiratoryChange = onRespiratoryChange,
            onHeartRateChange = onHeartRateChange,
            onOxygenChange = onOxygenChange,
            modifier = Modifier.weight(1f),
        )
        HealthRecordCreateBottomBar(
            progressLabel = uiState.progressLabel,
            primaryButtonLabel = uiState.primaryButtonLabel,
            canProceed = uiState.canProceed,
            isSaving = uiState.isSaving,
            saveError = uiState.saveError,
            loadError = uiState.loadError,
            onPrimaryAction = onPrimaryAction,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HealthRecordCreateTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                modifier = Modifier.padding(start = 8.dp) // Отступ для текста
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF6F6F6),      // Цвет фона
        ),
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
    )
}

@Composable
private fun HealthRecordCreatePageContent(
    uiState: HealthRecordCreateUiState,
    onSystolicChange: (String) -> Unit,
    onDiastolicChange: (String) -> Unit,
    onRespiratoryChange: (String) -> Unit,
    onHeartRateChange: (String) -> Unit,
    onOxygenChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        when (uiState.currentPage) {
            0 -> BloodPressurePage(
                systolic = uiState.systolicInput,
                diastolic = uiState.diastolicInput,
                systolicPlaceholder = uiState.systolicPlaceholder,
                diastolicPlaceholder = uiState.diastolicPlaceholder,
                warning = uiState.bloodPressureWarning,
                onSystolicChange = onSystolicChange,
                onDiastolicChange = onDiastolicChange,
            )

            1 -> SingleMetricPage(
                value = uiState.respiratoryInput,
                placeholder = uiState.metricPlaceholder,
                instruction = HealthMetricKind.RespiratoryRate.measurementInstruction,
                warning = uiState.respiratoryWarning,
                onValueChange = onRespiratoryChange,
            )

            2 -> SingleMetricPage(
                value = uiState.heartRateInput,
                placeholder = uiState.metricPlaceholder,
                instruction = HealthMetricKind.HeartRate.measurementInstruction,
                warning = uiState.heartRateWarning,
                onValueChange = onHeartRateChange,
            )

            3 -> SingleMetricPage(
                value = uiState.oxygenInput,
                placeholder = uiState.metricPlaceholder,
                instruction = HealthMetricKind.OxygenSaturation.measurementInstruction,
                warning = uiState.oxygenWarning,
                onValueChange = onOxygenChange,
            )
        }
    }
}

@Composable
private fun HealthRecordCreateBottomBar(
    progressLabel: String,
    primaryButtonLabel: String,
    canProceed: Boolean,
    isSaving: Boolean,
    saveError: String?,
    loadError: String?,
    onPrimaryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = progressLabel,
            style = CardioTheme.typography.bodyLarge,
            color = CardioTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
        val errorMessage = saveError ?: loadError
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = CardioTheme.typography.bodyMedium,
                color = CardioTheme.colors.warningContainer,
                textAlign = TextAlign.Center,
            )
        }
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = CardioTheme.colors.primary,      // Основной цвет
                contentColor = CardioTheme.colors.onPrimary       // Цвет текста
            ),
            onClick = onPrimaryAction,
            enabled = canProceed && !isSaving,
            modifier = Modifier.fillMaxWidth().height(72.dp),
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = primaryButtonLabel,
                    style = CardioTheme.typography.buttonPrimary,
                )
            }
        }
    }
}

@Composable
private fun BloodPressurePage(
    systolic: String,
    diastolic: String,
    systolicPlaceholder: String,
    diastolicPlaceholder: String,
    warning: FieldWarning?,
    onSystolicChange: (String) -> Unit,
    onDiastolicChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var warningVisible by remember { mutableStateOf(false) }
    val diastolicFocusRequester = remember { FocusRequester() }

    LaunchedEffect(systolic, diastolic) {
        warningVisible = false
    }

    fun submitBloodPressure() {
        if (systolic.isNotBlank() && diastolic.isNotBlank()) {
            warningVisible = warning != null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Верхнее",
            style = CardioTheme.typography.bodyMedium,
            color = CardioTheme.colors.textMain,
        )
        MetricInputField(
            value = systolic,
            onValueChange = onSystolicChange,
            label = systolicPlaceholder,
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
            value = diastolic,
            onValueChange = onDiastolicChange,
            label = diastolicPlaceholder,
            onImeAction = ::submitBloodPressure,
        )
        MetricLimitsFooter(
            showWarning = warningVisible,
            warning = warning,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun SingleMetricPage(
    value: String,
    placeholder: String,
    instruction: String?,
    warning: FieldWarning?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var warningVisible by remember { mutableStateOf(false) }
    LaunchedEffect(value) {
        warningVisible = false
    }

    fun submitValue() {
        if (value.isNotBlank()) {
            warningVisible = warning != null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MetricInputField(
            value = value,
            onValueChange = onValueChange,
            label = placeholder,
            onImeAction = ::submitValue,
        )
        if (instruction != null) {
            MetricInstructionText(text = instruction)
        }
        MetricLimitsFooter(
            showWarning = warningVisible,
            warning = warning,
        )
    }
}

@CardioPreview
@Composable
private fun HealthRecordCreateScreenPreview() {
    HealthRecordCreateScreen(
        uiState = HealthRecordCreateUiState(
            currentPage = 0,
            systolicInput = "120",
            diastolicInput = "80",
            systolicPlaceholder = "117",
            diastolicPlaceholder = "77",
            bloodPressureWarning = FieldWarning.Critical,
            canProceed = true,
        ),
        onBack = {},
        onPrimaryAction = {},
        onSystolicChange = {},
        onDiastolicChange = {},
        onRespiratoryChange = {},
        onHeartRateChange = {},
        onOxygenChange = {},
    )
}

