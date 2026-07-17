package com.vpnch.cardioapp.feature.healthrecords.components.inputsections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.health.metrics.MetricStatus
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.healthrecords.components.MetricInputField
import com.vpnch.cardioapp.feature.healthrecords.components.MetricLimitsFooter
import com.vpnch.cardioapp.feature.healthrecords.components.rememberMetricWarningState

private const val LABEL_SYSTOLIC = "Верхнее"
private const val LABEL_DIASTOLIC = "Нижнее"

private val SECTION_SPACING = 16.dp
private val FOOTER_PADDING_TOP = 8.dp

@Composable
fun BloodPressureInputSection(
    modifier: Modifier = Modifier,
    systolic: String,
    diastolic: String,
    systolicPlaceholder: String,
    diastolicPlaceholder: String,
    warning: MetricStatus?,
    onSystolicChange: (String) -> Unit,
    onDiastolicChange: (String) -> Unit,
    forceShowWarning: Boolean = false,
) {
    val (warningVisible, triggerWarning) = rememberMetricWarningState(
        forceShowWarning = forceShowWarning,
        warning = warning,
        inputs = arrayOf(systolic, diastolic)
    )
    val diastolicFocusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SECTION_SPACING),
    ) {
        Text(
            text = LABEL_SYSTOLIC,
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
            text = LABEL_DIASTOLIC,
            style = CardioTheme.typography.bodyMedium,
            color = CardioTheme.colors.textMain,
        )
        MetricInputField(
            modifier = Modifier.focusRequester(diastolicFocusRequester),
            value = diastolic,
            onValueChange = onDiastolicChange,
            label = diastolicPlaceholder,
            onImeAction = {
                if (systolic.isNotBlank() && diastolic.isNotBlank()) {
                    triggerWarning()
                }
            },
        )
        MetricLimitsFooter(
            showWarning = warningVisible,
            warning = warning,
            modifier = Modifier.padding(top = FOOTER_PADDING_TOP),
        )
    }
}
