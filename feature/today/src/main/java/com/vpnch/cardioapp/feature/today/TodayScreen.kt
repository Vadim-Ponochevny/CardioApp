package com.vpnch.cardioapp.feature.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.SurveyLink
import com.vpnch.cardioapp.core.model.Vitamin
import com.vpnch.cardioapp.core.model.VitaminIntake
import com.vpnch.cardioapp.core.model.VitaminIntakeSummary
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.CardioPreviewTheme
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.today.components.HealthRecordsCard
import com.vpnch.cardioapp.feature.today.components.SurveyCard
import com.vpnch.cardioapp.feature.today.components.VitaminsSection
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@Composable
fun TodayScreen(
    uiState: TodayUiState,
    onOpenLatestRecord: (String) -> Unit,
    onAddHealthRecord: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSurvey: (SurveyLink) -> Unit,
    onVitaminCheckedChange: (VitaminIntakeSummary, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp) // отступы только слева и справа
            .padding(top = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column {
                Text(
                    text = "Сегодня",
                    style = CardioTheme.typography.screenTitle,
                    color = CardioTheme.colors.textMain,
                )
                Text(
                    text = getFormattedDate(),
                    style = CardioTheme.typography.bodySmall,
                    color = CardioTheme.colors.textSecondary,
                )
            }
            IconButton(onClick = onOpenProfile) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Профиль",
                    modifier = Modifier.size(28.dp),
                    tint = CardioTheme.colors.textSecondary,
                )
            }
        }

        HealthRecordsCard(
            latestRecord = uiState.latestRecord,
            onOpenLatestRecord = onOpenLatestRecord,
        )

        Button(
            onClick = onAddHealthRecord,
            modifier = Modifier.fillMaxWidth().height(88.dp),
            shape = RoundedCornerShape(36.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CardioTheme.colors.primary,
                contentColor = CardioTheme.colors.onPrimary,
            ),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center, // центр по горизонтали
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Новая запись",
                    style = CardioTheme.typography.actionLabel,
                )
                Spacer(modifier = Modifier.width(18.dp))
                Icon(
                    painter = painterResource(id = R.drawable.plus1),
                    contentDescription = "Добавить",
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        VitaminsSection(
            vitaminIntakes = uiState.vitaminIntakes,
            onVitaminCheckedChange = onVitaminCheckedChange,
        )

//        SurveyCard(
//            surveyLink = uiState.surveyLink,
//            onOpenSurvey = onOpenSurvey,
//        )
    }
}

@Composable
fun getFormattedDate(): String {
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val months = arrayOf(
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )
    val month = months[calendar.get(Calendar.MONTH)]

    val weekdays = arrayOf(
        "воскресенье", "понедельник", "вторник", "среда",
        "четверг", "пятница", "суббота"
    )
    val weekday = weekdays[calendar.get(Calendar.DAY_OF_WEEK) - 1]

    return "$day $month, $weekday"
}

@CardioPreview
@Composable
private fun TodayScreenPreview() {
    CardioPreviewTheme {
        TodayScreenPreviewContent()
    }
}

@Composable
private fun TodayScreenPreviewContent() {
    TodayScreen(
        uiState = TodayUiState(
            isLoading = false,
            latestRecord = LatestHealthRecordSummary(
                recordId = "record-1",
                timeLabel = "17:00",
                hasOutOfNorm = true,
                hasCritical = true,
                metrics = listOf(
                    TodayMetricItem("Давление", "110/80", TodayMetricIcons.BLOOD_PRESSURE, false),
                    TodayMetricItem("Дыхание", "18", TodayMetricIcons.RESPIRATORY, false),
                    TodayMetricItem("Пульс", "90", TodayMetricIcons.HEART_RATE, true),
                    TodayMetricItem("Кислород", "98", TodayMetricIcons.OXYGEN, false),
                ),
            ),
            vitaminIntakes = listOf(
                VitaminIntakeSummary(
                    vitamin = Vitamin(
                        id = "vitamin-1",
                        patientId = "patient",
                        name = "Витаминка утром",
                        dose = "1 шт.",
                    ),
                    intake = VitaminIntake(
                        id = "intake-1",
                        intakeDayId = "day",
                        vitaminId = "vitamin-1",
                        isTaken = true,
                        takenAt = 0L,
                        updatedAt = 0L,
                    ),
                ),
                VitaminIntakeSummary(
                    vitamin = Vitamin(
                        id = "vitamin-2",
                        patientId = "patient",
                        name = "Витаминка вечером",
                        dose = "1 шт.",
                    ),
                    intake = null,
                ),
            ),
            surveyLink = SurveyLink(
                id = "survey",
                title = "Опрос",
                url = "https://forms.yandex.ru/",
                isActive = true,
                createdAt = 0L,
                updatedAt = 0L,
            ),
        ),
        onOpenLatestRecord = {},
        onAddHealthRecord = {},
        onOpenProfile = {},
        onOpenSurvey = {},
        onVitaminCheckedChange = { _, _ -> },
    )
}

