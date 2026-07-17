package com.vpnch.cardioapp.feature.healthrecords.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val BOTTOM_BAR_PADDING_HORIZONTAL = 16.dp
private val BOTTOM_BAR_PADDING_VERTICAL = 12.dp
private val BOTTOM_BAR_SPACING = 12.dp
private val BUTTON_HEIGHT = 72.dp
private val BUTTON_CORNER_RADIUS = 36.dp
private val SPINNER_SIZE = 20.dp
private val SPINNER_STROKE_WIDTH = 2.dp

@Composable
fun HealthRecordBottomBar(
    modifier: Modifier = Modifier,
    buttonLabel: String,
    canProceed: Boolean,
    isSaving: Boolean,
    errorMessage: String? = null,
    progressLabel: String? = null,
    onPrimaryAction: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BOTTOM_BAR_PADDING_HORIZONTAL, vertical = BOTTOM_BAR_PADDING_VERTICAL),
        verticalArrangement = Arrangement.spacedBy(BOTTOM_BAR_SPACING),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (progressLabel != null) {
            Text(
                text = progressLabel,
                style = CardioTheme.typography.bodyLarge,
                color = CardioTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = CardioTheme.typography.bodyMedium,
                color = CardioTheme.colors.warningContainer,
                textAlign = TextAlign.Center,
            )
        }
        Button(
            onClick = onPrimaryAction,
            enabled = canProceed && !isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .height(BUTTON_HEIGHT),
            shape = RoundedCornerShape(BUTTON_CORNER_RADIUS),
            colors = ButtonDefaults.buttonColors(
                containerColor = CardioTheme.colors.primary,
                contentColor = CardioTheme.colors.onPrimary,
            ),
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(SPINNER_SIZE),
                    strokeWidth = SPINNER_STROKE_WIDTH,
                    color = CardioTheme.colors.onPrimary,
                )
            } else {
                Text(
                    text = buttonLabel,
                    style = CardioTheme.typography.buttonPrimary,
                )
            }
        }
    }
}
