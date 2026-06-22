package com.vpnch.cardioapp.feature.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp) // отступы только слева и справа
            .padding(top = 12.dp)
            .background(Color(0xFFF6F6F6)), // Добавляем фон
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

            Text(
                text = "История",
                style = CardioTheme.typography.screenTitle,
                color = CardioTheme.colors.textMain,
            )

        Spacer(modifier = Modifier.size(12.dp))

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color(0xFFF6F6F6), // Цвет фона TabRow
            contentColor = CardioTheme.colors.textMain
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = {
                    Text(
                        text = "Витаминки",
                        style = CardioTheme.typography.bodySmall,
                        color = if (selectedTabIndex == 0)
                            CardioTheme.colors.primary
                        else
                            CardioTheme.colors.textMain
                    )
                },
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = {
                    Text(
                        text = "Показатели",
                        style = CardioTheme.typography.bodySmall,
                        color = if (selectedTabIndex == 1)
                            CardioTheme.colors.primary
                        else
                            CardioTheme.colors.textMain
                    )
                },
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(sections, key = { it.dateKey }) { section ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = section.label,
                    style = CardioTheme.typography.navLabel,
                    color = CardioTheme.colors.textMain,
                    modifier = Modifier.padding(start = 4.dp)
                )

                // Переделанная карточка
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenRecordsForDate(section.dateKey) },
                    shape = RoundedCornerShape(36.dp), // Скругление углов 36
                    colors = CardDefaults.cardColors(
                        containerColor =  CardioTheme.colors.onPrimary // Белый фон
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp // Убираем тень
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = section.recordsCountLabel,
                            style = CardioTheme.typography.bodyLarge,
                            color = CardioTheme.colors.textMain
                        )

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = CardioTheme.colors.textMain,
                            modifier = Modifier.size(44.dp)
                        )
                    }
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(sections, key = { it.dateKey }) { section ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = section.label,
                    style = CardioTheme.typography.navLabel,
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(section.vitamins, key = { it.id }) { vitamin ->
                        VitaminHistoryCard(
                            name = vitamin.name,
                            isTaken = true // Все витамины в истории уже приняты
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VitaminHistoryCard(
    name: String,
    isTaken: Boolean = true,
) {
    val cardColor = if (isTaken) {
        Color(0xFFE5F6EA).copy(alpha = 0.6f) // Приглушённый зелёный
    } else {
        Color(0xFFFEF9E6).copy(alpha = 0.6f) // Приглушённый оранжевый
    }

    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Круг с галочкой (приглушённый)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isTaken) Color(0xFF1DC44E).copy(alpha = 0.6f)
                        else Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isTaken) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White.copy(alpha = 0.6f)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .border(
                                width = 2.dp,
                                color = Color(0xFFFEC81A).copy(alpha = 0.6f),
                                shape = CircleShape
                            )
                    )
                }
            }

            Text(
                text = name,
                style = CardioTheme.typography.bodySmall,
                color = CardioTheme.colors.textMain.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
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
                HealthRecordHistorySection("2026-05-26", "26 мая", "1 запись"),
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
