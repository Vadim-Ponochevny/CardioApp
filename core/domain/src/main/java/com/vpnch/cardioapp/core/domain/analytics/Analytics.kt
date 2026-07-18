package com.vpnch.cardioapp.core.domain.analytics

interface Analytics {
    fun report(event: AnalyticsEvent)
}

sealed class AnalyticsEvent(val name: String) {
    data object AppOpened : AnalyticsEvent("app_opened")
    data object FirstOpen : AnalyticsEvent("first_open")
    data object MeasurementAdded : AnalyticsEvent("measurement_added")
    data object MedicineMarked : AnalyticsEvent("medicine_marked")
    data object QuestionnaireOpened : AnalyticsEvent("questionnaire_opened")
    data object QuestionnaireCompleted : AnalyticsEvent("questionnaire_completed")
    data object AbnormalValueDetected : AnalyticsEvent("abnormal_value_detected")
    data object CallButtonPressed : AnalyticsEvent("call_button_pressed")
    data object OpenedFromDailyNotification : AnalyticsEvent("opened_from_daily_notification")
    data object OpenedFromSurveyNotification : AnalyticsEvent("opened_from_survey_notification")
}
