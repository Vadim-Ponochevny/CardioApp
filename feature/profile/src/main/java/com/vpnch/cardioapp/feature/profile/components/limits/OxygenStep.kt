package com.vpnch.cardioapp.feature.profile.components.limits

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.feature.profile.CustomLimitsEditState
import com.vpnch.cardioapp.feature.profile.components.AttentionColor
import com.vpnch.cardioapp.feature.profile.components.DoctorColor
import com.vpnch.cardioapp.feature.profile.components.NormalColor

@Composable
internal fun OxygenStep(edit: CustomLimitsEditState, onChange: (CustomLimitsEditState) -> Unit) {
    ZoneCard("Норма", NormalColor) {
        LimitsRangeRow(
            minValue = edit.spO2NormalMin, onMinChange = { onChange(edit.copy(spO2NormalMin = it)) },
            maxValue = edit.spO2NormalMax, onMaxChange = { onChange(edit.copy(spO2NormalMax = it)) },
        )
    }
    Spacer(Modifier.height(12.dp))
    ZoneCard("Обратить внимание", AttentionColor) {
        LimitsRangeRow(
            minValue = edit.spO2AttentionMin, onMinChange = { onChange(edit.copy(spO2AttentionMin = it)) },
            maxValue = edit.spO2AttentionMax, onMaxChange = { onChange(edit.copy(spO2AttentionMax = it)) },
        )
    }
    Spacer(Modifier.height(12.dp))
    ZoneCard("Обратиться к врачу", DoctorColor) {
        LimitsSingleField(
            label = "< этого значения",
            value = edit.spO2DoctorMax,
            onValueChange = { onChange(edit.copy(spO2DoctorMax = it)) },
        )
    }
}
