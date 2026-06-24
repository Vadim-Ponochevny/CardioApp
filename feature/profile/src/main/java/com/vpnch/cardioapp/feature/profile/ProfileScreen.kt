package com.vpnch.cardioapp.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.AgeGroup
import com.vpnch.cardioapp.core.model.MetricType
import com.vpnch.cardioapp.core.model.ValveType
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onPatientIdChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onAgeGroupChange: (AgeGroup) -> Unit,
    onTakesWarfarinChange: (Boolean) -> Unit,
    onValveTypeChange: (ValveType?) -> Unit,
    onUseCustomLimitsChange: (Boolean) -> Unit,
    onCustomLimitsChange: (CustomLimitsEditState) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            text = "Профиль пациента",
            style = CardioTheme.typography.screenTitle,
            color = CardioTheme.colors.textMain,
        )

        // --- Основные данные ---
        SectionCard {
            SectionTitle("Основные данные")
            Spacer(Modifier.height(12.dp))

            ProfileTextField(
                label = "ID пациента",
                placeholder = "PT-7GZVL7PT",
                value = uiState.patientId,
                onValueChange = onPatientIdChange,
            )
            Spacer(Modifier.height(12.dp))
            ProfileTextField(
                label = "Дата рождения",
                placeholder = "ГГГГ-ММ-ДД",
                value = uiState.birthDate,
                onValueChange = onBirthDateChange,
                keyboardType = KeyboardType.Number,
            )
        }

        // --- Медицинские данные ---
        SectionCard {
            SectionTitle("Возрастная группа")
            Spacer(Modifier.height(12.dp))

            val selectableGroups = AgeGroup.entries.filter { it != AgeGroup.Custom }
            selectableGroups.chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { group ->
                        FilterChip(
                            selected = uiState.ageGroup == group,
                            onClick = { onAgeGroupChange(group) },
                            label = { Text(group.label, style = CardioTheme.typography.bodySmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CardioTheme.colors.primary,
                                selectedLabelColor = CardioTheme.colors.onPrimary,
                                containerColor = CardioTheme.colors.onPrimary,
                                labelColor = CardioTheme.colors.textMain,
                            ),
                        )
                    }
                }
            }
        }

        // --- Показатели для выбранной группы ---
        uiState.limitsForAgeGroup?.let { limits ->
            SectionCard {
                SectionTitle("Показатели нормы для ${uiState.ageGroup.label}")
                Spacer(Modifier.height(12.dp))

                limits.bloodPressureLimits?.let { bp ->
                    LimitRow("Давление (систола)", "${bp.normalSystolicMin}–${bp.normalSystolicMax} мм рт. ст.")
                    LimitRow("Давление (диастола)", "${bp.normalDiastolicMin}–${bp.normalDiastolicMax} мм рт. ст.")
                }
                limits.singleMetricLimits.forEach { limit ->
                    val unit = when (limit.metricType) {
                        MetricType.RespiratoryRate -> "в мин"
                        MetricType.HeartRate -> "уд/мин"
                        MetricType.OxygenSaturation -> "%"
                        else -> ""
                    }
                    val name = when (limit.metricType) {
                        MetricType.RespiratoryRate -> "Частота дыхания"
                        MetricType.HeartRate -> "Пульс"
                        MetricType.OxygenSaturation -> "Кислород (SpO₂)"
                        else -> limit.metricType.name
                    }
                    LimitRow(name, "${limit.normalMin}–${limit.normalMax} $unit")
                }

                Spacer(Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Изменить значения",
                        style = CardioTheme.typography.bodySmall,
                        color = CardioTheme.colors.textMain,
                        modifier = Modifier.weight(1f),
                    )
                    Switch(
                        checked = uiState.useCustomLimits,
                        onCheckedChange = onUseCustomLimitsChange,
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = CardioTheme.colors.primary,
                        ),
                    )
                }

                if (uiState.useCustomLimits) {
                    uiState.customLimitsEdit?.let { edit ->
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(12.dp))
                        CustomLimitsSection(edit, onCustomLimitsChange)
                    }
                }
            }
        }

        // --- Варфарин ---
        SectionCard {
            SectionTitle("Медикаменты")
            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Принимает варфарин",
                        style = CardioTheme.typography.bodySmall,
                        color = CardioTheme.colors.textMain,
                    )
                    Text(
                        text = "Включает отслеживание МНО",
                        style = CardioTheme.typography.bodySmall,
                        color = CardioTheme.colors.textSecondary,
                    )
                }
                Switch(
                    checked = uiState.takesWarfarin,
                    onCheckedChange = onTakesWarfarinChange,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = CardioTheme.colors.primary,
                    ),
                )
            }

            if (uiState.takesWarfarin) {
                Spacer(Modifier.height(16.dp))
                SectionTitle("Тип клапана")
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ValveType.entries.forEach { valve ->
                        FilterChip(
                            selected = uiState.valveType == valve,
                            onClick = {
                                onValveTypeChange(if (uiState.valveType == valve) null else valve)
                            },
                            label = { Text(valve.label, style = CardioTheme.typography.bodySmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CardioTheme.colors.primary,
                                selectedLabelColor = CardioTheme.colors.onPrimary,
                                containerColor = CardioTheme.colors.onPrimary,
                                labelColor = CardioTheme.colors.textMain,
                            ),
                        )
                    }
                }
            }
        }

        // --- Сохранить ---
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CardioTheme.colors.primary,
                contentColor = CardioTheme.colors.onPrimary,
            ),
        ) {
            Text("Сохранить", style = CardioTheme.typography.actionLabel)
        }
    }
}

