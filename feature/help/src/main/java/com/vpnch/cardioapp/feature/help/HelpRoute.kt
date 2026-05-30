package com.vpnch.cardioapp.feature.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.CardioPreview

@Composable
fun HelpRoute(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("Помощь", style = MaterialTheme.typography.headlineMedium)
        Text("Здесь будут телефоны и вопросы-ответы.")
    }
}

@CardioPreview
@Composable
private fun HelpRoutePreview() {
    HelpRoute()
}
