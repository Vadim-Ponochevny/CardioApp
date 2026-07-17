package com.vpnch.cardioapp.feature.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

@Composable
internal fun NotificationsCard(
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    ProfileCard(modifier = modifier) {
        Text(
            text = "Уведомления",
            style = CardioTheme.typography.cardTitle,
            color = CardioTheme.colors.textMain,
        )
        Spacer(Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Ежедневное напоминание",
                    style = CardioTheme.typography.bodySmall,
                    color = CardioTheme.colors.textMain,
                )
                Text(
                    text = "Принять витаминки и записать показатели",
                    style = CardioTheme.typography.bodySmall,
                    color = CardioTheme.colors.textSecondary,
                )
            }
            Switch(
                checked = enabled,
                onCheckedChange = onEnabledChange,
                colors = SwitchDefaults.colors(checkedTrackColor = CardioTheme.colors.primary),
            )
        }
    }
}
