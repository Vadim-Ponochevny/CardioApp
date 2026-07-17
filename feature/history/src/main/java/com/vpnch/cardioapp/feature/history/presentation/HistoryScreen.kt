package com.vpnch.cardioapp.feature.history.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.history.presentation.components.HealthRecordsHistoryTab
import com.vpnch.cardioapp.feature.history.presentation.components.StreakBanner
import com.vpnch.cardioapp.feature.history.presentation.components.VitaminsHistoryTab

private val SCREEN_BG_COLOR = Color(0xFFF6F6F6)
private val TAB_BG_COLOR = Color(0xFFF6F6F6)

private val CONTENT_HORIZONTAL_PADDING = 12.dp
private val CONTENT_TOP_PADDING = 12.dp
private val CONTENT_SPACING = 16.dp

private const val TITLE = "История"
private const val TAB_VITAMINS = "Витаминки"
private const val TAB_RECORDS = "Показатели"

private const val TAB_INDEX_VITAMINS = 0
private const val TAB_INDEX_RECORDS = 1

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onOpenRecordsForDate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(TAB_INDEX_RECORDS) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = CONTENT_HORIZONTAL_PADDING)
            .padding(top = CONTENT_TOP_PADDING)
            .background(SCREEN_BG_COLOR),
        verticalArrangement = Arrangement.spacedBy(CONTENT_SPACING),
    ) {
        Text(
            text = TITLE,
            style = CardioTheme.typography.screenTitle,
            color = CardioTheme.colors.textMain,
        )

        StreakBanner(streakDays = uiState.streakDays)

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = TAB_BG_COLOR,
            contentColor = CardioTheme.colors.textMain,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Tab(
                selected = selectedTabIndex == TAB_INDEX_VITAMINS,
                onClick = { selectedTabIndex = TAB_INDEX_VITAMINS },
                text = {
                    Text(
                        text = TAB_VITAMINS,
                        style = CardioTheme.typography.bodySmall,
                        color = if (selectedTabIndex == TAB_INDEX_VITAMINS)
                            CardioTheme.colors.primary
                        else
                            CardioTheme.colors.textMain,
                    )
                },
            )
            Tab(
                selected = selectedTabIndex == TAB_INDEX_RECORDS,
                onClick = { selectedTabIndex = TAB_INDEX_RECORDS },
                text = {
                    Text(
                        text = TAB_RECORDS,
                        style = CardioTheme.typography.bodySmall,
                        color = if (selectedTabIndex == TAB_INDEX_RECORDS)
                            CardioTheme.colors.primary
                        else
                            CardioTheme.colors.textMain,
                    )
                },
            )
        }

        when (selectedTabIndex) {
            TAB_INDEX_VITAMINS -> VitaminsHistoryTab(
                sections = uiState.vitaminSections,
                modifier = Modifier.fillMaxSize(),
            )
            TAB_INDEX_RECORDS -> HealthRecordsHistoryTab(
                sections = uiState.healthRecordSections,
                onOpenRecordsForDate = onOpenRecordsForDate,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@CardioPreview
@Composable
private fun HistoryScreenPreview() {
    HistoryScreen(
        uiState = HistoryUiState(
            isLoading = false,
            streakDays = 5,
            healthRecordSections = listOf(
                HealthRecordHistorySection(
                    dateKey = "2026-05-26",
                    dayLabel = "26",
                    monthLabel = "мая",
                    recordsCountLabel = "1 запись",
                    recordDots = listOf(RecordDotStatus.Ok),
                ),
            ),
            vitaminSections = listOf(
                VitaminHistorySection(
                    dateKey = "2026-05-26",
                    label = "26 мая",
                    vitamins = listOf(VitaminHistoryItem("1", "Витамин D")),
                ),
            ),
        ),
        onOpenRecordsForDate = {},
    )
}
