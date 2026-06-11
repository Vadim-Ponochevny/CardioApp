package com.vpnch.cardioapp.feature.healthrecords.create

enum class HealthRecordCreatePage(val title: String) {
    BloodPressure("Давление"),
    RespiratoryRate("Дыхание"),
    HeartRate("Пульс"),
    OxygenSaturation("Кислород"),
}

data object FieldWarning

data class HealthRecordCreateUiState(
    val currentPage: Int = 0,
    val systolicInput: String = "",
    val diastolicInput: String = "",
    val respiratoryInput: String = "",
    val heartRateInput: String = "",
    val oxygenInput: String = "",
    val bloodPressureWarning: FieldWarning? = null,
    val respiratoryWarning: FieldWarning? = null,
    val heartRateWarning: FieldWarning? = null,
    val oxygenWarning: FieldWarning? = null,
    val expectedRangeLabel: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val savedWithDoctorAlert: Boolean = false,
    val saveError: String? = null,
    val loadError: String? = null,
    val canProceed: Boolean = false,
    val primaryButtonLabel: String = "Далее",
) {
    val pageCount: Int = HealthRecordCreatePage.entries.size

    val progressLabel: String
        get() = "${currentPage + 1} из $pageCount"

    val currentTitle: String
        get() = HealthRecordCreatePage.entries[currentPage].title
}
