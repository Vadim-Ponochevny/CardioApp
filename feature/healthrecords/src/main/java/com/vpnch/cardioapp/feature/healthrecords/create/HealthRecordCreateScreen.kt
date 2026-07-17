package com.vpnch.cardioapp.feature.healthrecords.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.health.metrics.MetricStatus
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.CardioTopBar
import com.vpnch.cardioapp.feature.healthrecords.model.HealthMetricKind
import com.vpnch.cardioapp.feature.healthrecords.create.model.HealthRecordCreatePage
import com.vpnch.cardioapp.feature.healthrecords.components.HealthRecordBottomBar
import com.vpnch.cardioapp.feature.healthrecords.components.inputsections.BloodPressureInputSection
import com.vpnch.cardioapp.feature.healthrecords.components.inputsections.SingleMetricInputSection
import com.vpnch.cardioapp.feature.healthrecords.ui.extensions.measurementInstruction

private val SCREEN_HORIZONTAL_PADDING = 16.dp
private val PAGE_TOP_PADDING = 24.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthRecordCreateScreen(
    modifier: Modifier = Modifier,
    uiState: HealthRecordCreateUiState,
    onBack: () -> Unit,
    onPrimaryAction: () -> Unit,
    onSystolicChange: (String) -> Unit,
    onDiastolicChange: (String) -> Unit,
    onRespiratoryChange: (String) -> Unit,
    onHeartRateChange: (String) -> Unit,
    onOxygenChange: (String) -> Unit,
    onInrChange: (String) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxSize()) {
        CardioTopBar(
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
            onInrChange = onInrChange,
            modifier = Modifier.weight(1f),
        )
        HealthRecordBottomBar(
            progressLabel = uiState.progressLabel,
            buttonLabel = uiState.primaryButtonLabel,
            canProceed = uiState.canProceed,
            isSaving = uiState.isSaving,
            errorMessage = uiState.saveError ?: uiState.loadError,
            onPrimaryAction = onPrimaryAction,
        )
    }
}

@Composable
private fun HealthRecordCreatePageContent(
    uiState: HealthRecordCreateUiState,
    onSystolicChange: (String) -> Unit,
    onDiastolicChange: (String) -> Unit,
    onRespiratoryChange: (String) -> Unit,
    onHeartRateChange: (String) -> Unit,
    onOxygenChange: (String) -> Unit,
    onInrChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SCREEN_HORIZONTAL_PADDING)
            .verticalScroll(rememberScrollState()),
    ) {
        val pageModifier = Modifier.padding(top = PAGE_TOP_PADDING)
        when (uiState.currentPage) {
            HealthRecordCreatePage.BloodPressure -> BloodPressureInputSection(
                systolic = uiState.systolicInput,
                diastolic = uiState.diastolicInput,
                systolicPlaceholder = uiState.systolicPlaceholder,
                diastolicPlaceholder = uiState.diastolicPlaceholder,
                warning = uiState.bloodPressureWarning,
                onSystolicChange = onSystolicChange,
                onDiastolicChange = onDiastolicChange,
                modifier = pageModifier,
            )
            HealthRecordCreatePage.RespiratoryRate -> SingleMetricInputSection(
                value = uiState.respiratoryInput,
                placeholder = uiState.metricPlaceholder,
                instruction = HealthMetricKind.RespiratoryRate.measurementInstruction,
                warning = uiState.respiratoryWarning,
                onValueChange = onRespiratoryChange,
                modifier = pageModifier,
            )
            HealthRecordCreatePage.HeartRate -> SingleMetricInputSection(
                value = uiState.heartRateInput,
                placeholder = uiState.metricPlaceholder,
                instruction = HealthMetricKind.HeartRate.measurementInstruction,
                warning = uiState.heartRateWarning,
                onValueChange = onHeartRateChange,
                modifier = pageModifier,
            )
            HealthRecordCreatePage.OxygenSaturation -> SingleMetricInputSection(
                value = uiState.oxygenInput,
                placeholder = uiState.metricPlaceholder,
                instruction = HealthMetricKind.OxygenSaturation.measurementInstruction,
                warning = uiState.oxygenWarning,
                onValueChange = onOxygenChange,
                modifier = pageModifier,
            )
            HealthRecordCreatePage.INR -> SingleMetricInputSection(
                value = uiState.inrInput,
                placeholder = uiState.metricPlaceholder,
                instruction = HealthMetricKind.INR.measurementInstruction,
                warning = uiState.inrWarning,
                onValueChange = onInrChange,
                allowDecimal = true,
                modifier = pageModifier,
            )
        }
    }
}

@CardioPreview
@Composable
private fun HealthRecordCreateScreenPreview() {
    HealthRecordCreateScreen(
        uiState = HealthRecordCreateUiState(
            currentPage = HealthRecordCreatePage.BloodPressure,
            systolicInput = "120",
            diastolicInput = "80",
            systolicPlaceholder = "117",
            diastolicPlaceholder = "77",
            bloodPressureWarning = MetricStatus.DoctorSoon,
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
