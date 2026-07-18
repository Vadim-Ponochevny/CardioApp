package com.vpnch.cardioapp.feature.profile.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val INPUT_HEIGHT = 60.dp

@Composable
internal fun ProfileInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    enabled: Boolean = true,
    onDone: (() -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    val strokeColor = when {
        !enabled -> CardioTheme.colors.textDisabled
        isFocused -> CardioTheme.colors.textMain
        else -> CardioTheme.colors.textDisabled
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(INPUT_HEIGHT)
            .border(3.dp, strokeColor, ProfileCardShape),
        contentAlignment = Alignment.CenterStart,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .onFocusChanged { isFocused = it.isFocused },
            textStyle = CardioTheme.typography.bodySmall.copy(
                color = if (enabled) CardioTheme.colors.textMain else CardioTheme.colors.textSecondary,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = if (onDone != null) ImeAction.Done else ImeAction.Default),
            keyboardActions = KeyboardActions(onDone = { onDone?.invoke() }),
            cursorBrush = SolidColor(CardioTheme.colors.textMain),
            decorationBox = { innerTextField ->
                if (value.isEmpty() && !isFocused) {
                    Text(
                        text = placeholder,
                        style = CardioTheme.typography.bodySmall.copy(color = CardioTheme.colors.textDisabled),
                    )
                }
                innerTextField()
            },
        )
    }
}
