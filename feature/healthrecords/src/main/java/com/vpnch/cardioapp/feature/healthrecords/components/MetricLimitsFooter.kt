package com.vpnch.cardioapp.feature.healthrecords.components

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
import com.vpnch.cardioapp.feature.healthrecords.create.FieldWarning
import com.vpnch.cardioapp.feature.healthrecords.create.isCritical

private const val LIMIT_WARNING_MESSAGE =
    "Если замер не попал в эти ожидания — позвони доктору и он расскажет что делать."

@Composable
fun MetricLimitsFooter(
    modifier: Modifier = Modifier,
    showWarning: Boolean,
    warning: FieldWarning? = null,
) {
    if (!showWarning) return

    MetricLimitWarningCard(
        isCritical = warning?.isCritical == true,
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardioTheme.colors.onPrimary,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
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
        warning = FieldWarning.Critical,
    )
}
