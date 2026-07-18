package com.vpnch.cardioapp.feature.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.history.presentation.VitaminHistorySection

private const val MSG_EMPTY = "Пока нет отмеченных витаминок."

private val SECTION_SPACING = 16.dp
private val ITEM_SPACING = 8.dp
private val LABEL_START_PADDING = 4.dp

private val CARD_CORNER = 36.dp
private val CARD_PADDING_H = 20.dp
private val CARD_PADDING_V = 14.dp
private val CARD_SPACING = 12.dp
private val HISTORY_CARD_ALPHA = 0.65f

private val CHIP_CORNER = 20.dp
private val CHIP_PADDING_H = 10.dp
private val CHIP_PADDING_V = 8.dp
private val CHIP_ICON_SIZE = 16.dp
private val CHIP_ICON_SPACING = 4.dp

private val ColorTaken = Color(0xFF1DC44E)
private val ColorTakenBg = Color(0xFFE5F6EA)
private val ColorNotTaken = Color(0xFF8A6A00)
private val ColorNotTakenBg = Color(0xFFFFF8E1)

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
                Column(verticalArrangement = Arrangement.spacedBy(ITEM_SPACING)) {
                    section.vitamins.forEach { vitamin ->
                        VitaminHistoryItemRow(name = vitamin.name, isTaken = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun VitaminHistoryItemRow(
    name: String,
    isTaken: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(HISTORY_CARD_ALPHA)
            .clip(RoundedCornerShape(CARD_CORNER))
            .background(CardioTheme.colors.onPrimary)
            .padding(horizontal = CARD_PADDING_H, vertical = CARD_PADDING_V),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CARD_SPACING),
    ) {
        Text(
            text = name,
            style = CardioTheme.typography.navLabel,
            color = CardioTheme.colors.textMain,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        StaticVitaminChip(
            label = "Не принял",
            isActive = !isTaken,
            activeColor = ColorNotTaken,
            activeBgColor = ColorNotTakenBg,
        )
        StaticVitaminChip(
            label = "Принял",
            isActive = isTaken,
            activeColor = ColorTaken,
            activeBgColor = ColorTakenBg,
        )
    }
}

@Composable
private fun StaticVitaminChip(
    label: String,
    isActive: Boolean,
    activeColor: Color,
    activeBgColor: Color,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(CHIP_CORNER))
            .background(if (isActive) activeBgColor else CardioTheme.colors.background)
            .padding(horizontal = CHIP_PADDING_H, vertical = CHIP_PADDING_V),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CHIP_ICON_SPACING),
    ) {
        Text(
            text = label,
            style = CardioTheme.typography.navLabel.copy(fontSize = 16.sp),
            color = if (isActive) activeColor else CardioTheme.colors.textSecondary,
        )
        if (isActive) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = activeColor,
                modifier = Modifier.size(CHIP_ICON_SIZE),
            )
        }
    }
}
