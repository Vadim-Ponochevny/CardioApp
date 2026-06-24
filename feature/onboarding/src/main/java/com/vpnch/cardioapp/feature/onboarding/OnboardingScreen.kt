package com.vpnch.cardioapp.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.ui.theme.CardioTheme

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    onPatientIdChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Добро\nпожаловать",
            style = CardioTheme.typography.screenTitle,
            color = CardioTheme.colors.textMain,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Введите данные пациента чтобы начать",
            style = CardioTheme.typography.bodySmall,
            color = CardioTheme.colors.textSecondary,
        )
        Spacer(Modifier.height(32.dp))

        Text(
            text = "ID пациента",
            style = CardioTheme.typography.bodySmall,
            color = CardioTheme.colors.textSecondary,
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = uiState.patientId,
            onValueChange = onPatientIdChange,
            placeholder = { Text("PT-7GZVL7PT", color = CardioTheme.colors.textDisabled) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CardioTheme.colors.primary,
                unfocusedBorderColor = CardioTheme.colors.textDisabled,
                focusedTextColor = CardioTheme.colors.textMain,
                unfocusedTextColor = CardioTheme.colors.textMain,
            ),
        )
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Дата рождения",
            style = CardioTheme.typography.bodySmall,
            color = CardioTheme.colors.textSecondary,
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = uiState.birthDate,
            onValueChange = onBirthDateChange,
            placeholder = { Text("ГГГГ-ММ-ДД", color = CardioTheme.colors.textDisabled) },
            modifier = Modifier.fillMaxWidth(),
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
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onContinue,
            enabled = uiState.patientId.isNotBlank() && uiState.birthDate.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CardioTheme.colors.primary,
                contentColor = CardioTheme.colors.onPrimary,
                disabledContainerColor = CardioTheme.colors.textDisabled,
                disabledContentColor = CardioTheme.colors.onPrimary,
            ),
        ) {
            Text("Продолжить", style = CardioTheme.typography.actionLabel)
        }
    }
}
