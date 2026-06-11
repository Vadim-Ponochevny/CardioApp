package com.vpnch.cardioapp.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalCardioTypography = staticCompositionLocalOf { DefaultCardioTypography }
val LocalCardioColors = staticCompositionLocalOf { DefaultCardioColors }

@Composable
fun ProvideCardioTypography(
    typography: CardioTypography = DefaultCardioTypography,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalCardioTypography provides typography,
        content = content,
    )
}

@Composable
fun ProvideCardioColors(
    colors: CardioColors = DefaultCardioColors,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalCardioColors provides colors,
        content = content,
    )
}

@Composable
fun ProvideCardioTheme(
    typography: CardioTypography = DefaultCardioTypography,
    colors: CardioColors = DefaultCardioColors,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalCardioTypography provides typography,
        LocalCardioColors provides colors,
        content = content,
    )
}

@Composable
fun CardioTheme(
    typography: CardioTypography = DefaultCardioTypography,
    colors: CardioColors = DefaultCardioColors,
    content: @Composable () -> Unit,
) {
    ProvideCardioTheme(typography, colors) {
        MaterialTheme(
            colorScheme = colors.asMaterialColorScheme(),
            typography = typography.asMaterialTypography(),
            content = content,
        )
    }
}

/**
 * Единая точка доступа к дизайн-системе приложения.
 * Использовать на экранах как: CardioTheme.colors.primary
 */
object CardioTheme {
    val typography: CardioTypography
        @Composable
        get() = LocalCardioTypography.current

    val colors: CardioColors
        @Composable
        get() = LocalCardioColors.current
}
