package com.vpnch.cardioapp.feature.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.core.ui.CardioPreview

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryScreen(
    uiState: HistoryUiState,
    onOpenRecordsForDate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(1) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("История") })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Витаминки") },
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Показатели") },
                )
            }

            when (selectedTabIndex) {
                0 -> VitaminsHistoryTab(
                    sections = uiState.vitaminSections,
                    modifier = Modifier.fillMaxSize(),
                )

                1 -> HealthRecordsHistoryTab(
                    sections = uiState.healthRecordSections,
                    onOpenRecordsForDate = onOpenRecordsForDate,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun HealthRecordsHistoryTab(
    sections: List<HealthRecordHistorySection>,
    onOpenRecordsForDate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (sections.isEmpty()) {
        EmptyHistoryMessage(
            text = "Пока нет записей показателей.",
            modifier = modifier,
        )
        return
    }

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(sections, key = { it.dateKey }) { section ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(section.label, style = MaterialTheme.typography.titleMedium)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenRecordsForDate(section.dateKey) },
                ) {
                    Text(
                        text = section.recordsCountLabel,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun VitaminsHistoryTab(
    sections: List<VitaminHistorySection>,
    modifier: Modifier = Modifier,
) {
    if (sections.isEmpty()) {
        EmptyHistoryMessage(
            text = "Пока нет отмеченных витаминок.",
            modifier = modifier,
        )
        return
    }

    LazyColumn(
        modifier = modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(sections, key = { it.dateKey }) { section ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = section.label,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(section.vitamins, key = { it.id }) { vitamin ->
                        VitaminHistoryCard(name = vitamin.name)
                    }
                }
            }
        }
    }
}

@Composable
private fun VitaminHistoryCard(name: String) {
    Card(
        modifier = Modifier
            .width(112.dp)
            .height(112.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = name,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun EmptyHistoryMessage(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@CardioPreview
@Composable
private fun HistoryRoutePreview() {
    HistoryScreen(
        uiState = HistoryUiState(
            isLoading = false,
            healthRecordSections = listOf(
                HealthRecordHistorySection("2026-05-26", "Сегодня", "1 запись"),
            ),
            vitaminSections = listOf(
                VitaminHistorySection(
                    dateKey = "2026-05-26",
                    label = "Сегодня",
                    vitamins = listOf(VitaminHistoryItem("1", "Витамин D")),
                ),
            ),
        ),
        onOpenRecordsForDate = {},
    )
}
