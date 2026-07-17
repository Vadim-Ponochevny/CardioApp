package com.vpnch.cardioapp.feature.healthrecords.create

import com.vpnch.cardioapp.core.model.health.metrics.MetricStatus
import com.vpnch.cardioapp.feature.healthrecords.create.model.HealthRecordCreatePage

data class HealthRecordCreateUiState(
    val currentPage: HealthRecordCreatePage = HealthRecordCreatePage.BloodPressure,
    val systolicInput: String = "",
    val diastolicInput: String = "",
    val respiratoryInput: String = "",
    val heartRateInput: String = "",
    val oxygenInput: String = "",
    val inrInput: String = "",
    val showInrPage: Boolean = false,
    val bloodPressureWarning: MetricStatus? = null,
    val respiratoryWarning: MetricStatus? = null,
    val heartRateWarning: MetricStatus? = null,
    val oxygenWarning: MetricStatus? = null,
    val inrWarning: MetricStatus? = null,
    val systolicPlaceholder: String = "",
    val diastolicPlaceholder: String = "",
    val metricPlaceholder: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val savedWithDoctorAlert: Boolean = false,
    val saveError: String? = null,
    val loadError: String? = null,
    val canProceed: Boolean = false,
    val primaryButtonLabel: String = "Далее",
) {
    val availablePages: List<HealthRecordCreatePage>
        get() = if (showInrPage) HealthRecordCreatePage.entries
                else HealthRecordCreatePage.entries.filter { it != HealthRecordCreatePage.INR }

    val pageCount: Int get() = availablePages.size
    val lastPageIndex: Int get() = pageCount - 1

    val progressLabel: String
        get() = "${availablePages.indexOf(currentPage) + 1} из $pageCount"

    val currentTitle: String
        get() = currentPage.title
}

