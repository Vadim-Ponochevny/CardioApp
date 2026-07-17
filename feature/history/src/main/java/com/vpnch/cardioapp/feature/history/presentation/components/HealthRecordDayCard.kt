package com.vpnch.cardioapp.feature.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.history.presentation.RecordDotStatus

private val CARD_CORNER_RADIUS = 36.dp
private val CARD_CONTENT_PADDING = 20.dp

private val DATE_SECTION_WIDTH = 44.dp
private val DIVIDER_WIDTH = 1.dp
private val DIVIDER_VERTICAL_MARGIN = 4.dp
private val DIVIDER_HORIZONTAL_MARGIN = 16.dp

private val DOT_SIZE = 16.dp
private val DOT_SPACING = 6.dp
private val DOTS_TOP_SPACING = 6.dp

private val ARROW_ICON_SIZE = 24.dp

private const val MAX_DOTS_VISIBLE = 8

private val ColorDivider = Color(0xFFE0E0E0)
private val ColorDotOk = Color(0xFF2E9B3D)
private val ColorDotWarning = Color(0xFFFEC81A)
private val ColorDotCritical = Color(0xFFE53935)

@Composable
fun HealthRecordDayCard(
    dayLabel: String,
    monthLabel: String,
    recordsCountLabel: String,
    recordDots: List<RecordDotStatus>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        colors = CardDefaults.cardColors(containerColor = CardioTheme.colors.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CARD_CONTENT_PADDING),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Дата: число крупно + месяц мелко
            Column(
                modifier = Modifier.width(DATE_SECTION_WIDTH),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = dayLabel,
                    style = CardioTheme.typography.cardTitle,
                    color = CardioTheme.colors.textMain,
                )
                Text(
                    text = monthLabel,
                    style = CardioTheme.typography.navLabel,
                    color = CardioTheme.colors.textSecondary,
                )
            }

            // Вертикальная полоска
            Box(
                modifier = Modifier
                    .padding(horizontal = DIVIDER_HORIZONTAL_MARGIN)
                    .width(DIVIDER_WIDTH)
                    .height(48.dp)
                    .background(ColorDivider),
            )

            // Количество записей + точки
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = recordsCountLabel,
                    style = CardioTheme.typography.bodySmall,
                    color = CardioTheme.colors.textMain,
                )
                if (recordDots.isNotEmpty() && recordDots.size <= MAX_DOTS_VISIBLE) {
                    Row(
                        modifier = Modifier.padding(top = DOTS_TOP_SPACING),
                        horizontalArrangement = Arrangement.spacedBy(DOT_SPACING),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        recordDots.forEach { status ->
                            val color = when (status) {
                                RecordDotStatus.Ok -> ColorDotOk
                                RecordDotStatus.Warning -> ColorDotWarning
                                RecordDotStatus.Critical -> ColorDotCritical
                            }
                            Box(
                                modifier = Modifier
                                    .size(DOT_SIZE)
                                    .clip(CircleShape)
                                    .background(color),
                            )
                        }
                    }
                }
            }

            // Стрелка
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = CardioTheme.colors.textSecondary,
                modifier = Modifier.size(ARROW_ICON_SIZE),
            )
        }
    }
}
