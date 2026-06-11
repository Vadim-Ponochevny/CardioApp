package com.vpnch.cardioapp.feature.healthrecords

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.formatAsHealthMetric
import com.vpnch.cardioapp.core.model.formatBloodPressure
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.feature.healthrecords.components.MetricAlertBadge
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthRecordsScreen(
    uiState: HealthRecordsUiState,
    deleteError: Boolean,
    onOpenRecord: (String) -> Unit,
    onDeleteRecord: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(uiState.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            if (deleteError) {
                Text(
                    text = "Не удалось удалить запись. Попробуй ещё раз.",
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            if (uiState.records.isEmpty()) {
                Text(
                    text = "За этот день записей пока нет.",
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
                return@Column
            }

            LazyColumn(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.records, key = { it.record.id }) { item ->
                    HealthRecordListItem(
                        item = item,
                        onOpenRecord = { onOpenRecord(item.record.id) },
                        onDeleteRecord = { onDeleteRecord(item.record.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun HealthRecordListItem(
    item: HealthRecordListItem,
    onOpenRecord: () -> Unit,
    onDeleteRecord: () -> Unit,
) {
    val record = item.record
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = formatRecordTime(record.createdAt),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .height(96.dp)
                            .width(1.dp),
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Давление: ${formatBloodPressure(record.systolicPressure, record.diastolicPressure)}")
                        Text("Дыхание: ${record.respiratoryRate.formatAsHealthMetric()}")
                        Text("Пульс: ${record.heartRate.formatAsHealthMetric()}")
                        Text("Кислород: ${record.oxygenSaturation.formatAsHealthMetric()}")
                    }
                }

                HorizontalDivider()

                Button(
                    onClick = onOpenRecord,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Открыть запись")
                }

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Text("Удалить запись", modifier = Modifier.padding(start = 8.dp))
                }

                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text("Подтверждение") },
                        text = { Text("Вы действительно хотите удалить эту запись?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    onDeleteRecord()
                                    showDeleteDialog = false
                                },
                            ) {
                                Text("Удалить", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteDialog = false }) {
                                Text("Отмена")
                            }
                        },
                    )
                }
            }
        }

        if (item.hasOutOfNorm) {
            MetricAlertBadge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
            )
        }
    }
}

private fun formatRecordTime(timestamp: Long): String {
    return SimpleDateFormat("HH:mm", Locale("ru", "RU")).format(Date(timestamp))
}

@CardioPreview
@Composable
private fun HealthRecordsScreenPreview() {
    HealthRecordsScreen(
        uiState = HealthRecordsUiState(),
        deleteError = false,
        onOpenRecord = {},
        onDeleteRecord = {},
        onBack = {},
    )
}
