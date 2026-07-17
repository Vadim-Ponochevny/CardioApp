package com.vpnch.cardioapp.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class CardioTypography(
    val screenTitle: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val cardTitle: TextStyle,
    val actionLabel: TextStyle,
    val itemTitle: TextStyle,
    val navLabel: TextStyle,
    val inputValue: TextStyle,
    val dialogTitle: TextStyle,
    val buttonPrimary: TextStyle,
    val tabLabel: TextStyle,
)

private fun cardioTextStyle(
    fontSize: Int,
    fontWeight: FontWeight,
): TextStyle = TextStyle(
    fontFamily = CardioFontFamily,
    fontWeight = fontWeight,
    fontSize = fontSize.sp,
    letterSpacing = 0.sp,
)

val DefaultCardioTypography = CardioTypography(
    screenTitle = cardioTextStyle(fontSize = 40, fontWeight = FontWeight.Medium),
    bodyLarge = cardioTextStyle(fontSize = 32, fontWeight = FontWeight.Normal),
    bodyMedium = cardioTextStyle(fontSize = 28, fontWeight = FontWeight.Normal),
    bodySmall = cardioTextStyle(fontSize = 24, fontWeight = FontWeight.Normal),
    cardTitle = cardioTextStyle(fontSize = 36, fontWeight = FontWeight.SemiBold),
    actionLabel = cardioTextStyle(fontSize = 32, fontWeight = FontWeight.Medium),
    itemTitle = cardioTextStyle(fontSize = 28, fontWeight = FontWeight.Medium),
    navLabel = cardioTextStyle(fontSize = 20, fontWeight = FontWeight.Medium),
    inputValue = cardioTextStyle(fontSize = 48, fontWeight = FontWeight.Normal),
    dialogTitle = cardioTextStyle(fontSize = 32, fontWeight = FontWeight.SemiBold),
    buttonPrimary = cardioTextStyle(fontSize = 40, fontWeight = FontWeight.Bold),
    tabLabel = cardioTextStyle(fontSize = 24, fontWeight = FontWeight.Medium),
)

/**
 * Маппинг семантических стилей на слоты Material Typography для обратной совместимости (не используется).
 */
fun CardioTypography.asMaterialTypography(): Typography = Typography(
    displayLarge = inputValue,
    headlineLarge = screenTitle,
    headlineMedium = cardTitle,
    headlineSmall = actionLabel,
    titleLarge = cardTitle,
    titleMedium = dialogTitle,
    titleSmall = itemTitle,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = tabLabel,
    labelMedium = navLabel,
    labelSmall = navLabel,
)
