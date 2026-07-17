package com.vpnch.cardioapp.feature.today.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.core.model.health.MISSING_METRIC_LABEL
import com.vpnch.cardioapp.feature.today.LatestHealthRecordSummary
import com.vpnch.cardioapp.feature.today.TodayMetricIcons
import com.vpnch.cardioapp.feature.today.TodayMetricItem

@Composable
fun HealthRecordsCard(
    latestRecord: LatestHealthRecordSummary?,
    onOpenLatestRecord: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardioTheme.colors.onPrimary,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, ),
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
                        OutOfNormBadge(isCritical = latestRecord.hasCritical)
                    }
                }
                if (latestRecord != null) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Открыть запись",
                        tint = CardioTheme.colors.textMain,
                        modifier = Modifier
                            .size(44.dp)
                            .clickable { onOpenLatestRecord(latestRecord.recordId) },
                    )
                }
            }

            Text(
                text = if (latestRecord != null) {
                    "Последняя запись в ${latestRecord.timeLabel}"
                } else {
                    "Записей пока нет"
                },
                style = CardioTheme.typography.navLabel,
                color = CardioTheme.colors.textSecondary,
            )

            Column(
                modifier = Modifier.padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)) {
                val metrics = latestRecord?.metrics ?: emptyMetricsPlaceholder()
                MetricGridRow(metrics[0], metrics[1])
                MetricGridRow(metrics[2], metrics[3])
            }
        }
    }
}

private fun emptyMetricsPlaceholder(): List<TodayMetricItem> = listOf(
    TodayMetricItem("Давление", MISSING_METRIC_LABEL, TodayMetricIcons.BLOOD_PRESSURE, false),
    TodayMetricItem("Дыхание", MISSING_METRIC_LABEL, TodayMetricIcons.RESPIRATORY, false),
    TodayMetricItem("Пульс", MISSING_METRIC_LABEL, TodayMetricIcons.HEART_RATE, false),
    TodayMetricItem("Кислород", MISSING_METRIC_LABEL, TodayMetricIcons.OXYGEN, false),
)

@Composable
private fun MetricGridRow(
    first: TodayMetricItem?,
    second: TodayMetricItem?,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MetricCell(
            metric = first,
            modifier = Modifier.weight(1f),
        )
        MetricCell(
            metric = second,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun MetricCell(
    metric: TodayMetricItem?,
    modifier: Modifier = Modifier,
) {
    if (metric == null) {
        Box(modifier = modifier)
        return
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            painter = painterResource(id = metric.iconRes),
            contentDescription = metric.title,
            modifier = Modifier.size(52.dp),
            // colorFilter = ColorFilter.tint(CardioTheme.colors.onPrimary) // если нужно перекрасить
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = metric.value,
                style = CardioTheme.typography.bodyMedium,
                color = CardioTheme.colors.textMain,
            )
            Text(
                text = metric.title,
                style = CardioTheme.typography.navLabel,
                color = CardioTheme.colors.textMain,
            )
        }
    }
}
