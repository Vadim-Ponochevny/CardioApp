package com.vpnch.cardioapp.feature.today.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.health.MISSING_METRIC_LABEL
import com.vpnch.cardioapp.core.ui.HealthMetricSummaryRow
import com.vpnch.cardioapp.core.ui.MetricAlertBadge
import com.vpnch.cardioapp.core.ui.MetricDisplayItem
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.today.LatestHealthRecordSummary
import com.vpnch.cardioapp.feature.today.TodayMetricIcons
import com.vpnch.cardioapp.feature.today.TodayMetricItem

private val CARD_CORNER = 36.dp
private val CARD_PADDING = 16.dp
private val HEADER_BOTTOM_PADDING = 12.dp
private val METRICS_TOP_PADDING = 24.dp
private val METRICS_SPACING = 24.dp
private val ARROW_SIZE = 44.dp

@Composable
fun HealthRecordsCard(
    latestRecord: LatestHealthRecordSummary?,
    onOpenLatestRecord: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CARD_CORNER),
        colors = CardDefaults.cardColors(containerColor = CardioTheme.colors.onPrimary),
    ) {
        Column(modifier = Modifier.padding(CARD_PADDING)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = HEADER_BOTTOM_PADDING),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Показатели",
                        style = CardioTheme.typography.itemTitle,
                        color = CardioTheme.colors.textMain,
                    )
                    if (latestRecord?.hasOutOfNorm == true) {
                        MetricAlertBadge(isCritical = latestRecord.hasCritical)
                    }
                }
                if (latestRecord != null) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Открыть запись",
                        tint = CardioTheme.colors.textMain,
                        modifier = Modifier
                            .size(ARROW_SIZE)
                            .clickable { onOpenLatestRecord(latestRecord.recordId) },
                    )
                }
            }

            Text(
                text = if (latestRecord != null) "Последняя запись в ${latestRecord.timeLabel}"
                       else "Записей пока нет",
                style = CardioTheme.typography.navLabel,
                color = CardioTheme.colors.textSecondary,
            )

            val metrics = latestRecord?.metrics ?: emptyMetricsPlaceholder()
            HealthMetricSummaryRow(
                metrics = metrics.map { MetricDisplayItem(it.iconRes, it.value, it.title) },
                modifier = Modifier.padding(top = METRICS_TOP_PADDING),
            )
        }
    }
}

private fun emptyMetricsPlaceholder(): List<TodayMetricItem> = listOf(
    TodayMetricItem("Давление", MISSING_METRIC_LABEL, TodayMetricIcons.BLOOD_PRESSURE, false),
    TodayMetricItem("Дыхание", MISSING_METRIC_LABEL, TodayMetricIcons.RESPIRATORY, false),
    TodayMetricItem("Пульс", MISSING_METRIC_LABEL, TodayMetricIcons.HEART_RATE, false),
    TodayMetricItem("Кислород", MISSING_METRIC_LABEL, TodayMetricIcons.OXYGEN, false),
)
