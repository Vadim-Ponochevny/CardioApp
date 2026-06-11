package com.vpnch.cardioapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.vpnch.cardioapp.core.ui.theme.DefaultCardioColors
import com.vpnch.cardioapp.core.ui.theme.DefaultCardioTypography
import com.vpnch.cardioapp.core.ui.theme.ProvideCardioTheme
import com.vpnch.cardioapp.core.ui.theme.asMaterialColorScheme
import com.vpnch.cardioapp.core.ui.theme.asMaterialTypography

@Composable
fun CardioAppTheme(
    content: @Composable () -> Unit,
) {
    ProvideCardioTheme {
        MaterialTheme(
            colorScheme = DefaultCardioColors.asMaterialColorScheme(),
            typography = DefaultCardioTypography.asMaterialTypography(),
            content = content,
        )
    }
}
