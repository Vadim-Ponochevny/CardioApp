package com.vpnch.cardioapp.core.database

import com.vpnch.cardioapp.core.database.entity.FaqEntity
import com.vpnch.cardioapp.core.database.entity.BloodPressureLimitEntity
import com.vpnch.cardioapp.core.database.entity.HelpContactEntity
import com.vpnch.cardioapp.core.database.entity.SingleMetricLimitEntity
import com.vpnch.cardioapp.core.database.entity.SurveyLinkEntity

object SeedData {

    val singleMetricLimits = listOf(
        // Age3To5
        SingleMetricLimitEntity(
            id = "age-3-5-respiratory",
            ageGroup = "Age3To5",
            metricType = "RespiratoryRate",
            normalMin = 22, normalMax = 30,
            attentionMin = 18, attentionMax = 22,
            doctorSoonMin = null, doctorSoonMax = 17,
        ),
        SingleMetricLimitEntity(
            id = "age-3-5-heart-rate",
            ageGroup = "Age3To5",
            metricType = "HeartRate",
            normalMin = 85, normalMax = 110,
            attentionMin = 70, attentionMax = 85,
            doctorSoonMin = null, doctorSoonMax = 69,
        ),
        SingleMetricLimitEntity(
            id = "age-3-5-oxygen",
            ageGroup = "Age3To5",
            metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 89,
        ),
        // Age5To7
        SingleMetricLimitEntity(
            id = "age-5-7-respiratory",
            ageGroup = "Age5To7",
            metricType = "RespiratoryRate",
            normalMin = 20, normalMax = 26,
            attentionMin = 16, attentionMax = 20,
            doctorSoonMin = null, doctorSoonMax = 15,
        ),
        SingleMetricLimitEntity(
            id = "age-5-7-heart-rate",
            ageGroup = "Age5To7",
            metricType = "HeartRate",
            normalMin = 78, normalMax = 100,
            attentionMin = 65, attentionMax = 78,
            doctorSoonMin = null, doctorSoonMax = 64,
        ),
        SingleMetricLimitEntity(
            id = "age-5-7-oxygen",
            ageGroup = "Age5To7",
            metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 89,
        ),
        // Age7To9
        SingleMetricLimitEntity(
            id = "age-7-9-respiratory",
            ageGroup = "Age7To9",
            metricType = "RespiratoryRate",
            normalMin = 18, normalMax = 24,
            attentionMin = 14, attentionMax = 18,
            doctorSoonMin = null, doctorSoonMax = 13,
        ),
        SingleMetricLimitEntity(
            id = "age-7-9-heart-rate",
            ageGroup = "Age7To9",
            metricType = "HeartRate",
            normalMin = 70, normalMax = 95,
            attentionMin = 58, attentionMax = 70,
            doctorSoonMin = null, doctorSoonMax = 57,
        ),
        SingleMetricLimitEntity(
            id = "age-7-9-oxygen",
            ageGroup = "Age7To9",
            metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 89,
        ),
        // Age9To11
        SingleMetricLimitEntity(
            id = "age-9-11-respiratory",
            ageGroup = "Age9To11",
            metricType = "RespiratoryRate",
            normalMin = 18, normalMax = 24,
            attentionMin = 12, attentionMax = 18,
            doctorSoonMin = null, doctorSoonMax = 12,
        ),
        SingleMetricLimitEntity(
            id = "age-9-11-heart-rate",
            ageGroup = "Age9To11",
            metricType = "HeartRate",
            normalMin = 62, normalMax = 85,
            attentionMin = 50, attentionMax = 62,
            doctorSoonMin = null, doctorSoonMax = 49,
        ),
        SingleMetricLimitEntity(
            id = "age-9-11-oxygen",
            ageGroup = "Age9To11",
            metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 89,
        ),
        // Age10To11 (сохраняем для совместимости)
        SingleMetricLimitEntity(
            id = "age-10-11-respiratory",
            ageGroup = "Age10To11",
            metricType = "RespiratoryRate",
            normalMin = 18, normalMax = 24,
            attentionMin = 12, attentionMax = 18,
            doctorSoonMin = null, doctorSoonMax = 12,
        ),
        SingleMetricLimitEntity(
            id = "age-10-11-heart-rate",
            ageGroup = "Age10To11",
            metricType = "HeartRate",
            normalMin = 60, normalMax = 80,
            attentionMin = 45, attentionMax = 60,
            doctorSoonMin = null, doctorSoonMax = 45,
        ),
        SingleMetricLimitEntity(
            id = "age-10-11-oxygen",
            ageGroup = "Age10To11",
            metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 89,
        ),
        // Age11To14
        SingleMetricLimitEntity(
            id = "age-11-14-respiratory",
            ageGroup = "Age11To14",
            metricType = "RespiratoryRate",
            normalMin = 16, normalMax = 22,
            attentionMin = 12, attentionMax = 16,
            doctorSoonMin = null, doctorSoonMax = 11,
        ),
        SingleMetricLimitEntity(
            id = "age-11-14-heart-rate",
            ageGroup = "Age11To14",
            metricType = "HeartRate",
            normalMin = 58, normalMax = 82,
            attentionMin = 45, attentionMax = 58,
            doctorSoonMin = null, doctorSoonMax = 44,
        ),
        SingleMetricLimitEntity(
            id = "age-11-14-oxygen",
            ageGroup = "Age11To14",
            metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 89,
        ),
        // Age14To18
        SingleMetricLimitEntity(
            id = "age-14-18-respiratory",
            ageGroup = "Age14To18",
            metricType = "RespiratoryRate",
            normalMin = 14, normalMax = 20,
            attentionMin = 10, attentionMax = 14,
            doctorSoonMin = null, doctorSoonMax = 9,
        ),
        SingleMetricLimitEntity(
            id = "age-14-18-heart-rate",
            ageGroup = "Age14To18",
            metricType = "HeartRate",
            normalMin = 55, normalMax = 78,
            attentionMin = 45, attentionMax = 55,
            doctorSoonMin = null, doctorSoonMax = 44,
        ),
        SingleMetricLimitEntity(
            id = "age-14-18-oxygen",
            ageGroup = "Age14To18",
            metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 89,
        ),
    )

