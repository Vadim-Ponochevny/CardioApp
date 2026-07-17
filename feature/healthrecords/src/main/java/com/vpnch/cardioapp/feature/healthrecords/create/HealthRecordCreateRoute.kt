package com.vpnch.cardioapp.feature.healthrecords.create

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.feature.healthrecords.create.components.DraftExitDialog

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

    fun handleBack(): Boolean {
        return when {
            showDraftExitDialog -> {
                showDraftExitDialog = false
                true
            }
            uiState.isSaved -> {
                onFinished()
                true
            }
            else -> {
                val moved = viewModel.onToolbarBack()
                if (!moved) tryExit()
                true
            }
        }
    }

    BackHandler { handleBack() }

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
                    onFailure = { showDraftExitDialog = false }
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
                onBack = { handleBack() },
                onPrimaryAction = viewModel::onPrimaryAction,
                onSystolicChange = viewModel::onSystolicChange,
                onDiastolicChange = viewModel::onDiastolicChange,
                onRespiratoryChange = viewModel::onRespiratoryChange,
                onHeartRateChange = viewModel::onHeartRateChange,
                onOxygenChange = viewModel::onOxygenChange,
                onInrChange = viewModel::onInrChange,
                modifier = modifier,
            )
        }
    }
}