@Composable
private fun SectionCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardioTheme.colors.onPrimary, RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        content()
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = CardioTheme.typography.cardTitle,
        color = CardioTheme.colors.textMain,
    )
}

@Composable
private fun LimitRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = CardioTheme.typography.bodySmall,
            color = CardioTheme.colors.textSecondary,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = CardioTheme.typography.bodySmall,
            color = CardioTheme.colors.textMain,
        )
    }
}

@Composable
private fun ProfileTextField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column {
        Text(
            text = label,
            style = CardioTheme.typography.bodySmall,
            color = CardioTheme.colors.textSecondary,
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = CardioTheme.colors.textDisabled) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CardioTheme.colors.primary,
                unfocusedBorderColor = CardioTheme.colors.textDisabled,
                focusedTextColor = CardioTheme.colors.textMain,
                unfocusedTextColor = CardioTheme.colors.textMain,
            ),
        )
    }
}

@Composable
private fun CustomLimitsSection(
    edit: CustomLimitsEditState,
    onChange: (CustomLimitsEditState) -> Unit,
) {
    Text(
        "Пользовательские значения нормы",
        style = CardioTheme.typography.bodySmall,
        color = CardioTheme.colors.textSecondary,
    )
    Spacer(Modifier.height(8.dp))

    CustomRangeRow("Дыхание (в мин)", edit.rrNormalMin, edit.rrNormalMax,
        onMinChange = { onChange(edit.copy(rrNormalMin = it)) },
        onMaxChange = { onChange(edit.copy(rrNormalMax = it)) },
    )
    CustomRangeRow("Пульс (уд/мин)", edit.hrNormalMin, edit.hrNormalMax,
        onMinChange = { onChange(edit.copy(hrNormalMin = it)) },
        onMaxChange = { onChange(edit.copy(hrNormalMax = it)) },
    )
    CustomRangeRow("Кислород (%)", edit.spO2NormalMin, edit.spO2NormalMax,
        onMinChange = { onChange(edit.copy(spO2NormalMin = it)) },
        onMaxChange = { onChange(edit.copy(spO2NormalMax = it)) },
    )
    CustomRangeRow("АД систола", edit.bpSystolicMin, edit.bpSystolicMax,
        onMinChange = { onChange(edit.copy(bpSystolicMin = it)) },
        onMaxChange = { onChange(edit.copy(bpSystolicMax = it)) },
    )
    CustomRangeRow("АД диастола", edit.bpDiastolicMin, edit.bpDiastolicMax,
        onMinChange = { onChange(edit.copy(bpDiastolicMin = it)) },
        onMaxChange = { onChange(edit.copy(bpDiastolicMax = it)) },
    )
}

@Composable
private fun CustomRangeRow(
    label: String,
    minValue: String,
    maxValue: String,
    onMinChange: (String) -> Unit,
    onMaxChange: (String) -> Unit,
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, style = CardioTheme.typography.bodySmall, color = CardioTheme.colors.textSecondary)
        Spacer(Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = minValue,
                onValueChange = onMinChange,
                placeholder = { Text("Мин", color = CardioTheme.colors.textDisabled) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CardioTheme.colors.primary,
                    unfocusedBorderColor = CardioTheme.colors.textDisabled,
                    focusedTextColor = CardioTheme.colors.textMain,
                    unfocusedTextColor = CardioTheme.colors.textMain,
                ),
            )
            OutlinedTextField(
                value = maxValue,
                onValueChange = onMaxChange,
                placeholder = { Text("Макс", color = CardioTheme.colors.textDisabled) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CardioTheme.colors.primary,
                    unfocusedBorderColor = CardioTheme.colors.textDisabled,
                    focusedTextColor = CardioTheme.colors.textMain,
                    unfocusedTextColor = CardioTheme.colors.textMain,
                ),
            )
        }
    }
}
