package com.vpnch.cardioapp.feature.healthrecords.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HealthMetricEditRoute(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HealthMetricEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    HealthMetricEditScreen(
        uiState = uiState,
        onBack = onBack,
        onSave = { viewModel.save(onSuccess = onSaved) },
        onSystolicChange = viewModel::onSystolicChange,
        onDiastolicChange = viewModel::onDiastolicChange,
        onSingleChange = viewModel::onSingleChange,
        modifier = modifier,
    )
}
