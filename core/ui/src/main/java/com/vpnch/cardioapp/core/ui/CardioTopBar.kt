package com.vpnch.cardioapp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val TOP_BAR_HORIZONTAL_PADDING = 8.dp
private val TOP_BAR_VERTICAL_PADDING = 8.dp
private val TITLE_START_PADDING = 8.dp

@Composable
fun CardioTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(CardioTheme.colors.background)
            .statusBarsPadding()
            .padding(
                horizontal = TOP_BAR_HORIZONTAL_PADDING,
                vertical = TOP_BAR_VERTICAL_PADDING,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BackButton(onBack = onBack)
        Column(modifier = Modifier.padding(start = TITLE_START_PADDING)) {
            Text(
                text = title,
                style = CardioTheme.typography.cardTitle,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = CardioTheme.typography.navLabel,
                    color = CardioTheme.colors.textSecondary,
                )
            }
        }
    }
}
