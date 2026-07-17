@file:OptIn(ExperimentalMaterial3Api::class)

package com.vpnch.cardioapp.feature.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import com.vpnch.cardioapp.core.ui.R as CoreUiR

private val InputShape = RoundedCornerShape(36.dp)
private val TopBarBg = Color(0xFFF6F6F6)

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    onPatientIdChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val step = uiState.currentStep
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

    Scaffold(
        modifier = modifier,
        containerColor = CardioTheme.colors.background,
        topBar = {
            if (step > 0) {
                // Используем Box вместо TopAppBar, чтобы полностью контролировать высоту и форму
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TopBarBg)
                        .statusBarsPadding() // Автоматический отступ под статус-бар (чтобы кнопка не лезла на часы)
                        .padding(horizontal = 16.dp, vertical = 8.dp) // Внешние отступы для кнопки
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(68.dp) // Теперь размер применится честно и кнопка будет идеально круглой
                            .background(CardioTheme.colors.onPrimary, CircleShape),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = CoreUiR.drawable.ic_back),
                            contentDescription = "Назад",
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(16.dp))
                when (step) {
                    0 -> StepWelcome()
                    1 -> StepParentHandoff()
                    2 -> StepPatientData(
                        uiState = uiState,
                        onPatientIdChange = onPatientIdChange,
                        onDateClick = { showDatePicker = true },
                    )
                }
            }

            // Progress indicator
            Text(
                text = "${step + 1} из 3",
                style = CardioTheme.typography.bodySmall,
                color = CardioTheme.colors.textSecondary,
            )
            Spacer(Modifier.height(8.dp))

            uiState.saveError?.let { error ->
                Text(
                    text = error,
                    style = CardioTheme.typography.bodySmall,
                    color = CardioTheme.colors.warningContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }

            // Main action button
            Button(
                onClick = onContinue,
                enabled = step < 2 || (uiState.patientId.isNotBlank() && uiState.birthDate.isNotBlank()),
                modifier = Modifier.fillMaxWidth().height(72.dp),
                shape = InputShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardioTheme.colors.primary,
                    contentColor = CardioTheme.colors.onPrimary,
                    disabledContainerColor = CardioTheme.colors.textDisabled,
                    disabledContentColor = CardioTheme.colors.onPrimary,
                ),
            ) {
                Text(
                    text = if (step == 2) "Сохранить" else "Далее",
                    style = CardioTheme.typography.buttonPrimary,
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

// ── Step screens ──────────────────────────────────────────────────────────────

@Composable
private fun StepWelcome() {
    // Image placeholder — max 360dp tall, full width
    Image(
        painter = painterResource(id = R.drawable.welcome_image),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        contentScale = ContentScale.Fit // Помещает картинку целиком внутрь области 240.dp
    )
    Spacer(Modifier.height(32.dp))
    Text(
        text = "Привет!",
        style = CardioTheme.typography.cardTitle,
        color = CardioTheme.colors.textMain,
        textAlign = TextAlign.Center,
    )
    Spacer(Modifier.height(16.dp))
    Text(
        text = "Я помогу следить за здоровьем.\nДавай познакомимся, это займёт 2 минуты",
        style = CardioTheme.typography.bodyLarge,
        color = CardioTheme.colors.textMain,
        textAlign = TextAlign.Center,
    )
}
@Composable
private fun StepParentHandoff() {
    // Добавляем Column, чтобы элементы шли строго друг за другом сверху вниз
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally // Выравнивает все элементы по центру экрана
    ) {
        // Yellow attention chip — wraps text
        Box(
            modifier = Modifier
                .background(CardioTheme.colors.warningContainer, RoundedCornerShape(36.dp))
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            Text(
                text = "Внимание",
                style = CardioTheme.typography.screenTitle,
                color = Color.Black.copy(alpha = 0.8f),
            )
        }

        Spacer(Modifier.height(24.dp))

        // Изменяем картинку: задаем ей точный размер
        Image(
            painter = painterResource(id = R.drawable.warning_image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Для продолжения нужно передать устройство родителю",
            style = CardioTheme.typography.bodyLarge,
            color = CardioTheme.colors.textMain,
            textAlign = TextAlign.Center,
        )
    }
}
@Composable
private fun StepPatientData(
    uiState: OnboardingUiState,
    onPatientIdChange: (String) -> Unit,
    onDateClick: () -> Unit,
) {
    // Patient ID
    OnboardingInputField(
        label = "ID пациента",
        value = uiState.patientId,
        onValueChange = onPatientIdChange,
        placeholder = "PT-7GZVL7PT",
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(12.dp))
    // Birth date
    OnboardingDateField(
        label = "Дата рождения",
        value = uiState.birthDate.toDisplayDate(),
        onClick = onDateClick,
    )
    Spacer(Modifier.height(24.dp))
    // Info note
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                CardioTheme.colors.textDisabled.copy(alpha = 0.12f),
                RoundedCornerShape(20.dp),
            )
            .padding(16.dp),
    ) {
        Text(
            text = "Настройка критических значений для показателей доступна в профиле на главной странице",
            style = CardioTheme.typography.bodySmall,
            color = CardioTheme.colors.textSecondary,
        )
    }
}

// ── Input components ─────────────────────────────────────────────────────────

@Composable
private fun OnboardingInputField(
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
                .height(88.dp)
                .border(4.dp, strokeColor, InputShape),
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

@Composable
private fun OnboardingDateField(
    label: String,
    value: String,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = CardioTheme.typography.bodySmall, color = CardioTheme.colors.textSecondary)
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .border(4.dp, CardioTheme.colors.textDisabled, InputShape)
                .clickable(onClick = onClick)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = value.ifBlank { "ДД.ММ.ГГГГ" },
                style = CardioTheme.typography.inputValue.copy(
                    color = if (value.isBlank()) CardioTheme.colors.textDisabled else CardioTheme.colors.textMain,
                ),
            )
        }
    }
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
