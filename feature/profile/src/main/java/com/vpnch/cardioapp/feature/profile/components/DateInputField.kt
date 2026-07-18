package com.vpnch.cardioapp.feature.profile.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val INPUT_HEIGHT = 60.dp

@Composable
internal fun DateInputField(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(INPUT_HEIGHT)
            .border(3.dp, CardioTheme.colors.textDisabled, ProfileCardShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = value.ifBlank { "ДД.ММ.ГГГГ" },
            style = CardioTheme.typography.bodySmall.copy(
                color = if (value.isBlank()) CardioTheme.colors.textDisabled else CardioTheme.colors.textMain,
            ),
        )
    }
}
