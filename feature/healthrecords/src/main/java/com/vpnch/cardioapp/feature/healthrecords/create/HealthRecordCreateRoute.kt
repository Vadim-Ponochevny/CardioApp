package com.vpnch.cardioapp.feature.healthrecords.create

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Сохранить как черновик?") },
        text = {
            Text("Незаполненные поля останутся пустыми — их можно заполнить потом.")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDiscard) {
                Text("Отмена")
            }
        },
    )
}
