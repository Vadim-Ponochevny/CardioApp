package com.vpnch.cardioapp.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OnboardingRoute(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: OnboardingViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) onComplete()
    }

    OnboardingScreen(
        uiState = uiState,
        onPatientIdChange = viewModel::onPatientIdChange,
        onBirthDateChange = viewModel::onBirthDateChange,
        onContinue = viewModel::continueOnboarding,
        modifier = modifier,
    )
}
