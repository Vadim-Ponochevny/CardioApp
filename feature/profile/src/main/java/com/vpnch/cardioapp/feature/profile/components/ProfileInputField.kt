package com.vpnch.cardioapp.feature.profile.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val INPUT_HEIGHT = 88.dp

@Composable
internal fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }
    val strokeColor = if (isFocused) CardioTheme.colors.textMain else CardioTheme.colors.textDisabled

    Column(modifier = modifier) {
        Text(label, style = CardioTheme.typography.bodySmall, color = CardioTheme.colors.textSecondary)
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(INPUT_HEIGHT)
                .border(4.dp, strokeColor, ProfileCardShape),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle = CardioTheme.typography.inputValue.copy(color = CardioTheme.colors.textMain),
                singleLine = true,
                cursorBrush = SolidColor(CardioTheme.colors.textMain),
                decorationBox = { innerTextField ->
                    if (value.isEmpty() && !isFocused) {
                        Text(
                            text = placeholder,
                            style = CardioTheme.typography.inputValue.copy(color = CardioTheme.colors.textDisabled),
                        )
                    }
                    innerTextField()
                },
            )
        }
    }
}
