package com.vpnch.cardioapp.feature.healthrecords.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.health.metrics.MetricStatus
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.CardioTopBar
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.healthrecords.model.HealthMetricKind
import com.vpnch.cardioapp.feature.healthrecords.components.HealthRecordBottomBar
import com.vpnch.cardioapp.feature.healthrecords.components.inputsections.BloodPressureInputSection
import com.vpnch.cardioapp.feature.healthrecords.components.inputsections.SingleMetricInputSection
import com.vpnch.cardioapp.feature.healthrecords.ui.extensions.measurementInstruction

private const val BTN_SAVE = "Сохранить"
private const val MSG_LOAD_ERROR = "Не удалось загрузить запись."

private val SCREEN_HORIZONTAL_PADDING = 16.dp
private val SCREEN_TOP_PADDING = 16.dp
private val CONTENT_VERTICAL_SPACING = 16.dp
private val ERROR_PADDING = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HealthMetricEditScreen(
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
            .background(CardioTheme.colors.background),
    ) {
        CardioTopBar(title = uiState.title, onBack = onBack)

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
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = SCREEN_HORIZONTAL_PADDING)
                        .padding(top = SCREEN_TOP_PADDING),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Основной контент с прокруткой
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(CONTENT_VERTICAL_SPACING),
                    ) {
                        if (uiState.isBloodPressure) {
                            BloodPressureInputSection(
                                systolic = uiState.systolicInput,
                                diastolic = uiState.diastolicInput,
                                systolicPlaceholder = uiState.systolicPlaceholder,
                                diastolicPlaceholder = uiState.diastolicPlaceholder,
                                warning = uiState.bloodPressureWarning,
                                onSystolicChange = onSystolicChange,
                                onDiastolicChange = onDiastolicChange,
                                forceShowWarning = uiState.showWarningOnLoad,
                            )
                        } else {
                            SingleMetricInputSection(
                                value = uiState.singleInput,
                                placeholder = uiState.singlePlaceholder,
                                instruction = uiState.metricKind.measurementInstruction,
                                warning = uiState.singleWarning,
                                onValueChange = onSingleChange,
                                allowDecimal = uiState.isInr,
                                forceShowWarning = uiState.showWarningOnLoad,
                            )
                        }
                    }

                    HealthRecordBottomBar(
                        buttonLabel = BTN_SAVE,
                        canProceed = uiState.canSave,
                        isSaving = uiState.isSaving,
                        errorMessage = uiState.saveError,
                        onPrimaryAction = onSave,
                    )
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
            bloodPressureWarning = MetricStatus.DoctorSoon,
            canSave = true,
        ),
        onBack = {},
        onSave = {},
        onSystolicChange = {},
        onDiastolicChange = {},
        onSingleChange = {},
    )
}
