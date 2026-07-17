package com.vpnch.cardioapp.feature.history.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val EMPTY_PADDING = 16.dp

@Composable
fun EmptyHistoryMessage(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(EMPTY_PADDING),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}
