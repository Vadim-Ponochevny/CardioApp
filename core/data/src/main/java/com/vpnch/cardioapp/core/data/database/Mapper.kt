package com.vpnch.cardioapp.core.data.database

import com.vpnch.cardioapp.core.database.entity.help.FaqEntity
import com.vpnch.cardioapp.core.database.entity.health.limits.BloodPressureLimitEntity
import com.vpnch.cardioapp.core.database.entity.health.HealthRecordEntity
import com.vpnch.cardioapp.core.database.entity.help.HelpContactEntity
import com.vpnch.cardioapp.core.database.entity.health.limits.SingleMetricLimitEntity
import com.vpnch.cardioapp.core.database.entity.survey.SurveyLinkEntity
import com.vpnch.cardioapp.core.database.entity.vitamins.VitaminEntity
import com.vpnch.cardioapp.core.database.projection.VitaminIntakeSummaryEntity
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.core.model.health.limits.BloodPressureLimits
import com.vpnch.cardioapp.core.model.help.Faq
import com.vpnch.cardioapp.core.model.health.HealthRecord
import com.vpnch.cardioapp.core.model.help.HelpContact
import com.vpnch.cardioapp.core.model.health.metrics.MetricType
import com.vpnch.cardioapp.core.model.survey.SurveyLink
import com.vpnch.cardioapp.core.model.health.limits.SingleMetricLimits
import com.vpnch.cardioapp.core.model.vitamins.Vitamin
import com.vpnch.cardioapp.core.model.vitamins.VitaminIntake
import com.vpnch.cardioapp.core.model.vitamins.VitaminIntakeSummary

internal fun HealthRecordEntity.asExternalModel() = HealthRecord(
    id = id,
    patientId = patientId,
    createdAt = createdAt,
    updatedAt = updatedAt,
    systolicPressure = systolicPressure,
    diastolicPressure = diastolicPressure,
    respiratoryRate = respiratoryRate,
    heartRate = heartRate,
    oxygenSaturation = oxygenSaturation,
    inr = inr,
)

internal fun HealthRecord.asEntity() = HealthRecordEntity(
    id = id,
    patientId = patientId,
    createdAt = createdAt,
    updatedAt = updatedAt,
    systolicPressure = systolicPressure,
    diastolicPressure = diastolicPressure,
    respiratoryRate = respiratoryRate,
    heartRate = heartRate,
    oxygenSaturation = oxygenSaturation,
    inr = inr,
)

internal fun SingleMetricLimitEntity.asExternalModel() = SingleMetricLimits(
    id = id,
    ageGroup = AgeGroup.valueOf(ageGroup),
    metricType = MetricType.valueOf(metricType),
    normalMin = normalMin,
    normalMax = normalMax,
    attentionMin = attentionMin,
    attentionMax = attentionMax,
    doctorSoonMin = doctorSoonMin,
    doctorSoonMax = doctorSoonMax,
)

internal fun BloodPressureLimitEntity.asExternalModel() = BloodPressureLimits(
    id = id,
    ageGroup = AgeGroup.valueOf(ageGroup),
    normalSystolicMin = normalSystolicMin,
    normalSystolicMax = normalSystolicMax,
    normalDiastolicMin = normalDiastolicMin,
    normalDiastolicMax = normalDiastolicMax,
    doctorSoonSystolicLow = doctorSoonSystolicLow,
    doctorSoonSystolicHigh = doctorSoonSystolicHigh,
    doctorSoonDiastolicLow = doctorSoonDiastolicLow,
    doctorSoonDiastolicHigh = doctorSoonDiastolicHigh,
)

internal fun VitaminEntity.asExternalModel() = Vitamin(
    id = id,
    patientId = patientId,
    name = name,
    dose = dose,
)

internal fun Vitamin.asEntity() = VitaminEntity(
    id = id,
    patientId = patientId,
    name = name,
    dose = dose,
)

internal fun VitaminIntakeSummaryEntity.asExternalModel() = VitaminIntakeSummary(
    vitamin = Vitamin(
        id = id,
        patientId = patientId,
        name = name,
        dose = dose,
    ),
    intake = intakeId?.let {
        VitaminIntake(
            id = it,
            intakeDayId = requireNotNull(intakeDayId),
            vitaminId = requireNotNull(intakeVitaminId),
            isTaken = isTaken == true,
            takenAt = takenAt,
            updatedAt = requireNotNull(intakeUpdatedAt),
        )
    },
)

internal fun SurveyLinkEntity.asExternalModel() = SurveyLink(
    id = id,
    title = title,
    url = url,
    isActive = isActive,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

internal fun HelpContactEntity.asExternalModel() = HelpContact(
    id = id,
    title = title,
    phone = phone,
    description = description,
    sortOrder = sortOrder,
    isActive = isActive,
)

internal fun SingleMetricLimits.asEntity() = SingleMetricLimitEntity(
    id = id,
    ageGroup = ageGroup.name,
    metricType = metricType.name,
    normalMin = normalMin,
    normalMax = normalMax,
    attentionMin = attentionMin,
    attentionMax = attentionMax,
    doctorSoonMin = doctorSoonMin,
    doctorSoonMax = doctorSoonMax,
)

internal fun BloodPressureLimits.asEntity() = BloodPressureLimitEntity(
    id = id,
    ageGroup = ageGroup.name,
    normalSystolicMin = normalSystolicMin,
    normalSystolicMax = normalSystolicMax,
    normalDiastolicMin = normalDiastolicMin,
    normalDiastolicMax = normalDiastolicMax,
    doctorSoonSystolicLow = doctorSoonSystolicLow,
    doctorSoonSystolicHigh = doctorSoonSystolicHigh,
    doctorSoonDiastolicLow = doctorSoonDiastolicLow,
    doctorSoonDiastolicHigh = doctorSoonDiastolicHigh,
)

internal fun FaqEntity.asExternalModel() = Faq(
    id = id,
    question = question,
    answer = answer,
    sortOrder = sortOrder,
    isActive = isActive,
)
