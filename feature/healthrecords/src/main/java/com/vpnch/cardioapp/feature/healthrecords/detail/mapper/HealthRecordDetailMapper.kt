package com.vpnch.cardioapp.feature.healthrecords.detail.mapper

import com.vpnch.cardioapp.core.domain.usecase.EvaluateHealthRecordLimitsUseCase
import com.vpnch.cardioapp.core.model.health.limits.MetricLimitsBundle
import com.vpnch.cardioapp.core.model.health.HealthRecord
import com.vpnch.cardioapp.core.model.health.formatAsHealthMetric
import com.vpnch.cardioapp.core.model.health.formatAsInr
import com.vpnch.cardioapp.core.model.health.formatBloodPressure
import com.vpnch.cardioapp.core.model.health.isOutOfNorm
import com.vpnch.cardioapp.core.model.health.metrics.MetricStatus
import com.vpnch.cardioapp.feature.healthrecords.model.HealthMetricKind
import com.vpnch.cardioapp.feature.healthrecords.ui.extensions.displayTitle
import com.vpnch.cardioapp.feature.healthrecords.detail.model.MetricItem

fun HealthRecord.toMetricItems(
    limits: MetricLimitsBundle?,
    evaluateLimits: EvaluateHealthRecordLimitsUseCase,
): List<MetricItem> {
    val evaluation = limits?.let {
        evaluateLimits.evaluate(this, it.singleLimits, it.bloodPressureLimits)
    }

    fun createItem(
        kind: HealthMetricKind,
        title: String,
        value: String,
        status: MetricStatus?
    ): MetricItem = MetricItem(
        kind = kind,
        title = title,
        value = value,
        isOutOfNorm = status.isOutOfNorm(),
        isCritical = status == MetricStatus.DoctorSoon,
    )

    return buildList {
        add(
            createItem(
                kind = HealthMetricKind.BloodPressure,
                title = HealthMetricKind.BloodPressure.displayTitle,
                value = formatBloodPressure(systolicPressure, diastolicPressure),
                status = evaluation?.bloodPressure
            )
        )
        add(
            createItem(
                kind = HealthMetricKind.RespiratoryRate,
                title = HealthMetricKind.RespiratoryRate.displayTitle,
                value = respiratoryRate.formatAsHealthMetric(),
                status = evaluation?.respiratoryRate
            )
        )
        add(
            createItem(
                kind = HealthMetricKind.HeartRate,
                title = HealthMetricKind.HeartRate.displayTitle,
                value = heartRate.formatAsHealthMetric(),
                status = evaluation?.heartRate
            )
        )
        add(
            createItem(
                kind = HealthMetricKind.OxygenSaturation,
                title = HealthMetricKind.OxygenSaturation.displayTitle,
                value = oxygenSaturation.formatAsHealthMetric(),
                status = evaluation?.oxygenSaturation
            )
        )
        inr?.let { inrValue ->
            add(
                createItem(
                    kind = HealthMetricKind.INR,
                    title = HealthMetricKind.INR.displayTitle,
                    value = inrValue.formatAsInr(),
                    status = evaluation?.inr
                )
            )
        }
    }
}