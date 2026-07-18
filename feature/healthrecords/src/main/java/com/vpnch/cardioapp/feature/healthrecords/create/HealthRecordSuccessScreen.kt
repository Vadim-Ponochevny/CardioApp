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

private const val TEXT_TITLE = "Ты молодец!"
private const val BTN_HOME = "Завершить"

private val SCREEN_PADDING_H = 16.dp
private val SCREEN_PADDING_V = 24.dp
private val IMAGE_HEIGHT = 260.dp
private val BUTTON_HEIGHT = 72.dp
private val BUTTON_CORNER = 36.dp

@Composable
fun HealthRecordSuccessScreen(
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
            Text(
                text = TEXT_TITLE,
                style = CardioTheme.typography.screenTitle,
                color = CardioTheme.colors.textMain,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(24.dp))
            Image(
                painter = painterResource(R.drawable.win),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IMAGE_HEIGHT),
            )
        }

        Button(
            onClick = onFinish,
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
                text = BTN_HOME,
                style = CardioTheme.typography.buttonPrimary,
            )
        }
    }
}

@CardioPreview
@Composable
private fun HealthRecordSuccessScreenPreview() {
    HealthRecordSuccessScreen(onFinish = {})
}
