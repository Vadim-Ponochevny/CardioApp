@file:OptIn(ExperimentalMaterial3Api::class)

package com.vpnch.cardioapp.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.health.limits.BloodPressureLimits
import com.vpnch.cardioapp.core.model.health.limits.SingleMetricLimits
import com.vpnch.cardioapp.core.model.health.metrics.MetricType
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.core.model.patient.ValveType
import com.vpnch.cardioapp.core.ui.CardioDialog
import com.vpnch.cardioapp.core.ui.CardioDialogButton
import com.vpnch.cardioapp.core.ui.CardioTopBar
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.profile.components.AgeGroupDropdown
import com.vpnch.cardioapp.feature.profile.components.DateInputField
import com.vpnch.cardioapp.feature.profile.components.LegendCard
import com.vpnch.cardioapp.feature.profile.components.MedicationsCard
import com.vpnch.cardioapp.feature.profile.components.MetricBlock
import com.vpnch.cardioapp.feature.profile.components.MetricDisplay
import com.vpnch.cardioapp.feature.profile.components.NotificationsCard
import com.vpnch.cardioapp.feature.profile.components.ProfileCardShape
import com.vpnch.cardioapp.feature.profile.components.ProfileInputField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val SCREEN_BG_COLOR = Color(0xFFF6F6F6)
private val CONTENT_HORIZONTAL_PADDING = 16.dp
private val ITEM_SPACING = 12.dp
private val INFO_BUTTON_SIZE = 24.dp
private val COLOR_MANUAL_BUTTON = Color(0xFFE0E0E0)

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onBack: () -> Unit,
    onPatientIdChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onAgeGroupChange: (AgeGroup) -> Unit,
    onTakesWarfarinChange: (Boolean) -> Unit,
    onValveTypeChange: (ValveType?) -> Unit,
    onOpenCustomLimits: () -> Unit,
    onEditMetric: (MetricType) -> Unit,
    onNotificationsEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showIdInfo by remember { mutableStateOf(false) }
    var isIdLocked by remember { mutableStateOf(true) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.birthDate.parseToMillis(),
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onBirthDateChange(millisToIso(it)) }
                    showDatePicker = false
                }) { Text("ОК") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            },
            colors = DatePickerDefaults.colors(
                containerColor = CardioTheme.colors.onPrimary,
            ),
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = CardioTheme.colors.onPrimary,
                    selectedDayContainerColor = CardioTheme.colors.primary,
                    todayDateBorderColor = CardioTheme.colors.primary,
                    todayContentColor = CardioTheme.colors.primary,
                    selectedYearContainerColor = CardioTheme.colors.primary,
                ),
            )
        }
    }

    if (showIdInfo) {
        CardioDialog(
            title = "ID пациента",
            message = "Это уникальный идентификатор пациента. При смене ID все данные (записи, витаминки, лимиты) будут привязаны к новому профилю. Менять ID следует только намеренно — например, чтобы переключиться на другого пациента.",
            primaryButton = CardioDialogButton(
                label = "Понятно",
                containerColor = CardioTheme.colors.primary,
                contentColor = CardioTheme.colors.onPrimary,
                onClick = { showIdInfo = false },
            ),
            secondaryButton = CardioDialogButton(
                label = "Изменить ID",
                containerColor = CardioTheme.colors.textDisabled,
                contentColor = CardioTheme.colors.textMain,
                onClick = { showIdInfo = false; isIdLocked = false },
            ),
            onDismiss = { showIdInfo = false },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SCREEN_BG_COLOR),
    ) {
        CardioTopBar(title = "Профиль", onBack = onBack)

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }

        val metricDisplays = buildMetricDisplays(uiState)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(ITEM_SPACING),
        ) {
            item { Spacer(Modifier.height(4.dp)) }

            item {
                Column(modifier = Modifier.padding(horizontal = CONTENT_HORIZONTAL_PADDING)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp),
                    ) {
                        Text(
                            text = "ID пациента",
                            style = CardioTheme.typography.bodySmall,
                            color = CardioTheme.colors.textSecondary,
                            modifier = Modifier.weight(1f),
                        )
                        Box(
                            modifier = Modifier
                                .size(INFO_BUTTON_SIZE)
                                .clip(CircleShape)
                                .background(CardioTheme.colors.textDisabled)
                                .clickable { showIdInfo = true },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "?",
                                style = CardioTheme.typography.navLabel,
                                color = CardioTheme.colors.textSecondary,
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ProfileInputField(
                            value = uiState.patientId,
                            onValueChange = onPatientIdChange,
                            placeholder = "PT-7GZVL7PT",
                            enabled = !isIdLocked,
                            onDone = { focusManager.clearFocus(); isIdLocked = true },
                            modifier = Modifier.weight(1f),
                        )
                        if (isIdLocked) {
                            Box(
                                modifier = Modifier
                                    .height(60.dp)
                                    .clip(ProfileCardShape)
                                    .background(COLOR_MANUAL_BUTTON)
                                    .clickable { isIdLocked = false }
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Text(
                                        text = "Изменить",
                                        style = CardioTheme.typography.navLabel,
                                        color = CardioTheme.colors.textMain,
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = CardioTheme.colors.textMain,
                                        modifier = Modifier.size(16.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = CONTENT_HORIZONTAL_PADDING)) {
                    Text(
                        text = "Дата рождения",
                        style = CardioTheme.typography.bodySmall,
                        color = CardioTheme.colors.textSecondary,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    DateInputField(
                        value = uiState.birthDate.toDisplayDate(),
                        onClick = { showDatePicker = true },
                    )
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = CONTENT_HORIZONTAL_PADDING)) {
                    Text(
                        text = "Возрастная группа",
                        style = CardioTheme.typography.bodySmall,
                        color = CardioTheme.colors.textSecondary,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp).padding(bottom = 8.dp),
                    )
                    AgeGroupDropdown(
                        selected = uiState.ageGroup,
                        useCustomLimits = uiState.useCustomLimits,
                        onSelect = onAgeGroupChange,
                    )
                }
            }

            item {
                LegendCard(modifier = Modifier.padding(horizontal = CONTENT_HORIZONTAL_PADDING))
            }

            items(metricDisplays) { metric ->
                MetricBlock(
                    metric = metric,
                    onClick = { onEditMetric(metric.metricType) },
                    modifier = Modifier.padding(horizontal = CONTENT_HORIZONTAL_PADDING),
                )
            }

            item {
                Button(
                    onClick = onOpenCustomLimits,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .padding(horizontal = CONTENT_HORIZONTAL_PADDING),
                    shape = ProfileCardShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = COLOR_MANUAL_BUTTON,
                        contentColor = CardioTheme.colors.textSecondary,
                    ),
                ) {
                    Text("Заполнить вручную", style = CardioTheme.typography.bodyMedium)
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            item {
                MedicationsCard(
                    takesWarfarin = uiState.takesWarfarin,
                    onTakesWarfarinChange = onTakesWarfarinChange,
                    modifier = Modifier.padding(horizontal = CONTENT_HORIZONTAL_PADDING),
                )
            }

            item {
                NotificationsCard(
                    enabled = uiState.notificationsEnabled,
                    onEnabledChange = onNotificationsEnabledChange,
                    modifier = Modifier.padding(horizontal = CONTENT_HORIZONTAL_PADDING),
                )
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

// ── Metric display helpers ────────────────────────────────────────────────────

private fun buildMetricDisplays(uiState: ProfileUiState): List<MetricDisplay> {
    val limits = uiState.limitsForAgeGroup ?: return emptyList()
    val edit = uiState.customLimitsEdit
    val useCustom = uiState.useCustomLimits && edit != null
    val result = mutableListOf<MetricDisplay>()

    if (useCustom && edit != null && edit.hrNormalMin.isNotBlank()) {
        result.add(
            MetricDisplay(
                metricType = MetricType.HeartRate,
                title = "ЧСС (уд/мин)",
                normalText = "${edit.hrNormalMin}–${edit.hrNormalMax}",
                attentionText = edit.hrAttentionMin.takeIf { it.isNotBlank() }
                    ?.let { "${it}–${edit.hrAttentionMax}" },
                doctorText = edit.hrDoctorMax.takeIf { it.isNotBlank() }?.let { "< $it" },
            )
        )
    } else {
        limits.singleMetricLimits.find { it.metricType == MetricType.HeartRate }
            ?.let { result.add(it.toDisplay("ЧСС (уд/мин)")) }
    }

    if (useCustom && edit != null && edit.bpSystolicMin.isNotBlank()) {
        result.add(
            MetricDisplay(
                metricType = MetricType.BloodPressure,
                title = "АД (мм рт. ст.)",
                normalText = "${edit.bpSystolicMin}–${edit.bpSystolicMax}/${edit.bpDiastolicMin}–${edit.bpDiastolicMax}",
                attentionText = null,
                doctorText = null,
            )
        )
    } else {
        limits.bloodPressureLimits?.let { result.add(it.toDisplay()) }
    }

    if (useCustom && edit != null && edit.spO2NormalMin.isNotBlank()) {
        result.add(
            MetricDisplay(
                metricType = MetricType.OxygenSaturation,
                title = "Сатурация (%)",
                normalText = "${edit.spO2NormalMin}–${edit.spO2NormalMax}",
                attentionText = edit.spO2AttentionMin.takeIf { it.isNotBlank() }
                    ?.let { "${it}–${edit.spO2AttentionMax}" },
                doctorText = edit.spO2DoctorMax.takeIf { it.isNotBlank() }?.let { "< $it" },
            )
        )
    } else {
        limits.singleMetricLimits.find { it.metricType == MetricType.OxygenSaturation }
            ?.let { result.add(it.toDisplay("Сатурация (%)")) }
    }

    if (uiState.takesWarfarin) {
        result.add(
            if (useCustom && edit != null && edit.inrNormalMin.isNotBlank()) {
                MetricDisplay(
                    metricType = MetricType.INR,
                    title = "МНО",
                    normalText = "${edit.inrNormalMin}–${edit.inrNormalMax}",
                    attentionText = edit.inrAttentionMin.takeIf { it.isNotBlank() }
                        ?.let { "${it}–${edit.inrAttentionMax}" },
                    doctorText = edit.inrDoctorMax.takeIf { it.isNotBlank() }?.let { "< $it" },
                )
            } else {
                MetricDisplay(MetricType.INR, "МНО", "Не задано", null, null)
            }
        )
    }

    return result
}

private fun SingleMetricLimits.toDisplay(title: String): MetricDisplay {
    val attention = if (attentionMin != null && attentionMax != null) "$attentionMin–$attentionMax" else null
    val doctor = doctorSoonMax?.let { "< $it" }
    return MetricDisplay(metricType, title, "$normalMin–$normalMax", attention, doctor)
}

private fun BloodPressureLimits.toDisplay(): MetricDisplay {
    val normal = "$normalSystolicMin–$normalSystolicMax/$normalDiastolicMin–$normalDiastolicMax"
    val doctor = "<$doctorSoonSystolicLow/$doctorSoonDiastolicLow или >$doctorSoonSystolicHigh/$doctorSoonDiastolicHigh"
    return MetricDisplay(MetricType.BloodPressure, "АД (мм рт. ст.)", normal, null, doctor)
}

// ── Date helpers ──────────────────────────────────────────────────────────────

private fun String.parseToMillis(): Long? = try {
    SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.parse(this)?.time
} catch (_: Exception) { null }

private fun String.toDisplayDate(): String = try {
    val inFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }
    val outFmt = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    inFmt.parse(this)?.let { outFmt.format(it) } ?: this
} catch (_: Exception) { this }

private fun millisToIso(millis: Long): String =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date(millis))
