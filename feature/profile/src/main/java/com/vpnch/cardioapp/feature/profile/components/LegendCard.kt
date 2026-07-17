package com.vpnch.cardioapp.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

internal val NormalColor = Color(0xFFADEF74)
internal val AttentionColor = Color(0xFFFFF06C)
internal val DoctorColor = Color(0xFFFFB09A)

@Composable
internal fun LegendCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CardioTheme.colors.onPrimary, ProfileCardShape)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        LegendRow(NormalColor, "Норма")
        LegendRow(AttentionColor, "Обратить внимание")
        LegendRow(DoctorColor, "Обратиться к врачу")
    }
}

@Composable
private fun LegendRow(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(30.dp).background(color, CircleShape))
        Spacer(Modifier.width(12.dp))
        Text(label, style = CardioTheme.typography.bodySmall, color = CardioTheme.colors.textMain)
    }
}
