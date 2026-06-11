package com.vpnch.cardioapp.feature.healthrecords

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.core.model.HealthRecord
import com.vpnch.cardioapp.core.model.formatAsHealthMetric
import com.vpnch.cardioapp.core.model.formatBloodPressure
import com.vpnch.cardioapp.core.ui.CardioPreview
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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