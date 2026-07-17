package com.vpnch.cardioapp.feature.healthrecords.components.inputsections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.health.metrics.MetricStatus
import com.vpnch.cardioapp.feature.healthrecords.components.MetricInputField
import com.vpnch.cardioapp.feature.healthrecords.components.MetricInstructionText
import com.vpnch.cardioapp.feature.healthrecords.components.MetricLimitsFooter
import com.vpnch.cardioapp.feature.healthrecords.components.rememberMetricWarningState

@Composable
fun SingleMetricInputSection(
    value: String,
    placeholder: String,
    instruction: String?,
    warning: MetricStatus?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    allowDecimal: Boolean = false,
    forceShowWarning: Boolean = false,
) {
    val (warningVisible, triggerWarning) =
        rememberMetricWarningState(forceShowWarning, warning, value)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MetricInputField(
            value = value,
            onValueChange = onValueChange,
            label = placeholder,
            keyboardType = if (allowDecimal) KeyboardType.Decimal else KeyboardType.Number,
            onImeAction = { if (value.isNotBlank()) triggerWarning() },
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
