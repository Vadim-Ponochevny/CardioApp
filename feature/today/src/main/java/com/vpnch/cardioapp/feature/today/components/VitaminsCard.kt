package com.vpnch.cardioapp.feature.today.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vpnch.cardioapp.core.model.vitamins.VitaminIntakeSummary
import com.vpnch.cardioapp.core.ui.CardioDialog
import com.vpnch.cardioapp.core.ui.CardioDialogButton
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val SECTION_SPACING = 12.dp
private val ARROW_SIZE = 44.dp
private val ARROW_ICON_SIZE = 34.dp

private val CARD_CORNER = 36.dp
private val CARD_PADDING_H = 20.dp
private val CARD_PADDING_V = 16.dp
private val CARD_SPACING = 12.dp

private val TOGGLE_CORNER = 20.dp
private val TOGGLE_PADDING_H = 10.dp
private val TOGGLE_PADDING_V = 8.dp
private val TOGGLE_ICON_SIZE = 18.dp
private val TOGGLE_ICON_SPACING = 5.dp

private val ColorChecked = Color(0xFF1DC44E)
private val ColorCheckedBg = Color(0xFFE5F6EA)
private val ColorUnchecked = Color(0xFF8A6A00)
private val ColorUncheckedBg = Color(0xFFFFF8E1)

@Composable
fun VitaminsSection(
    vitaminIntakes: List<VitaminIntakeSummary>,
    onVitaminCheckedChange: (VitaminIntakeSummary, Boolean) -> Unit,
    onOpenVitamins: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SECTION_SPACING),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Витаминки",
                style = CardioTheme.typography.itemTitle,
                color = CardioTheme.colors.textMain,
            )
            IconButton(onClick = onOpenVitamins, modifier = Modifier.size(ARROW_SIZE)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Управление витаминками",
                    tint = CardioTheme.colors.textSecondary,
                    modifier = Modifier.size(ARROW_ICON_SIZE),
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(SECTION_SPACING)) {
            vitaminIntakes.forEach { summary ->
                VitaminItemCard(
                    summary = summary,
                    onCheckedChange = { checked -> onVitaminCheckedChange(summary, checked) },
                )
            }
        }
    }
}

@Composable
private fun VitaminItemCard(
    summary: VitaminIntakeSummary,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }
    var showCancelDialog by rememberSaveable { mutableStateOf(false) }
    val isChecked = summary.intake?.isTaken == true

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CARD_CORNER))
            .background(CardioTheme.colors.onPrimary)
            .padding(horizontal = CARD_PADDING_H, vertical = CARD_PADDING_V),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CARD_SPACING),
    ) {
        Text(
            text = summary.vitamin.name,
            style = CardioTheme.typography.navLabel,
            color = CardioTheme.colors.textMain,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        VitaminToggleChip(
            label = "Не принял",
            isActive = !isChecked,
            activeColor = ColorUnchecked,
            activeBgColor = ColorUncheckedBg,
            onClick = { if (isChecked) showCancelDialog = true },
        )

        VitaminToggleChip(
            label = "Принял",
            isActive = isChecked,
            activeColor = ColorChecked,
            activeBgColor = ColorCheckedBg,
            onClick = { if (!isChecked) showConfirmDialog = true },
        )
    }

    if (showConfirmDialog) {
        CardioDialog(
            title = summary.vitamin.name,
            message = "Подтвердить приём?",
            primaryButton = CardioDialogButton(
                label = "Да",
                containerColor = ColorChecked,
                contentColor = Color.White,
                onClick = { showConfirmDialog = false; onCheckedChange(true) },
            ),
            secondaryButton = CardioDialogButton(
                label = "Нет",
                containerColor = CardioTheme.colors.textDisabled,
                contentColor = CardioTheme.colors.textMain,
                onClick = { showConfirmDialog = false },
            ),
            onDismiss = { showConfirmDialog = false },
        )
    }

    if (showCancelDialog) {
        CardioDialog(
            title = summary.vitamin.name,
            message = "Отменить приём?",
            primaryButton = CardioDialogButton(
                label = "Отменить",
                containerColor = CardioTheme.colors.cardVitamins,
                contentColor = CardioTheme.colors.textMain,
                onClick = { showCancelDialog = false; onCheckedChange(false) },
            ),
            secondaryButton = CardioDialogButton(
                label = "Нет",
                containerColor = CardioTheme.colors.textDisabled,
                contentColor = CardioTheme.colors.textMain,
                onClick = { showCancelDialog = false },
            ),
            onDismiss = { showCancelDialog = false },
        )
    }
}

@Composable
private fun VitaminToggleChip(
    label: String,
    isActive: Boolean,
    activeColor: Color,
    activeBgColor: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(TOGGLE_CORNER))
            .background(if (isActive) activeBgColor else CardioTheme.colors.background)
            .clickable(onClick = onClick)
            .padding(horizontal = TOGGLE_PADDING_H, vertical = TOGGLE_PADDING_V),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TOGGLE_ICON_SPACING),
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
                modifier = Modifier.size(TOGGLE_ICON_SIZE),
            )
        }
    }
}
