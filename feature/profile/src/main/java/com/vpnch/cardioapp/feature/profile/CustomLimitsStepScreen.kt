package com.vpnch.cardioapp.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.CardioTopBar
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.profile.components.limits.BloodPressureStep
import com.vpnch.cardioapp.feature.profile.components.limits.HeartRateStep
import com.vpnch.cardioapp.feature.profile.components.limits.InrStep
import com.vpnch.cardioapp.feature.profile.components.limits.OxygenStep
import com.vpnch.cardioapp.feature.profile.components.limits.StepCardShape

private val SCREEN_BG_COLOR = Color(0xFFF6F6F6)
private val CONTENT_HORIZONTAL_PADDING = 16.dp

@Composable
fun CustomLimitsStepScreen(
    uiState: ProfileUiState,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onCustomLimitsChange: (CustomLimitsEditState) -> Unit,
) {
    val step = uiState.customLimitsStep ?: return
    val edit = uiState.customLimitsEdit ?: CustomLimitsEditState()
    val maxStep = if (uiState.takesWarfarin) 3 else 2
    val isLastStep = uiState.isSingleMetricEdit || step == maxStep
    val titles = buildList {
        add("ЧСС (уд/мин)")
        add("АД (мм рт. ст.)")
        add("Сатурация (%)")
        if (uiState.takesWarfarin) add("МНО")
    }
    val title = titles.getOrElse(step) { "Показатель" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SCREEN_BG_COLOR),
    ) {
        CardioTopBar(title = title, onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = CONTENT_HORIZONTAL_PADDING)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(16.dp))

            when (step) {
                0 -> HeartRateStep(edit, onCustomLimitsChange)
                1 -> BloodPressureStep(edit, onCustomLimitsChange)
                2 -> OxygenStep(edit, onCustomLimitsChange)
                3 -> InrStep(edit, onCustomLimitsChange)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth().height(72.dp),
                shape = StepCardShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardioTheme.colors.primary,
                    contentColor = CardioTheme.colors.onPrimary,
                ),
            ) {
                Text(
                    text = if (isLastStep) "Сохранить" else "Далее",
                    style = CardioTheme.typography.buttonPrimary,
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
