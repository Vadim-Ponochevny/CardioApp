package com.vpnch.cardioapp.feature.history.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vpnch.cardioapp.core.ui.R as CoreUiR
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val BANNER_CORNER_RADIUS = 36.dp
private val BANNER_HEIGHT = 120.dp
private val BANNER_HORIZONTAL_PADDING = 24.dp
private val BANNER_VERTICAL_PADDING = 20.dp
private val DOCTOR_IMAGE_WIDTH = 110.dp
private val DOCTOR_IMAGE_OFFSET_X = (-36).dp

private val CountTextSize = 40.sp
private val LabelTextSize = 17.sp

private val ColorTextOnPrimary = Color.White

@Composable
fun StreakBanner(
    streakDays: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(BANNER_HEIGHT)
            .clip(RoundedCornerShape(BANNER_CORNER_RADIUS))
            .background(CardioTheme.colors.primary),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = BANNER_HORIZONTAL_PADDING, vertical = BANNER_VERTICAL_PADDING),
        ) {
            Text(
                text = "$streakDays ${streakDays.daysWord()}",
                color = ColorTextOnPrimary,
                fontSize = CountTextSize,
                style = CardioTheme.typography.cardTitle,
            )
            Text(
                text = "Без пропусков",
                color = ColorTextOnPrimary.copy(alpha = 0.85f),
                fontSize = LabelTextSize,
                style = CardioTheme.typography.bodySmall,
            )
        }

        Image(
            painter = painterResource(CoreUiR.drawable.ic_doctor),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(DOCTOR_IMAGE_WIDTH)
                .fillMaxHeight()
                .align(Alignment.BottomEnd)
                .offset(x = DOCTOR_IMAGE_OFFSET_X),
        )
    }
}

private fun Int.daysWord(): String = when {
    this % 100 in 11..19 -> "дней"
    this % 10 == 1 -> "день"
    this % 10 in 2..4 -> "дня"
    else -> "дней"
}
