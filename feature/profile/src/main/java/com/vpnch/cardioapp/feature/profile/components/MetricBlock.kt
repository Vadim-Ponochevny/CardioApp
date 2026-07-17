package com.vpnch.cardioapp.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.health.metrics.MetricType
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

data class MetricDisplay(
    val metricType: MetricType,
    val title: String,
    val normalText: String,
    val attentionText: String?,
    val doctorText: String?,
)

@Composable
internal fun MetricBlock(
    metric: MetricDisplay,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CardioTheme.colors.onPrimary, ProfileCardShape)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(metric.title, style = CardioTheme.typography.cardTitle, color = CardioTheme.colors.textMain)
        Spacer(Modifier.height(4.dp))
        MetricChipRow(text = metric.normalText, color = NormalColor)
        if (metric.attentionText != null) {
            MetricChipRow(text = metric.attentionText, color = AttentionColor)
        }
        if (metric.doctorText != null) {
            MetricChipRow(text = metric.doctorText, color = DoctorColor)
        }
    }
}

@Composable
private fun MetricChipRow(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(36.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Text(
            text = text,
            style = CardioTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.75f),
            maxLines = 1,
        )
    }
}
