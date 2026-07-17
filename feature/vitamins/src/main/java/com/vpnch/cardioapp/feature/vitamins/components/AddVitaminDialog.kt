package com.vpnch.cardioapp.feature.vitamins.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

private val DIALOG_CORNER_RADIUS = 36.dp
private val DIALOG_PADDING = 24.dp
private val DIALOG_SPACING = 16.dp

private val INPUT_HEIGHT = 56.dp
private val INPUT_BORDER_WIDTH = 3.dp
private val INPUT_CORNER_RADIUS = 24.dp
private val INPUT_HORIZONTAL_PADDING = 16.dp

private val BUTTON_HEIGHT = 56.dp
private val BUTTON_SPACING = 12.dp
private val BUTTON_CORNER_RADIUS = 24.dp
private val BUTTON_CONTENT_PADDING_H = 8.dp

@Composable
fun AddVitaminDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var name by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(DIALOG_CORNER_RADIUS),
            colors = CardDefaults.cardColors(containerColor = CardioTheme.colors.onPrimary),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DIALOG_PADDING),
                verticalArrangement = Arrangement.spacedBy(DIALOG_SPACING),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Новая витаминка",
                    style = CardioTheme.typography.actionLabel,
                    color = CardioTheme.colors.textMain,
                    textAlign = TextAlign.Center,
                )

                val inputShape = RoundedCornerShape(INPUT_CORNER_RADIUS)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(INPUT_HEIGHT)
                        .border(
                            width = INPUT_BORDER_WIDTH,
                            color = if (name.isNotBlank()) CardioTheme.colors.textMain
                                    else CardioTheme.colors.textDisabled,
                            shape = inputShape,
                        ),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    BasicTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = INPUT_HORIZONTAL_PADDING),
                        textStyle = CardioTheme.typography.bodyMedium.copy(
                            color = CardioTheme.colors.textMain,
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        singleLine = true,
                        cursorBrush = SolidColor(CardioTheme.colors.textMain),
                        decorationBox = { inner ->
                            if (name.isEmpty()) {
                                Text(
                                    text = "Название",
                                    style = CardioTheme.typography.bodyMedium.copy(
                                        color = CardioTheme.colors.textDisabled,
                                    ),
                                )
                            }
                            inner()
                        },
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(BUTTON_SPACING),
                ) {
                    TextButton(
                        modifier = Modifier.weight(1f).height(BUTTON_HEIGHT),
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = CardioTheme.colors.textDisabled,
                            contentColor = CardioTheme.colors.textMain,
                        ),
                        shape = RoundedCornerShape(BUTTON_CORNER_RADIUS),
                    ) {
                        Text(
                            text = "Отмена",
                            style = CardioTheme.typography.bodySmall,
                            color = CardioTheme.colors.textMain,
                        )
                    }
                    Button(
                        modifier = Modifier.weight(1f).height(BUTTON_HEIGHT),
                        onClick = { onConfirm(name) },
                        enabled = name.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CardioTheme.colors.primary,
                            contentColor = CardioTheme.colors.onPrimary,
                        ),
                        shape = RoundedCornerShape(BUTTON_CORNER_RADIUS),
                        contentPadding = PaddingValues(horizontal = BUTTON_CONTENT_PADDING_H),
                    ) {
                        Text(
                            text = "Добавить",
                            style = CardioTheme.typography.bodySmall,
                            color = CardioTheme.colors.onPrimary,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}
