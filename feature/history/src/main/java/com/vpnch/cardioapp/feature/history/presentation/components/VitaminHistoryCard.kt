package com.vpnch.cardioapp.feature.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val CARD_WIDTH = 140.dp
private val CARD_CORNER_RADIUS = 18.dp
private val CARD_HORIZONTAL_PADDING = 12.dp
private val CARD_VERTICAL_PADDING = 16.dp
private val CARD_CONTENT_SPACING = 8.dp

private val BADGE_SIZE = 36.dp
private val CHECK_ICON_SIZE = 20.dp
private val NOT_TAKEN_BORDER_WIDTH = 2.dp

private val ColorTakenBackground = Color(0xFFE5F6EA).copy(alpha = 0.6f)
private val ColorNotTakenBackground = Color(0xFFFEF9E6).copy(alpha = 0.6f)
private val ColorTakenBadge = Color(0xFF1DC44E).copy(alpha = 0.6f)
private val ColorNotTakenBorder = Color(0xFFFEC81A).copy(alpha = 0.6f)
private val ColorCheckIcon = Color.White.copy(alpha = 0.6f)

@Composable
fun VitaminHistoryCard(
    name: String,
    isTaken: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val cardColor = if (isTaken) ColorTakenBackground else ColorNotTakenBackground

    Card(
        modifier = modifier.width(CARD_WIDTH),
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CARD_HORIZONTAL_PADDING, vertical = CARD_VERTICAL_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(CARD_CONTENT_SPACING),
        ) {
            Box(
                modifier = Modifier
                    .size(BADGE_SIZE)
                    .clip(CircleShape)
                    .background(if (isTaken) ColorTakenBadge else Color.Transparent),
                contentAlignment = Alignment.Center,
            ) {
                if (isTaken) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(CHECK_ICON_SIZE),
                        tint = ColorCheckIcon,
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(BADGE_SIZE)
                            .border(
                                width = NOT_TAKEN_BORDER_WIDTH,
                                color = ColorNotTakenBorder,
                                shape = CircleShape,
                            ),
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