    val bloodPressureLimits = listOf(
        BloodPressureLimitEntity(
            id = "age-3-5-blood-pressure",
            ageGroup = "Age3To5",
            normalSystolicMin = 95, normalSystolicMax = 105,
            normalDiastolicMin = 55, normalDiastolicMax = 65,
            doctorSoonSystolicLow = 85, doctorSoonSystolicHigh = 115,
            doctorSoonDiastolicLow = 45, doctorSoonDiastolicHigh = 75,
        ),
        BloodPressureLimitEntity(
            id = "age-5-7-blood-pressure",
            ageGroup = "Age5To7",
            normalSystolicMin = 100, normalSystolicMax = 110,
            normalDiastolicMin = 60, normalDiastolicMax = 70,
            doctorSoonSystolicLow = 90, doctorSoonSystolicHigh = 120,
            doctorSoonDiastolicLow = 50, doctorSoonDiastolicHigh = 80,
        ),
        BloodPressureLimitEntity(
            id = "age-7-9-blood-pressure",
            ageGroup = "Age7To9",
            normalSystolicMin = 103, normalSystolicMax = 115,
            normalDiastolicMin = 63, normalDiastolicMax = 73,
            doctorSoonSystolicLow = 93, doctorSoonSystolicHigh = 125,
            doctorSoonDiastolicLow = 53, doctorSoonDiastolicHigh = 83,
        ),
        BloodPressureLimitEntity(
            id = "age-9-11-blood-pressure",
            ageGroup = "Age9To11",
            normalSystolicMin = 108, normalSystolicMax = 122,
            normalDiastolicMin = 68, normalDiastolicMax = 82,
            doctorSoonSystolicLow = 98, doctorSoonSystolicHigh = 132,
            doctorSoonDiastolicLow = 58, doctorSoonDiastolicHigh = 92,
        ),
        BloodPressureLimitEntity(
            id = "age-10-11-blood-pressure",
            ageGroup = "Age10To11",
            normalSystolicMin = 110, normalSystolicMax = 125,
            normalDiastolicMin = 70, normalDiastolicMax = 85,
            doctorSoonSystolicLow = 110, doctorSoonSystolicHigh = 125,
            doctorSoonDiastolicLow = 70, doctorSoonDiastolicHigh = 85,
        ),
        BloodPressureLimitEntity(
            id = "age-11-14-blood-pressure",
            ageGroup = "Age11To14",
            normalSystolicMin = 110, normalSystolicMax = 128,
            normalDiastolicMin = 68, normalDiastolicMax = 82,
            doctorSoonSystolicLow = 100, doctorSoonSystolicHigh = 138,
            doctorSoonDiastolicLow = 58, doctorSoonDiastolicHigh = 92,
        ),
        BloodPressureLimitEntity(
            id = "age-14-18-blood-pressure",
            ageGroup = "Age14To18",
            normalSystolicMin = 115, normalSystolicMax = 132,
            normalDiastolicMin = 70, normalDiastolicMax = 82,
            doctorSoonSystolicLow = 105, doctorSoonSystolicHigh = 142,
            doctorSoonDiastolicLow = 60, doctorSoonDiastolicHigh = 92,
        ),
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
