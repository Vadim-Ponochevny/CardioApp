package com.vpnch.cardioapp.feature.history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.feature.history.presentation.HealthRecordHistorySection

private const val MSG_EMPTY = "Пока нет записей показателей."

private val SECTION_SPACING = 12.dp

@Composable
fun HealthRecordsHistoryTab(
    sections: List<HealthRecordHistorySection>,
    onOpenRecordsForDate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (sections.isEmpty()) {
        EmptyHistoryMessage(text = MSG_EMPTY, modifier = modifier)
        return
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(SECTION_SPACING),
        modifier = modifier,
    ) {
        items(sections, key = { it.dateKey }) { section ->
            HealthRecordDayCard(
                dayLabel = section.dayLabel,
                monthLabel = section.monthLabel,
                recordsCountLabel = section.recordsCountLabel,
                recordDots = section.recordDots,
                onClick = { onOpenRecordsForDate(section.dateKey) },
            )
        }
    }
}
