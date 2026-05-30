package com.vpnch.cardioapp.core.data

import com.vpnch.cardioapp.core.database.entity.FaqEntity
import com.vpnch.cardioapp.core.database.entity.BloodPressureLimitEntity
import com.vpnch.cardioapp.core.database.entity.HealthRecordEntity
import com.vpnch.cardioapp.core.database.entity.HelpContactEntity
import com.vpnch.cardioapp.core.database.entity.SingleMetricLimitEntity
import com.vpnch.cardioapp.core.database.entity.SurveyLinkEntity
import com.vpnch.cardioapp.core.database.entity.VitaminEntity
import com.vpnch.cardioapp.core.database.model.VitaminIntakeSummaryEntity
import com.vpnch.cardioapp.core.model.AgeGroup
import com.vpnch.cardioapp.core.model.BloodPressureLimits
import com.vpnch.cardioapp.core.model.Faq
import com.vpnch.cardioapp.core.model.HealthRecord
import com.vpnch.cardioapp.core.model.HelpContact
import com.vpnch.cardioapp.core.model.MetricType
import com.vpnch.cardioapp.core.model.SurveyLink
import com.vpnch.cardioapp.core.model.SingleMetricLimits
import com.vpnch.cardioapp.core.model.Vitamin
import com.vpnch.cardioapp.core.model.VitaminIntake
import com.vpnch.cardioapp.core.model.VitaminIntakeSummary

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

internal fun FaqEntity.asExternalModel() = Faq(
    id = id,
    question = question,
    answer = answer,
    sortOrder = sortOrder,
    isActive = isActive,
)
