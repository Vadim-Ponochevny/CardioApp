package com.vpnch.cardioapp.feature.healthrecords.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private const val LIMIT_WARNING_MESSAGE =
    "Если замер не попал в эти ожидания — позвони доктору и он расскажет что делать."

@Composable
fun MetricLimitsFooter(
    expectedRangeLabel: String?,
    showWarning: Boolean,
    modifier: Modifier = Modifier,
) {
    if (expectedRangeLabel == null && !showWarning) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (expectedRangeLabel != null) {
            Text(
                text = expectedRangeLabel,
                style = CardioTheme.typography.bodySmall,
                color = CardioTheme.colors.textSecondary,
            )
        }
        if (showWarning) {
            MetricLimitWarningCard()
        }
    }
}

@Composable
private fun MetricLimitWarningCard(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardioTheme.colors.warningContainer,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Добавляем вертикальный скролл
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MetricAlertBadge()
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
        expectedRangeLabel = "Ожидается от 110/70 до 125/85",
        showWarning = true,
    )
}
