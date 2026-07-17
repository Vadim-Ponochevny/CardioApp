package com.vpnch.cardioapp.feature.healthrecords.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HealthRecordDetailRoute(
    onBack: () -> Unit,
    onEditMetric: (metricRouteKey: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HealthRecordDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    HealthRecordDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onEditMetric = onEditMetric,
        modifier = modifier,
    )
}
