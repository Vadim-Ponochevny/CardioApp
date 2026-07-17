package com.vpnch.cardioapp.feature.profile.components.limits

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.profile.CustomLimitsEditState
import com.vpnch.cardioapp.feature.profile.components.DoctorColor
import com.vpnch.cardioapp.feature.profile.components.NormalColor

@Composable
internal fun BloodPressureStep(edit: CustomLimitsEditState, onChange: (CustomLimitsEditState) -> Unit) {
    ZoneCard("Систолическое (норма)", NormalColor) {
        LimitsRangeRow(
            minValue = edit.bpSystolicMin, onMinChange = { onChange(edit.copy(bpSystolicMin = it)) },
            maxValue = edit.bpSystolicMax, onMaxChange = { onChange(edit.copy(bpSystolicMax = it)) },
        )
    }
    Spacer(Modifier.height(12.dp))
    ZoneCard("Диастолическое (норма)", NormalColor) {
        LimitsRangeRow(
            minValue = edit.bpDiastolicMin, onMinChange = { onChange(edit.copy(bpDiastolicMin = it)) },
            maxValue = edit.bpDiastolicMax, onMaxChange = { onChange(edit.copy(bpDiastolicMax = it)) },
        )
    }
    Spacer(Modifier.height(12.dp))
    ZoneCard("Обратиться к врачу", DoctorColor) {
        Text(
            text = "Вне нормального диапазона",
            style = CardioTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.75f),
        )
    }
}
