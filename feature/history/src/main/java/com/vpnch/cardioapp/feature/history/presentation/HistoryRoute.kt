package com.vpnch.cardioapp.feature.history.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HistoryRoute(
    onOpenRecordsForDate: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    HistoryScreen(
        uiState = uiState,
        onOpenRecordsForDate = onOpenRecordsForDate,
        modifier = modifier,
    )
}
