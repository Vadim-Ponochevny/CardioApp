package com.vpnch.cardioapp.feature.history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.history.presentation.VitaminHistorySection

private const val MSG_EMPTY = "Пока нет отмеченных витаминок."

private val SECTION_SPACING = 16.dp
private val ITEM_SPACING = 8.dp
private val CARD_SPACING = 8.dp
private val LABEL_START_PADDING = 4.dp

@Composable
fun VitaminsHistoryTab(
    sections: List<VitaminHistorySection>,
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
            Column(verticalArrangement = Arrangement.spacedBy(ITEM_SPACING)) {
                Text(
                    text = section.label,
                    style = CardioTheme.typography.navLabel,
                    color = CardioTheme.colors.textMain,
                    modifier = Modifier.padding(start = LABEL_START_PADDING),
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(CARD_SPACING),
                ) {
                    items(section.vitamins, key = { it.id }) { vitamin ->
                        VitaminHistoryCard(name = vitamin.name, isTaken = true)
                    }
                }
            }
        }
    }
}
