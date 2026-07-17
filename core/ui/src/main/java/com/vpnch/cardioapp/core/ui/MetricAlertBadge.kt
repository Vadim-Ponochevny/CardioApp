package com.vpnch.cardioapp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val BADGE_SIZE = 36.dp
private val ColorNormBackground = Color(0xFFDCF5E0)
private val ColorNormCheck = Color(0xFF2E9B3D)

@Composable
fun MetricAlertBadge(
    modifier: Modifier = Modifier,
    isCritical: Boolean = false,
) {
    Box(
        modifier = modifier
            .size(BADGE_SIZE)
            .clip(CircleShape)
            .background(
                if (isCritical) CardioTheme.colors.cardSurvey else CardioTheme.colors.warningContainer,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "!",
            color = CardioTheme.colors.textMain,
            style = CardioTheme.typography.navLabel,
        )
    }
}

@Composable
fun MetricNormBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(BADGE_SIZE)
            .clip(CircleShape)
            .background(ColorNormBackground),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = ColorNormCheck,
            modifier = Modifier.size(20.dp),
        )
    }
}
