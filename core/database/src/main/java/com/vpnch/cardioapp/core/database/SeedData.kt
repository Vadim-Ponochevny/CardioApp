package com.vpnch.cardioapp.core.database

import com.vpnch.cardioapp.core.database.entity.FaqEntity
import com.vpnch.cardioapp.core.database.entity.BloodPressureLimitEntity
import com.vpnch.cardioapp.core.database.entity.HelpContactEntity
import com.vpnch.cardioapp.core.database.entity.SingleMetricLimitEntity
import com.vpnch.cardioapp.core.database.entity.SurveyLinkEntity
import com.vpnch.cardioapp.core.database.entity.VitaminEntity

object SeedData {
    const val PATIENT_ID = "test-patient"

    val singleMetricLimits = listOf(
        SingleMetricLimitEntity(
            id = "age-10-11-respiratory",
            ageGroup = "Age10To11",
            metricType = "RespiratoryRate",
            normalMin = 18,
            normalMax = 24,
            attentionMin = 12,
            attentionMax = 18,
            doctorSoonMin = null,
            doctorSoonMax = 12,
        ),
        SingleMetricLimitEntity(
            id = "age-10-11-heart-rate",
            ageGroup = "Age10To11",
            metricType = "HeartRate",
            normalMin = 60,
            normalMax = 80,
            attentionMin = 45,
            attentionMax = 60,
            doctorSoonMin = null,
            doctorSoonMax = 45,
        ),
        SingleMetricLimitEntity(
            id = "age-10-11-oxygen",
            ageGroup = "Age10To11",
            metricType = "OxygenSaturation",
            normalMin = 95,
            normalMax = 100,
            attentionMin = 90,
            attentionMax = 95,
            doctorSoonMin = null,
            doctorSoonMax = 89,
        ),
    )

    val bloodPressureLimits = listOf(
        BloodPressureLimitEntity(
            id = "age-10-11-blood-pressure",
            ageGroup = "Age10To11",
            normalSystolicMin = 110,
            normalSystolicMax = 125,
            normalDiastolicMin = 70,
            normalDiastolicMax = 85,
            doctorSoonSystolicLow = 110,
            doctorSoonSystolicHigh = 125,
            doctorSoonDiastolicLow = 70,
            doctorSoonDiastolicHigh = 85,
        ),
    )

    val vitamins = listOf(
        VitaminEntity("vitamin-1", PATIENT_ID, "Йод", "1 шт."),
        VitaminEntity("vitamin-2", PATIENT_ID, "Аспирин", "1 шт."),
    )

    val surveyLink = SurveyLinkEntity(
        id = "default-survey",
        title = "Ежемесячный опрос",
        url = "https://forms.yandex.ru/u/6a2abbf095add53c1f78ef01/",
        isActive = true,
        createdAt = 0L,
        updatedAt = 0L,
    )

    val helpContacts = listOf(
        HelpContactEntity(
            id = "contact-doctor",
            title = "Доктор",
            phone = "+644580",
            description = null,
            sortOrder = 0,
            isActive = true,
        ),
        HelpContactEntity(
            id = "contact-appointment",
            title = "Запись на приём",
            phone = "+780278",
            description = null,
            sortOrder = 1,
            isActive = true,
        ),
    )

    val faqs = listOf(
        FaqEntity(
            id = "faq-metrics",
            question = "Что делать, если показатель не в норме?",
            answer = "Не паникуйте. Проверьте значение ещё раз и свяжитесь с врачом по номеру из раздела помощи.",
            sortOrder = 0,
            isActive = true,
        ),
    )
}
