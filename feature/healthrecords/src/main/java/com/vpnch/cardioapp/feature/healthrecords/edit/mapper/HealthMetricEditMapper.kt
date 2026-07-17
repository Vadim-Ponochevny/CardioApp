package com.vpnch.cardioapp.feature.healthrecords.edit.mapper

import com.vpnch.cardioapp.core.model.health.HealthRecord
import com.vpnch.cardioapp.core.model.health.InrConverter
import com.vpnch.cardioapp.core.model.health.formatAsInr
import com.vpnch.cardioapp.feature.healthrecords.model.HealthMetricKind
import com.vpnch.cardioapp.feature.healthrecords.ui.extensions.displayTitle
import com.vpnch.cardioapp.feature.healthrecords.edit.HealthMetricEditUiState
import com.vpnch.cardioapp.feature.healthrecords.utils.toPositiveInt

internal fun HealthRecord.toEditUiState(kind: HealthMetricKind): HealthMetricEditUiState =
    when (kind) {
        HealthMetricKind.BloodPressure -> HealthMetricEditUiState(
            metricKind = kind,
            title = kind.displayTitle,
            isBloodPressure = true,
            systolicInput = systolicPressure?.toString().orEmpty(),
            diastolicInput = diastolicPressure?.toString().orEmpty(),
        )
        HealthMetricKind.RespiratoryRate -> HealthMetricEditUiState(
            metricKind = kind,
            title = kind.displayTitle,
            singleInput = respiratoryRate?.toString().orEmpty(),
            singleLabel = "22",
        )
        HealthMetricKind.HeartRate -> HealthMetricEditUiState(
            metricKind = kind,
            title = kind.displayTitle,
            singleInput = heartRate?.toString().orEmpty(),
            singleLabel = "70",
        )
        HealthMetricKind.OxygenSaturation -> HealthMetricEditUiState(
            metricKind = kind,
            title = kind.displayTitle,
            singleInput = oxygenSaturation?.toString().orEmpty(),
            singleLabel = "95",
        )
        HealthMetricKind.INR -> HealthMetricEditUiState(
            metricKind = kind,
            title = kind.displayTitle,
            isInr = true,
            singleInput = inr?.formatAsInr().orEmpty(),
        )
    }

internal fun HealthMetricEditUiState.applyToRecord(record: HealthRecord): HealthRecord {
    val now = System.currentTimeMillis()
    return when (metricKind) {
        HealthMetricKind.BloodPressure -> record.copy(
            updatedAt = now,
            systolicPressure = systolicInput.toPositiveInt(),
            diastolicPressure = diastolicInput.toPositiveInt(),
        )
        HealthMetricKind.RespiratoryRate -> record.copy(
            updatedAt = now,
            respiratoryRate = singleInput.toPositiveInt(),
        )
        HealthMetricKind.HeartRate -> record.copy(
            updatedAt = now,
            heartRate = singleInput.toPositiveInt(),
        )
        HealthMetricKind.OxygenSaturation -> record.copy(
            updatedAt = now,
            oxygenSaturation = singleInput.toPositiveInt(),
        )
        HealthMetricKind.INR -> record.copy(
            updatedAt = now,
            inr = InrConverter.toStoredInt(singleInput),
        )
    }
}
