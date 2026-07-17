package com.vpnch.cardioapp.feature.profile

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onBack()
    }

    BackHandler {
        if (uiState.customLimitsStep != null) {
            viewModel.onCustomLimitsBack()
        } else {
            viewModel.saveProfile()
        }
    }

    if (uiState.customLimitsStep != null) {
        CustomLimitsStepScreen(
            uiState = uiState,
            onBack = viewModel::onCustomLimitsBack,
            onNext = viewModel::onCustomLimitsNext,
            onCustomLimitsChange = viewModel::onCustomLimitsChange,
        )
    } else {
        ProfileScreen(
            uiState = uiState,
            onBack = viewModel::saveProfile,
            onPatientIdChange = viewModel::onPatientIdChange,
            onBirthDateChange = viewModel::onBirthDateChange,
            onAgeGroupChange = viewModel::onAgeGroupChange,
            onTakesWarfarinChange = viewModel::onTakesWarfarinChange,
            onValveTypeChange = viewModel::onValveTypeChange,
            onOpenCustomLimits = viewModel::onOpenCustomLimits,
            onEditMetric = viewModel::onEditMetricDirectly,
            onNotificationsEnabledChange = viewModel::onNotificationsEnabledChange,
            modifier = modifier,
        )
    }
}
