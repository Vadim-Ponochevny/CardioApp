package com.vpnch.cardioapp.feature.healthrecords.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.CardioPreview

private const val TEXT_TITLE = "Ты молодец!"
private const val TEXT_SUBTITLE = "Запись сохранена"
private const val BTN_HOME = "На главный экран"

private val SCREEN_PADDING = 24.dp
private val SUBTITLE_TOP_PADDING = 12.dp
private val BUTTON_TOP_PADDING = 32.dp

@Composable
fun HealthRecordSuccessScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(SCREEN_PADDING),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = TEXT_TITLE,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            text = TEXT_SUBTITLE,
            modifier = Modifier.padding(top = SUBTITLE_TOP_PADDING),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BUTTON_TOP_PADDING),
        ) {
            Text(BTN_HOME)
        }
    }
}

@CardioPreview
@Composable
private fun HealthRecordSuccessScreenPreview() {
    HealthRecordSuccessScreen(onFinish = {})
}
