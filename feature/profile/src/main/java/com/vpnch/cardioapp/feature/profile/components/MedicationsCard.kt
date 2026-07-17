package com.vpnch.cardioapp.feature.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.patient.ValveType
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

@Composable
internal fun MedicationsCard(
    takesWarfarin: Boolean,
    valveType: ValveType?,
    onTakesWarfarinChange: (Boolean) -> Unit,
    onValveTypeChange: (ValveType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    ProfileCard(modifier = modifier) {
        Text(
            text = "Медикаменты",
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
                    text = "Принимает варфарин",
                    style = CardioTheme.typography.bodySmall,
                    color = CardioTheme.colors.textMain,
                )
                Text(
                    text = "Включает отслеживание МНО",
                    style = CardioTheme.typography.bodySmall,
                    color = CardioTheme.colors.textSecondary,
                )
            }
            Switch(
                checked = takesWarfarin,
                onCheckedChange = onTakesWarfarinChange,
                colors = SwitchDefaults.colors(checkedTrackColor = CardioTheme.colors.primary),
            )
        }

        if (takesWarfarin) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Тип клапана",
                style = CardioTheme.typography.cardTitle,
                color = CardioTheme.colors.textMain,
            )
            Spacer(Modifier.height(8.dp))
            ValveType.entries.forEach { valve ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onValveTypeChange(if (valveType == valve) null else valve)
                        }
                        .padding(vertical = 4.dp),
                ) {
                    RadioButton(
                        selected = valveType == valve,
                        onClick = { onValveTypeChange(if (valveType == valve) null else valve) },
                        colors = RadioButtonDefaults.colors(selectedColor = CardioTheme.colors.primary),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = valve.label,
                        style = CardioTheme.typography.bodySmall,
                        color = CardioTheme.colors.textMain,
                    )
                }
            }
        }
    }
}
