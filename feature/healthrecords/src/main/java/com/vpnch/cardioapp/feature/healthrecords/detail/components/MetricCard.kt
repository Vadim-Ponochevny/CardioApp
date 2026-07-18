package com.vpnch.cardioapp.feature.healthrecords.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.MetricAlertBadge
import com.vpnch.cardioapp.core.ui.R as CoreUiR
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.healthrecords.detail.model.MetricItem
import com.vpnch.cardioapp.feature.healthrecords.ui.extensions.iconRes
import com.vpnch.cardioapp.feature.healthrecords.ui.extensions.unitLabel

private const val BTN_EDIT = "Изменить"

private val CARD_CORNER_RADIUS = 36.dp
private val CARD_CONTENT_PADDING = 16.dp
private val CARD_CONTENT_SPACING = 8.dp
private val BADGE_START_PADDING = 8.dp
private val UNIT_START_PADDING = 12.dp
private val BUTTON_CORNER_RADIUS = 36.dp
private val BUTTON_PRESSED_ELEVATION = 2.dp
private val METRIC_ICON_SIZE = 44.dp
private val EDIT_ICON_SIZE = 36.dp
private val ICON_TITLE_SPACING = 8.dp
private val EDIT_ICON_SPACING = 6.dp

private val ColorButtonGrey = Color(0xFFF0F0F0)
private val ColorButtonGreyContent = Color(0xFF616161)

@Composable
fun MetricCard(
    metric: MetricItem,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        colors = CardDefaults.cardColors(containerColor = CardioTheme.colors.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(CARD_CONTENT_PADDING),
            verticalArrangement = Arrangement.spacedBy(CARD_CONTENT_SPACING),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(metric.kind.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(METRIC_ICON_SIZE),
                )
                Spacer(modifier = Modifier.width(ICON_TITLE_SPACING))
                Text(metric.title, style = CardioTheme.typography.actionLabel)
                if (metric.isOutOfNorm) {
                    MetricAlertBadge(
                        isCritical = metric.isCritical,
                        modifier = Modifier.padding(start = BADGE_START_PADDING),
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = metric.value,
                    style = CardioTheme.typography.inputValue,
                )
                metric.kind.unitLabel?.let { unit ->
                    Text(
                        text = unit,
                        style = CardioTheme.typography.bodyLarge,
                        color = CardioTheme.colors.textSecondary,
                        modifier = Modifier.padding(start = UNIT_START_PADDING),
                    )
                }
            }
            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorButtonGrey,
                    contentColor = ColorButtonGreyContent,
                ),
                shape = RoundedCornerShape(BUTTON_CORNER_RADIUS),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = BUTTON_PRESSED_ELEVATION,
                ),
            ) {
                Text(
                    text = BTN_EDIT,
                    style = CardioTheme.typography.actionLabel,
                    color = CardioTheme.colors.textMain
                )
                Spacer(modifier = Modifier.width(EDIT_ICON_SPACING))
                Icon(
                    imageVector = ImageVector.vectorResource(CoreUiR.drawable.ic_edit),
                    contentDescription = null,
                    modifier = Modifier.size(EDIT_ICON_SIZE),
                    tint = CardioTheme.colors.textMain,
                )
            }
        }
    }
}
