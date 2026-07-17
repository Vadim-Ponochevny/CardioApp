package com.vpnch.cardioapp.feature.healthrecords

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.CardioTopBar
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.healthrecords.components.HealthRecordListItemCard

private val ColorBackground = Color(0xFFF6F6F6)

private val CONTENT_HORIZONTAL_PADDING = 16.dp
private val CONTENT_TOP_PADDING = 8.dp
private val LIST_ITEMS_SPACING = 12.dp
private val LIST_BOTTOM_PADDING = 16.dp

private const val MSG_DELETE_ERROR = "Не удалось удалить запись. Попробуй ещё раз."
private const val MSG_EMPTY_LIST = "За этот день записей пока нет."

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
            .background(ColorBackground),
    ) {
        CardioTopBar(title = uiState.title, subtitle = uiState.dateLabel, onBack = onBack)

        Column(modifier = Modifier.fillMaxSize()) {
            if (deleteError) {
                Text(
                    text = MSG_DELETE_ERROR,
                    modifier = Modifier
                        .padding(horizontal = CONTENT_HORIZONTAL_PADDING)
                        .padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            if (uiState.records.isEmpty()) {
                Text(
                    text = MSG_EMPTY_LIST,
                    modifier = Modifier
                        .padding(horizontal = CONTENT_HORIZONTAL_PADDING)
                        .padding(top = 12.dp),
                    style = CardioTheme.typography.bodyLarge,
                    color = CardioTheme.colors.textSecondary,
                )
                return@Column
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(LIST_ITEMS_SPACING),
                contentPadding = PaddingValues(
                    start = CONTENT_HORIZONTAL_PADDING,
                    end = CONTENT_HORIZONTAL_PADDING,
                    top = CONTENT_TOP_PADDING,
                    bottom = LIST_BOTTOM_PADDING,
                ),
            ) {
                items(uiState.records, key = { it.record.id }) { item ->
                    HealthRecordListItemCard(
                        item = item,
                        onOpenRecord = { onOpenRecord(item.record.id) },
                        onDeleteRecord = { onDeleteRecord(item.record.id) },
                    )
                }
            }
        }
    }
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
