package com.vpnch.cardioapp.feature.profile.components.limits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val FIELD_HEIGHT = 72.dp
private val FIELD_BORDER_WIDTH = 3.dp

@Composable
internal fun LimitsRangeRow(
    minValue: String,
    onMinChange: (String) -> Unit,
    maxValue: String,
    onMaxChange: (String) -> Unit,
    allowDecimal: Boolean = false,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text("от", style = CardioTheme.typography.bodySmall, color = Color.Black.copy(alpha = 0.6f))
            Spacer(Modifier.height(4.dp))
            LimitsNumberField(value = minValue, onValueChange = onMinChange, allowDecimal = allowDecimal)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text("до", style = CardioTheme.typography.bodySmall, color = Color.Black.copy(alpha = 0.6f))
            Spacer(Modifier.height(4.dp))
            LimitsNumberField(value = maxValue, onValueChange = onMaxChange, allowDecimal = allowDecimal)
        }
    }
}

@Composable
internal fun LimitsSingleField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    allowDecimal: Boolean = false,
) {
    Text(label, style = CardioTheme.typography.bodySmall, color = Color.Black.copy(alpha = 0.6f))
    Spacer(Modifier.height(4.dp))
    LimitsNumberField(value = value, onValueChange = onValueChange, allowDecimal = allowDecimal)
}

@Composable
private fun LimitsNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    allowDecimal: Boolean = false,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }
    var tfState by remember { mutableStateOf(TextFieldValue(value, TextRange(value.length))) }

    // Sync when external value changes (e.g., step navigation or initial load)
    LaunchedEffect(value) {
        if (tfState.text != value) {
            tfState = TextFieldValue(value, TextRange(value.length))
        }
    }

    val strokeColor = if (isFocused) CardioTheme.colors.textMain else CardioTheme.colors.onPrimary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(FIELD_HEIGHT)
            .border(FIELD_BORDER_WIDTH, strokeColor, StepCardShape)
            .background(CardioTheme.colors.onPrimary, StepCardShape),
        contentAlignment = Alignment.CenterStart,
    ) {
        BasicTextField(
            value = tfState,
            onValueChange = { newTfValue ->
                val input = newTfValue.text
                val isValid = if (allowDecimal) {
                    input.matches(Regex("""^\d*\.?\d*$"""))
                } else {
                    input.isEmpty() || input.all { it.isDigit() }
                }
                if (isValid) {
                    tfState = newTfValue
                    onValueChange(input)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .onFocusChanged { isFocused = it.isFocused },
            textStyle = CardioTheme.typography.inputValue.copy(color = CardioTheme.colors.textMain),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(CardioTheme.colors.textMain),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (tfState.text.isEmpty() && !isFocused) {
                        Text(
                            text = "0",
                            style = CardioTheme.typography.inputValue.copy(color = CardioTheme.colors.textDisabled),
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}
