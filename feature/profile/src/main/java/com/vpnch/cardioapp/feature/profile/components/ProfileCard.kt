package com.vpnch.cardioapp.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

internal val ProfileCardShape = RoundedCornerShape(36.dp)

@Composable
internal fun ProfileCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CardioTheme.colors.onPrimary, ProfileCardShape)
            .padding(16.dp),
    ) { content() }
}
