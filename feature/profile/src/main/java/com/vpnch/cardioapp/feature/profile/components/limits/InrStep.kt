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
internal fun InrStep(edit: CustomLimitsEditState, onChange: (CustomLimitsEditState) -> Unit) {
    ZoneCard("Норма", NormalColor) {
        LimitsRangeRow(
            minValue = edit.inrNormalMin, onMinChange = { onChange(edit.copy(inrNormalMin = it)) },
            maxValue = edit.inrNormalMax, onMaxChange = { onChange(edit.copy(inrNormalMax = it)) },
            allowDecimal = true,
        )
    }
    Spacer(Modifier.height(12.dp))
    ZoneCard("Обратить внимание", AttentionColor) {
        LimitsRangeRow(
            minValue = edit.inrAttentionMin, onMinChange = { onChange(edit.copy(inrAttentionMin = it)) },
            maxValue = edit.inrAttentionMax, onMaxChange = { onChange(edit.copy(inrAttentionMax = it)) },
            allowDecimal = true,
        )
    }
    Spacer(Modifier.height(12.dp))
    ZoneCard("Обратиться к врачу", DoctorColor) {
        LimitsSingleField(
            label = "< этого значения",
            value = edit.inrDoctorMax,
            onValueChange = { onChange(edit.copy(inrDoctorMax = it)) },
            allowDecimal = true,
        )
    }
}
