package com.vpnch.cardioapp.feature.today.components

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
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

@Composable
fun OutOfNormBadge(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(CardioTheme.colors.warningContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "!",
            color = CardioTheme.colors.textMain,
            style = CardioTheme.typography.navLabel,
        )
    }
}
