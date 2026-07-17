package com.vpnch.cardioapp.feature.healthrecords.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.CardioPreview

private const val TEXT_ALERT_MESSAGE =
    "Очень важно время от времени показать наши цифры доктору. Пришло время позвонить."
private const val BTN_OPEN_CONTACTS = "Открыть номера"
private const val BTN_HOME = "На главный экран"

private val SCREEN_CONTENT_PADDING = 24.dp
private val FIRST_BUTTON_TOP_PADDING = 32.dp
private val BUTTON_SPACING = 12.dp

@Composable
fun HealthRecordDoctorAlertScreen(
    onOpenHelp: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(SCREEN_CONTENT_PADDING),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = TEXT_ALERT_MESSAGE,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onOpenHelp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = FIRST_BUTTON_TOP_PADDING),
        ) {
            Text(BTN_OPEN_CONTACTS)
        }
        OutlinedButton(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BUTTON_SPACING),
        ) {
            Text(BTN_HOME)
        }
    }
}

@CardioPreview
@Composable
private fun HealthRecordDoctorAlertScreenPreview() {
    HealthRecordDoctorAlertScreen(
        onOpenHelp = {},
        onFinish = {},
    )
}
