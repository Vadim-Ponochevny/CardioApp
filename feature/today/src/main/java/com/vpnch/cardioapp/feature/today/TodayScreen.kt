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
import androidx.compose.ui.unit.dp
import com.vpnch.cardioapp.core.model.survey.SurveyLink
import com.vpnch.cardioapp.core.model.vitamins.Vitamin
import com.vpnch.cardioapp.core.model.vitamins.VitaminIntake
import com.vpnch.cardioapp.core.model.vitamins.VitaminIntakeSummary
import com.vpnch.cardioapp.core.ui.CardioPreview
import com.vpnch.cardioapp.core.ui.CardioPreviewTheme
import com.vpnch.cardioapp.core.ui.theme.CardioTheme
import com.vpnch.cardioapp.feature.today.components.HealthRecordsCard
import com.vpnch.cardioapp.feature.today.components.VitaminsSection
import java.util.Calendar

private val SCREEN_HORIZONTAL_PADDING = 12.dp
private val SCREEN_TOP_PADDING = 12.dp
private val SECTION_SPACING = 16.dp
private val ADD_BUTTON_HEIGHT = 68.dp
private val ADD_BUTTON_CORNER = 36.dp
private val ADD_BUTTON_ICON_SPACING = 18.dp
private val ADD_BUTTON_ICON_SIZE = 26.dp
private val PROFILE_ICON_SIZE = 28.dp

@Composable
fun TodayScreen(
    uiState: TodayUiState,
    onOpenLatestRecord: (String) -> Unit,
    onAddHealthRecord: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSurvey: (SurveyLink) -> Unit,
    onVitaminCheckedChange: (VitaminIntakeSummary, Boolean) -> Unit,
    onOpenVitamins: () -> Unit,
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
            .padding(horizontal = SCREEN_HORIZONTAL_PADDING)
            .padding(top = SCREEN_TOP_PADDING)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(SECTION_SPACING),
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
                    modifier = Modifier.size(PROFILE_ICON_SIZE),
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
            modifier = Modifier.fillMaxWidth().height(ADD_BUTTON_HEIGHT),
            shape = RoundedCornerShape(ADD_BUTTON_CORNER),
            colors = ButtonDefaults.buttonColors(
                containerColor = CardioTheme.colors.primary,
                contentColor = CardioTheme.colors.onPrimary,
            ),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Создать запись",
                    style = CardioTheme.typography.itemTitle,
                )
                Spacer(modifier = Modifier.width(ADD_BUTTON_ICON_SPACING))
                Icon(
                    painter = painterResource(id = R.drawable.plus1),
                    contentDescription = "Добавить",
                    modifier = Modifier.size(ADD_BUTTON_ICON_SIZE),
                )
            }
        }

        VitaminsSection(
            vitaminIntakes = uiState.vitaminIntakes,
            onVitaminCheckedChange = onVitaminCheckedChange,
            onOpenVitamins = onOpenVitamins,
        )

        Spacer(modifier = Modifier.height(24.dp))

//        SurveyCard(
//            surveyLink = uiState.surveyLink,
//            onOpenSurvey = onOpenSurvey,
//        )
    }
}

private fun getFormattedDate(): String {
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val months = arrayOf(
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря",
    )
    val weekdays = arrayOf(
        "воскресенье", "понедельник", "вторник", "среда",
        "четверг", "пятница", "суббота",
    )
    val month = months[calendar.get(Calendar.MONTH)]
    val weekday = weekdays[calendar.get(Calendar.DAY_OF_WEEK) - 1]
    return "$day $month, $weekday"
}

@CardioPreview
@Composable
private fun TodayScreenPreview() {
    CardioPreviewTheme {
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
                        vitamin = Vitamin(id = "v1", patientId = "p", name = "Витаминка утром", dose = "1 шт."),
                        intake = VitaminIntake(id = "i1", intakeDayId = "day", vitaminId = "v1", isTaken = true, takenAt = 0L, updatedAt = 0L),
                    ),
                    VitaminIntakeSummary(
                        vitamin = Vitamin(id = "v2", patientId = "p", name = "Витаминка вечером", dose = "1 шт."),
                        intake = null,
                    ),
                ),
                surveyLink = SurveyLink(
                    id = "survey", title = "Опрос", url = "https://forms.yandex.ru/",
                    isActive = true, createdAt = 0L, updatedAt = 0L,
                ),
            ),
            onOpenLatestRecord = {},
            onAddHealthRecord = {},
            onOpenProfile = {},
            onOpenSurvey = {},
            onVitaminCheckedChange = { _, _ -> },
            onOpenVitamins = {},
        )
    }
}
