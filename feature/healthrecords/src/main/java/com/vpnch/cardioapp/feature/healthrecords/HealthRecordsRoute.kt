package com.vpnch.cardioapp.feature.healthrecords

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
fun HealthRecordsRoute(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("Показатели", style = MaterialTheme.typography.headlineMedium)
        Text("Здесь появится создание и история записей показателей.")
    }
}

@CardioPreview
@Composable
private fun HealthRecordsRoutePreview() {
    HealthRecordsRoute()
}
