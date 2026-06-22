package com.vpnch.cardioapp.feature.healthrecords.create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

@Composable
fun HealthRecordCreateRoute(
    onExit: () -> Unit,
    onFinished: () -> Unit,
    onOpenHelp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: HealthRecordCreateViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    var showDraftExitDialog by rememberSaveable { mutableStateOf(false) }

    fun tryExit() {
        if (viewModel.shouldConfirmDraftOnExit()) {
            showDraftExitDialog = true
        } else {
            onExit()
        }
    }

    BackHandler {
        when {
            showDraftExitDialog -> showDraftExitDialog = false
            uiState.isSaved -> onFinished()
            viewModel.onToolbarBack() -> Unit
            else -> tryExit()
        }
    }

    if (showDraftExitDialog) {
        DraftExitDialog(
            onDismiss = { showDraftExitDialog = false },
            onDiscard = {
                showDraftExitDialog = false
                onExit()
            },
            onConfirm = {
                viewModel.saveDraftAndExit(
                    onSuccess = {
                        showDraftExitDialog = false
                        onExit()
                    },
                    onFailure = {
                        showDraftExitDialog = false
                    },
                )
            },
        )
    }

    when {
        uiState.isSaved && uiState.savedWithDoctorAlert -> {
            HealthRecordDoctorAlertScreen(
                onOpenHelp = onOpenHelp,
                onFinish = onFinished,
                modifier = modifier,
            )
        }

        uiState.isSaved -> {
            HealthRecordSuccessScreen(
                onFinish = onFinished,
                modifier = modifier,
            )
        }

        else -> {
            HealthRecordCreateScreen(
                uiState = uiState,
                onBack = {
                    if (!viewModel.onToolbarBack()) {
                        tryExit()
                    }
                },
                onPrimaryAction = viewModel::onPrimaryAction,
                onSystolicChange = viewModel::onSystolicChange,
                onDiastolicChange = viewModel::onDiastolicChange,
                onRespiratoryChange = viewModel::onRespiratoryChange,
                onHeartRateChange = viewModel::onHeartRateChange,
                onOxygenChange = viewModel::onOxygenChange,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun DraftExitDialog(
    onDismiss: () -> Unit,
    onDiscard: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(36.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardioTheme.colors.onPrimary
            )
        ) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Сохранить как черновик?",
                    style = CardioTheme.typography.actionLabel,
                    color = CardioTheme.colors.textMain,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Незаполненные поля останутся пустыми — их можно заполнить потом.",
                    style = CardioTheme.typography.bodySmall,
                    color = CardioTheme.colors.textMain,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Кнопка "Отмена"
                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        onClick = onDiscard,
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = CardioTheme.colors.textDisabled,
                            contentColor = CardioTheme.colors.textMain
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = "Отмена",
                            style = CardioTheme.typography.navLabel,
                            color = CardioTheme.colors.textMain,
                            maxLines = 1
                        )
                    }

                    // Кнопка "Сохранить"
                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        onClick = onConfirm,
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = CardioTheme.colors.primary,
                            contentColor = CardioTheme.colors.onPrimary
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = "Сохранить",
                            style = CardioTheme.typography.navLabel,
                            color = CardioTheme.colors.onPrimary,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
