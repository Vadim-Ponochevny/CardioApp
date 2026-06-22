package com.vpnch.cardioapp.feature.healthrecords.create.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

@Composable
fun MetricInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: (() -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val isEmpty = value.isEmpty()

    val isActive = isFocused
    val isFilled = !isEmpty && !isFocused

    val strokeColor = when {
        isActive -> CardioTheme.colors.textMain
        isFilled -> CardioTheme.colors.textDisabled
        else -> CardioTheme.colors.textDisabled
    }

    val textColor = when {
        isActive -> CardioTheme.colors.textMain
        isFilled -> CardioTheme.colors.textMain
        else -> CardioTheme.colors.textDisabled
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .border(
                    width = 4.dp,
                    color = strokeColor,
                    shape = RoundedCornerShape(36.dp),
                ),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                textStyle = CardioTheme.typography.inputValue.copy(
                    color = textColor,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        onImeAction?.invoke()
                    },
                    onNext = {
                        onImeAction?.invoke()
                    },
                ),
                singleLine = true,
                cursorBrush = SolidColor(CardioTheme.colors.textMain),
                decorationBox = { innerTextField ->
                    if (value.isEmpty() && !isFocused) {
                        Text(
                            text = label,
                            style = CardioTheme.typography.inputValue.copy(
                                color = CardioTheme.colors.textDisabled,
                            ),
                        )
                    }
                    innerTextField()
                },
            )
        }
    }
}
