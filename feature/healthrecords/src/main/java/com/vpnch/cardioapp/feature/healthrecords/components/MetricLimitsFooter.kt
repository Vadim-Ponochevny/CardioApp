package com.vpnch.cardioapp.feature.healthrecords.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.health.metrics.MetricStatus
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.MetricAlertBadge
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private const val LIMIT_WARNING_MESSAGE =
    "Если замер не попал в эти ожидания — позвони доктору и он расскажет что делать."

private val CARD_CORNER_RADIUS = 20.dp
private val CARD_CONTENT_PADDING = 16.dp
private val CARD_CONTENT_SPACING = 12.dp
private val ROW_SPACING = 12.dp

@Composable
fun MetricLimitsFooter(
    modifier: Modifier = Modifier,
    showWarning: Boolean,
    warning: MetricStatus? = null,
) {
    if (!showWarning) return

    MetricLimitWarningCard(
        isCritical = warning == MetricStatus.DoctorSoon,
        modifier = modifier,
    )
}

@Composable
private fun MetricLimitWarningCard(
    isCritical: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        colors = CardDefaults.cardColors(
            containerColor = CardioTheme.colors.onPrimary,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CARD_CONTENT_PADDING),
            verticalArrangement = Arrangement.spacedBy(CARD_CONTENT_SPACING),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ROW_SPACING),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MetricAlertBadge(isCritical = isCritical)
                Text(
                    text = LIMIT_WARNING_MESSAGE,
                    style = CardioTheme.typography.bodySmall,
                    color = CardioTheme.colors.textMain,
                )
            }
        }
    }
}

@CardioPreview
@Composable
private fun MetricLimitsFooterPreview() {
    MetricLimitsFooter(
        showWarning = true,
        warning = MetricStatus.DoctorSoon,
    )
}
