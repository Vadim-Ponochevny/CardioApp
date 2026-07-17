@file:OptIn(ExperimentalMaterial3Api::class)

package com.vpnch.cardioapp.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
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
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.health.limits.BloodPressureLimits
import com.vpnch.cardioapp.core.model.health.limits.SingleMetricLimits
import com.vpnch.cardioapp.core.model.health.metrics.MetricType
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.core.model.patient.ValveType
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

private val SCREEN_BG_COLOR = androidx.compose.ui.graphics.Color(0xFFF6F6F6)
private val CONTENT_HORIZONTAL_PADDING = 16.dp
private val ITEM_SPACING = 12.dp

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
    var showDatePicker by remember { mutableStateOf(false) }
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
        ) { DatePicker(state = datePickerState) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SCREEN_BG_COLOR),
    ) {
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Box
        }

        val metricDisplays = buildMetricDisplays(uiState)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(ITEM_SPACING),
        ) {
            item { CardioTopBar(title = "Профиль", onBack = onBack) }

            item { Spacer(Modifier.height(4.dp)) }

            item {
                ProfileInputField(
                    label = "ID пациента",
                    value = uiState.patientId,
                    onValueChange = onPatientIdChange,
                    placeholder = "PT-7GZVL7PT",
                    modifier = Modifier.padding(horizontal = CONTENT_HORIZONTAL_PADDING),
                )
            }

            item {
                DateInputField(
                    label = "Дата рождения",
                    value = uiState.birthDate.toDisplayDate(),
                    onClick = { showDatePicker = true },
                    modifier = Modifier.padding(horizontal = CONTENT_HORIZONTAL_PADDING),
                )
            }

            item {
                Text(
                    text = "Возрастная группа",
                    style = CardioTheme.typography.cardTitle,
                    color = CardioTheme.colors.textMain,
                    modifier = Modifier.padding(horizontal = CONTENT_HORIZONTAL_PADDING + 4.dp),
                )
            }

            item {
                AgeGroupDropdown(
                    selected = uiState.ageGroup,
                    useCustomLimits = uiState.useCustomLimits,
                    onSelect = onAgeGroupChange,
                    modifier = Modifier.padding(horizontal = CONTENT_HORIZONTAL_PADDING),
                )
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
                        containerColor = CardioTheme.colors.onPrimary,
                        contentColor = CardioTheme.colors.textMain,
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
                    valveType = uiState.valveType,
                    onTakesWarfarinChange = onTakesWarfarinChange,
                    onValveTypeChange = onValveTypeChange,
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
    val result = mutableListOf<MetricDisplay>()

    limits.singleMetricLimits.find { it.metricType == MetricType.HeartRate }
        ?.let { result.add(it.toDisplay("ЧСС (уд/мин)")) }
    limits.bloodPressureLimits?.let { result.add(it.toDisplay()) }
    limits.singleMetricLimits.find { it.metricType == MetricType.OxygenSaturation }
        ?.let { result.add(it.toDisplay("Сатурация (%)")) }

    if (uiState.takesWarfarin) {
        result.add(
            if (uiState.useCustomLimits && edit != null && edit.inrNormalMin.isNotBlank()) {
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
