package com.vpnch.cardioapp.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val DIALOG_CARD_CORNER_RADIUS = 36.dp
private val DIALOG_CONTENT_PADDING = 24.dp
private val DIALOG_CONTENT_SPACING = 16.dp
private val DIALOG_BUTTON_SPACING = 12.dp
private val DIALOG_BUTTON_HEIGHT = 48.dp
private val DIALOG_BUTTON_CORNER_RADIUS = 24.dp

data class CardioDialogButton(
    val label: String,
    val containerColor: Color,
    val contentColor: Color,
    val onClick: () -> Unit,
)

@Composable
fun CardioDialog(
    title: String,
    message: String,
    primaryButton: CardioDialogButton,
    secondaryButton: CardioDialogButton,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(DIALOG_CARD_CORNER_RADIUS),
            colors = CardDefaults.cardColors(containerColor = CardioTheme.colors.onPrimary),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(DIALOG_CONTENT_PADDING),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(DIALOG_CONTENT_SPACING),
            ) {
                Text(
                    text = title,
                    style = CardioTheme.typography.actionLabel,
                    color = CardioTheme.colors.textMain,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = message,
                    style = CardioTheme.typography.bodySmall,
                    color = CardioTheme.colors.textMain,
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DIALOG_BUTTON_SPACING),
                ) {
                    DialogButton(button = secondaryButton, modifier = Modifier.weight(1f))
                    DialogButton(button = primaryButton, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun DialogButton(button: CardioDialogButton, modifier: Modifier = Modifier) {
    TextButton(
        modifier = modifier.height(DIALOG_BUTTON_HEIGHT),
        onClick = button.onClick,
        colors = ButtonDefaults.textButtonColors(
            containerColor = button.containerColor,
            contentColor = button.contentColor,
        ),
        shape = RoundedCornerShape(DIALOG_BUTTON_CORNER_RADIUS),
    ) {
        Text(
            text = button.label,
            style = CardioTheme.typography.navLabel,
            color = button.contentColor,
            maxLines = 1,
        )
    }
}
