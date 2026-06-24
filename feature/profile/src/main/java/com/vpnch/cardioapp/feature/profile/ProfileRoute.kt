package com.vpnch.cardioapp.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileRoute(
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    ProfileScreen(
        uiState = uiState,
        onPatientIdChange = viewModel::onPatientIdChange,
        onBirthDateChange = viewModel::onBirthDateChange,
        onAgeGroupChange = viewModel::onAgeGroupChange,
        onTakesWarfarinChange = viewModel::onTakesWarfarinChange,
        onValveTypeChange = viewModel::onValveTypeChange,
        onUseCustomLimitsChange = viewModel::onUseCustomLimitsChange,
        onCustomLimitsChange = viewModel::onCustomLimitsChange,
        onSave = viewModel::saveProfile,
        modifier = modifier,
    )
}
