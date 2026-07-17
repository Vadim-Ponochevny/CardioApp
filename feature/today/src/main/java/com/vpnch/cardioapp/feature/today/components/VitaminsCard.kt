package com.vpnch.cardioapp.feature.today.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vpnch.cardioapp.core.model.vitamins.VitaminIntakeSummary
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

@Composable
fun VitaminsSection(
    vitaminIntakes: List<VitaminIntakeSummary>,
    onVitaminCheckedChange: (VitaminIntakeSummary, Boolean) -> Unit,
    onOpenVitamins: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Витаминки",
                style = CardioTheme.typography.itemTitle,
                color = CardioTheme.colors.textMain,
            )
            IconButton(onClick = onOpenVitamins, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Управление витаминками",
                    tint = CardioTheme.colors.textSecondary,
                    modifier = Modifier.size(28.dp),
                )
            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 0.dp),
        ) {
            items(vitaminIntakes, key = { it.vitamin.id }) { summary ->
                VitaminItemCard(
                    summary = summary,
                    onCheckedChange = { checked ->
                        onVitaminCheckedChange(summary, checked)
                    },
                )
            }
        }
    }
}

@Composable
private fun VitaminItemCard(
    summary: VitaminIntakeSummary,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    var showCancelDialog by rememberSaveable { mutableStateOf(false) }
    val isChecked = summary.intake?.isTaken == true

    val cardColor = if (isChecked) {
        Color(0xFFE5F6EA)
    } else {
        Color(0xFFFEF9E6)
    }

    Card(
        modifier = modifier.width(140.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isChecked) Color(0xFF1DC44E)
                        else Color.Transparent
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null // Убираем ripple эффект
                    ) {
                        if (!isChecked) {
                            // Если не отмечено - показываем диалог подтверждения приёма
                            showConfirmationDialog = true
                        } else {
                            // Если отмечено - показываем диалог подтверждения отмены
                            showCancelDialog = true
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                } else {
                    // Неотмеченное состояние - только оранжевая граница
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .border(
                                width = 2.dp,
                                color = Color(0xFFFEC81A),
                                shape = CircleShape
                            )
                    )
                }
            }

            Text(
                text = summary.vitamin.name,
                style = CardioTheme.typography.bodySmall,
                color = CardioTheme.colors.textMain,
                textAlign = TextAlign.Center,
            )
        }
    }

    // Диалог подтверждения ПРИЁМА (поставить галочку)
    if (showConfirmationDialog) {
        Dialog(
            onDismissRequest = { showConfirmationDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(horizontal = 16.dp), // Уменьшаем внешние отступы
                shape = RoundedCornerShape(36.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardioTheme.colors.onPrimary
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = summary.vitamin.name,
                        style = CardioTheme.typography.actionLabel,
                        color = CardioTheme.colors.textMain,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Подтвердить приём?",
                        style = CardioTheme.typography.bodySmall,
                        color = CardioTheme.colors.textMain,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            onClick = { showConfirmationDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CardioTheme.colors.textDisabled,
                                contentColor = CardioTheme.colors.textMain
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(
                                text = "Нет",
                                style = CardioTheme.typography.bodyMedium,
                                color = CardioTheme.colors.textMain
                            )
                        }

                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            onClick = {
                                showConfirmationDialog = false
                                onCheckedChange(true)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1DC44E),
                                contentColor = CardioTheme.colors.onPrimary
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(
                                text = "Да",
                                style = CardioTheme.typography.navLabel,
                                color = CardioTheme.colors.onPrimary,
                            )
                        }
                    }
                }
            }
        }
    }

// Диалог подтверждения ОТМЕНЫ (убрать галочку)
    if (showCancelDialog) {
        Dialog(
            onDismissRequest = { showCancelDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .wrapContentWidth() , // Увеличиваем ширину до 85% от экрана
//                    .padding(horizontal = 16.dp), // Уменьшаем внешние отступы
                shape = RoundedCornerShape(36.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardioTheme.colors.onPrimary
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = summary.vitamin.name,
                        style = CardioTheme.typography.actionLabel,
                        color = CardioTheme.colors.textMain,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Отменить приём?",
                        style = CardioTheme.typography.bodySmall,
                        color = CardioTheme.colors.textMain,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            modifier = Modifier
                                .wrapContentWidth()
                                .height(56.dp),
                            onClick = { showCancelDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CardioTheme.colors.textDisabled,
                                contentColor = CardioTheme.colors.textMain
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(
                                text = "Нет",
                                style = CardioTheme.typography.navLabel,
                                color = CardioTheme.colors.textMain
                            )
                        }

                        TextButton(
                            modifier = Modifier
                                .wrapContentWidth()
                                .height(56.dp),
                            onClick = {
                                showCancelDialog = false
                                onCheckedChange(false)
                            },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = CardioTheme.colors.cardVitamins,
                                contentColor = CardioTheme.colors.onPrimary
                            )
                        ) {
                            Text(
                                text = "Отменить",
                                style = CardioTheme.typography.navLabel,
                                color = CardioTheme.colors.textMain,
                            )
                        }
                    }
                }
            }
        }
    }
}