package com.vpnch.cardioapp.feature.profile.components.limits

import androidx.compose.foundation.background
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
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

internal val StepCardShape = RoundedCornerShape(36.dp)

@Composable
internal fun ZoneCard(
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color, StepCardShape)
            .padding(16.dp),
    ) {
        Text(
            text = title,
            style = CardioTheme.typography.cardTitle,
            color = Color.Black.copy(alpha = 0.75f),
        )
        Spacer(Modifier.height(12.dp))
        content()
    }
}
