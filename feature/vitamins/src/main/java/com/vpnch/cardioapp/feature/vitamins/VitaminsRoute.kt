package com.vpnch.cardioapp.feature.vitamins

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.feature.vitamins.components.AddVitaminDialog

@Composable
fun VitaminsRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VitaminsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    VitaminsScreen(
        uiState = uiState,
        onBack = onBack,
        onAddClick = { showAddDialog = true },
        onDeleteVitamin = viewModel::deleteVitamin,
        modifier = modifier,
    )

    if (showAddDialog) {
        AddVitaminDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name ->
                viewModel.addVitamin(name)
                showAddDialog = false
            },
        )
    }
}
