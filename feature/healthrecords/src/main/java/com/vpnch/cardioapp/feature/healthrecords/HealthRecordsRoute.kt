package com.vpnch.cardioapp.feature.healthrecords

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HealthRecordsRoute(
    onOpenRecord: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HealthRecordsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var deleteError by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            if (event is HealthRecordsEvent.DeleteFailed) {
                deleteError = true
            }
        }
    }

    HealthRecordsScreen(
        uiState = uiState,
        deleteError = deleteError,
        onOpenRecord = onOpenRecord,
        onDeleteRecord = viewModel::deleteRecord,
        onBack = onBack,
        modifier = modifier,
    )
}