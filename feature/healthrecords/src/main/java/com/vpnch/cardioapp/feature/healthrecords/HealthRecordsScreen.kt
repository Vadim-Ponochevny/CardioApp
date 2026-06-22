package com.vpnch.cardioapp.feature.healthrecords

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.formatAsHealthMetric
import com.vpnch.cardioapp.core.model.formatBloodPressure
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.R
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        TopAppBar(
            title = { Text(uiState.title) },
            navigationIcon = {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(68.dp)
                        .background(
                            color = CardioTheme.colors.onPrimary,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.back),
                        contentDescription = null,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF6F6F6)
            ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            if (deleteError) {
                Text(
                    text = "Не удалось удалить запись. Попробуй ещё раз.",
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            if (uiState.records.isEmpty()) {
                Text(
                    text = "За этот день записей пока нет.",
                    modifier = Modifier.padding(top = 12.dp),
                    style = CardioTheme.typography.bodyLarge,
                    color = CardioTheme.colors.textSecondary
                )
                return@Column
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
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
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(36.dp), // Скругление углов 36
            colors = CardDefaults.cardColors(
                containerColor = CardioTheme.colors.onPrimary // Белый цвет из темы
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {
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
                        style = CardioTheme.typography.itemTitle,
                        color = CardioTheme.colors.textMain,
                    )

                    VerticalDivider(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .height(120.dp) // Увеличено на всю высоту показателей
                            .width(1.dp),
                    )

                    // Обновлённые метрики в новом стиле
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Давление
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = formatBloodPressure(record.systolicPressure, record.diastolicPressure),
                                style = CardioTheme.typography.bodyMedium,
                                color = CardioTheme.colors.textMain,
                            )
                            Text(
                                text = "Давление",
                                style = CardioTheme.typography.navLabel,
                                color = CardioTheme.colors.textSecondary,
                            )
                        }

                        // Дыхание
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = record.respiratoryRate.formatAsHealthMetric(),
                                style = CardioTheme.typography.bodyMedium,
                                color = CardioTheme.colors.textMain,
                            )
                            Text(
                                text = "Дыхание",
                                style = CardioTheme.typography.navLabel,
                                color = CardioTheme.colors.textSecondary,
                            )
                        }

                        // Пульс
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = record.heartRate.formatAsHealthMetric(),
                                style = CardioTheme.typography.bodyMedium,
                                color = CardioTheme.colors.textMain,
                            )
                            Text(
                                text = "Пульс",
                                style = CardioTheme.typography.navLabel,
                                color = CardioTheme.colors.textSecondary,
                            )
                        }

                        // Кислород
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = record.oxygenSaturation.formatAsHealthMetric(),
                                style = CardioTheme.typography.bodyMedium,
                                color = CardioTheme.colors.textMain,
                            )
                            Text(
                                text = "Кислород",
                                style = CardioTheme.typography.navLabel,
                                color = CardioTheme.colors.textSecondary,
                            )
                        }
                    }
                }

                HorizontalDivider()

                Button(
                    onClick = onOpenRecord,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CardioTheme.colors.primary,
                        contentColor = CardioTheme.colors.onPrimary
                    ),
                    shape = RoundedCornerShape(36.dp)
                ) {
                    Text(
                        text = "Открыть запись",
                        style = CardioTheme.typography.actionLabel,
                        color = CardioTheme.colors.onPrimary
                    )
                }

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(36.dp),
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Удалить запись",
                        style = CardioTheme.typography.actionLabel,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = {
                            Text(
                                text = "Подтверждение",
                                style = CardioTheme.typography.actionLabel,
                                color = CardioTheme.colors.textMain,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        },
                        text = {
                            Text(
                                text = "Вы действительно хотите удалить эту запись?",
                                style = CardioTheme.typography.bodyLarge,
                                color = CardioTheme.colors.textMain,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        },
                        confirmButton = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    onClick = { showDeleteDialog = false },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CardioTheme.colors.textDisabled,
                                        contentColor = CardioTheme.colors.textMain
                                    ),
                                    shape = RoundedCornerShape(24.dp)
                                ) {
                                    Text(
                                        text = "Отмена",
                                        style = CardioTheme.typography.bodyMedium,
                                        color = CardioTheme.colors.textMain
                                    )
                                }

                                Button(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    onClick = {
                                        onDeleteRecord()
                                        showDeleteDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CardioTheme.colors.primary,
                                        contentColor = CardioTheme.colors.onPrimary
                                    ),
                                    shape = RoundedCornerShape(24.dp)
                                ) {
                                    Text(
                                        text = "Удалить",
                                        style = CardioTheme.typography.bodyMedium,
                                        color = CardioTheme.colors.onPrimary
                                    )
                                }
                            }
                        },
                        containerColor = CardioTheme.colors.onPrimary,
                        shape = RoundedCornerShape(36.dp)
                    )
                }
            }
        }

        if (item.hasOutOfNorm) {
            MetricAlertBadge(
                isCritical = item.hasCritical,
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
