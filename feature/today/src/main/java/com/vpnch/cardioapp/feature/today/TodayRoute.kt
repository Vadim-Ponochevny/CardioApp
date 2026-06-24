package com.vpnch.cardioapp.feature.today

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TodayRoute(
    onOpenLatestRecord: (String) -> Unit,
    onAddHealthRecord: () -> Unit,
    onOpenProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: TodayViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    TodayScreen(
        uiState = uiState,
        onOpenLatestRecord = onOpenLatestRecord,
        onAddHealthRecord = onAddHealthRecord,
        onOpenProfile = onOpenProfile,
        onOpenSurvey = {},
        onVitaminCheckedChange = { summary, checked ->
            viewModel.setVitaminTaken(summary.vitamin.patientId, summary.vitamin.id, checked)
        },
        modifier = modifier,
    )
}
