package com.vpnch.cardioapp.feature.healthrecords.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.healthrecords.R

private const val TEXT_ALERT_MESSAGE =
    "Очень важно время от времени показать наши цифры доктору. Пришло время позвонить."
private const val BTN_OPEN_CONTACTS = "Открыть номера"
private const val BTN_HOME = "На главный экран"

private val SCREEN_PADDING_H = 16.dp
private val SCREEN_PADDING_V = 24.dp
private val TEXT_PADDING_H = 32.dp
private val IMAGE_HEIGHT = 240.dp
private val BUTTON_HEIGHT = 72.dp
private val BUTTON_CORNER = 36.dp
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
            .padding(horizontal = SCREEN_PADDING_H)
            .padding(vertical = SCREEN_PADDING_V),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.doctor_warning),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IMAGE_HEIGHT),
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = TEXT_ALERT_MESSAGE,
                style = CardioTheme.typography.navLabel,
                color = CardioTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = TEXT_PADDING_H),
            )
        }

        Button(
            onClick = onOpenHelp,
            modifier = Modifier
                .fillMaxWidth()
                .height(BUTTON_HEIGHT),
            shape = RoundedCornerShape(BUTTON_CORNER),
            colors = ButtonDefaults.buttonColors(
                containerColor = CardioTheme.colors.primary,
                contentColor = CardioTheme.colors.onPrimary,
            ),
        ) {
            Text(
                text = BTN_OPEN_CONTACTS,
                style = CardioTheme.typography.actionLabel,
            )
        }
        Spacer(Modifier.height(BUTTON_SPACING))
        OutlinedButton(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(BUTTON_HEIGHT),
            shape = RoundedCornerShape(BUTTON_CORNER),
        ) {
            Text(
                text = BTN_HOME,
                style = CardioTheme.typography.actionLabel,
                color = CardioTheme.colors.textMain,
            )
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
