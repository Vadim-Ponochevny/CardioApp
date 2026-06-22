package com.vpnch.cardioapp.feature.healthrecords.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

@Composable
fun MetricInstructionText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = CardioTheme.typography.bodySmall,
        color = CardioTheme.colors.textSecondary,
        modifier = modifier,
    )
}
