package com.vpnch.cardioapp.feature.healthrecords.create.components

import androidx.compose.runtime.Composable
import com.vpnch.cardioapp.core.ui.CardioDialog
import com.vpnch.cardioapp.core.ui.CardioDialogButton
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private const val DIALOG_TITLE = "Сохранить как черновик?"
private const val DIALOG_MESSAGE = "Незаполненные поля останутся пустыми — их можно заполнить потом."
private const val BTN_DISCARD = "Отмена"
private const val BTN_SAVE = "Сохранить"

@Composable
fun DraftExitDialog(
    onDismiss: () -> Unit,
    onDiscard: () -> Unit,
    onConfirm: () -> Unit,
) {
    CardioDialog(
        title = DIALOG_TITLE,
        message = DIALOG_MESSAGE,
        onDismiss = onDismiss,
        secondaryButton = CardioDialogButton(
            label = BTN_DISCARD,
            containerColor = CardioTheme.colors.textDisabled,
            contentColor = CardioTheme.colors.textMain,
            onClick = onDiscard,
        ),
        primaryButton = CardioDialogButton(
            label = BTN_SAVE,
            containerColor = CardioTheme.colors.primary,
            contentColor = CardioTheme.colors.onPrimary,
            onClick = onConfirm,
        ),
    )
}
