package com.vpnch.cardioapp.feature.healthrecords.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

@Composable
fun MetricAlertBadge(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(CardioTheme.colors.cardSurvey),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "!",
            color = CardioTheme.colors.onPrimary,
            style = CardioTheme.typography.navLabel,
        )
    }
}

@CardioPreview
@Composable
private fun MetricAlertBadgePreview() {
    MetricAlertBadge()
}
