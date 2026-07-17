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
internal fun HeartRateStep(edit: CustomLimitsEditState, onChange: (CustomLimitsEditState) -> Unit) {
    ZoneCard("Норма", NormalColor) {
        LimitsRangeRow(
            minValue = edit.hrNormalMin, onMinChange = { onChange(edit.copy(hrNormalMin = it)) },
            maxValue = edit.hrNormalMax, onMaxChange = { onChange(edit.copy(hrNormalMax = it)) },
        )
    }
    Spacer(Modifier.height(12.dp))
    ZoneCard("Обратить внимание", AttentionColor) {
        LimitsRangeRow(
            minValue = edit.hrAttentionMin, onMinChange = { onChange(edit.copy(hrAttentionMin = it)) },
            maxValue = edit.hrAttentionMax, onMaxChange = { onChange(edit.copy(hrAttentionMax = it)) },
        )
    }
    Spacer(Modifier.height(12.dp))
    ZoneCard("Обратиться к врачу", DoctorColor) {
        LimitsSingleField(
            label = "≤ этого значения",
            value = edit.hrDoctorMax,
            onValueChange = { onChange(edit.copy(hrDoctorMax = it)) },
        )
    }
}
