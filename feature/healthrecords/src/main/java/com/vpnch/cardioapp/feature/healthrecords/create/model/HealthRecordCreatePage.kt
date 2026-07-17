package com.vpnch.cardioapp.feature.healthrecords.create.model

import com.vpnch.cardioapp.feature.healthrecords.model.HealthMetricKind
import com.vpnch.cardioapp.feature.healthrecords.ui.extensions.displayTitle

enum class HealthRecordCreatePage(val kind: HealthMetricKind) {
    BloodPressure(HealthMetricKind.BloodPressure),
    RespiratoryRate(HealthMetricKind.RespiratoryRate),
    HeartRate(HealthMetricKind.HeartRate),
    OxygenSaturation(HealthMetricKind.OxygenSaturation),
    INR(HealthMetricKind.INR),
    ;

    val title: String get() = kind.displayTitle
}
