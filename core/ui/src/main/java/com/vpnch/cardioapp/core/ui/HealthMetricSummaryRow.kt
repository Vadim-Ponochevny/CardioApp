package com.vpnch.cardioapp.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val ICON_SIZE = 36.dp
private val ITEM_VERTICAL_SPACING = 4.dp
private val DIVIDER_PADDING = 8.dp
private val ColorDivider = Color(0xFFE0E0E0)

@Composable
fun HealthMetricSummaryRow(
    metrics: List<MetricDisplayItem>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.Top,
    ) {
        metrics.forEachIndexed { index, metric ->
            if (index > 0) {
                VerticalDivider(
                    color = ColorDivider,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = DIVIDER_PADDING),
                )
            }
            MetricCell(
                metric = metric,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun MetricCell(
    metric: MetricDisplayItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ITEM_VERTICAL_SPACING),
    ) {
        Image(
            painter = painterResource(id = metric.iconRes),
            contentDescription = metric.label,
            modifier = Modifier.size(ICON_SIZE),
        )
        Text(
            text = metric.value,
            style = CardioTheme.typography.navLabel,
            color = CardioTheme.colors.textMain,
            textAlign = TextAlign.Center,
        )
        Text(
            text = metric.label,
            style = CardioTheme.typography.navLabel.copy(fontSize = 14.sp),
            color = CardioTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}
