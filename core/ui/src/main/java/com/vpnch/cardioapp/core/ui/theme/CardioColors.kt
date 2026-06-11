package com.vpnch.cardioapp.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class CardioColors(
    val primary: Color,
    val onPrimary: Color,
    val textMain: Color,
    val textSecondary: Color,
    val navSelectedContainer: Color,
    val cardMetrics: Color,
    val cardVitamins: Color,
    val cardSurvey: Color,
    val textDisabled: Color,
    val primaryDark: Color,
    val warningContainer: Color,
    val actionCall: Color,
)

private fun cardioColor(hex: String): Color {
    val normalized = hex.removePrefix("#")
    return Color(normalized.toLong(16) or 0xFF000000)
}

val DefaultCardioColors = CardioColors(
    primary = cardioColor("#229FF5"),
    onPrimary = cardioColor("#FFFFFF"),
    textMain = cardioColor("#000000"),
    textSecondary = cardioColor("#646464"),
    navSelectedContainer = cardioColor("#CAE5F7"),
    cardMetrics = cardioColor("#797DFB"),
    cardVitamins = cardioColor("#FFAE43"),
    cardSurvey = cardioColor("#FF9494"),
    textDisabled = cardioColor("#CCCCCC"),
    primaryDark = cardioColor("#034C7F"),
    warningContainer = cardioColor("#FFF06C"),
    actionCall = cardioColor("#ADEF74"),
)

/**
 * Маппинг семантических цветов на слоты Material ColorScheme для обратной совместимости.
 */
fun CardioColors.asMaterialColorScheme(): ColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = navSelectedContainer,
    onPrimaryContainer = textMain,
    secondary = primaryDark,
    onSecondary = onPrimary,
    tertiary = cardMetrics,
    onTertiary = onPrimary,
    background = Color.White,
    onBackground = textMain,
    surface = Color.White,
    onSurface = textMain,
    surfaceVariant = navSelectedContainer,
    onSurfaceVariant = textSecondary,
    outline = textDisabled,
    error = cardSurvey,
    onError = onPrimary,
)
