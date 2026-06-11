package com.vpnch.cardioapp.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

@Preview(showBackground = true)
annotation class CardioPreview

@Composable
fun CardioPreviewTheme(
    content: @Composable () -> Unit,
) {
    CardioTheme(content = content)
}
