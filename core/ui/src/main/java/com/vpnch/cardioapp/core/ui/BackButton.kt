package com.vpnch.cardioapp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val BACK_BUTTON_SIZE = 68.dp

@Composable
fun BackButton(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onBack,
        modifier = modifier
            .size(BACK_BUTTON_SIZE)
            .background(CardioTheme.colors.onPrimary, CircleShape),
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
            contentDescription = null,
        )
    }
}
