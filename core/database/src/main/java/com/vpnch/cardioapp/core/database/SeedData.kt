package com.vpnch.cardioapp.core.database

import com.vpnch.cardioapp.core.database.entity.help.FaqEntity
import com.vpnch.cardioapp.core.database.entity.health.limits.BloodPressureLimitEntity
import com.vpnch.cardioapp.core.database.entity.help.HelpContactEntity
import com.vpnch.cardioapp.core.database.entity.health.limits.SingleMetricLimitEntity
import com.vpnch.cardioapp.core.database.entity.survey.SurveyLinkEntity

/**
 * Seed data based on "Критические значения КХО2" document.
 * ЧД (respiratory rate) values are placeholders — not present in the source document.
 * BP has no separate "attention" zone — goes directly from normal to doctor-soon.
 */
object SeedData {

    val singleMetricLimits = listOf(
        // ── Age3To5 → doc "1-6 лет" ──────────────────────────────────────────────
        SingleMetricLimitEntity(
            id = "age-3-5-heart-rate",
            ageGroup = "Age3To5", metricType = "HeartRate",
            normalMin = 105, normalMax = 110,
            attentionMin = 60, attentionMax = 105,
            doctorSoonMin = null, doctorSoonMax = 60,
        ),
        SingleMetricLimitEntity(
            id = "age-3-5-oxygen",
            ageGroup = "Age3To5", metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 90,
        ),
        SingleMetricLimitEntity( // placeholder — not in source document
            id = "age-3-5-respiratory",
            ageGroup = "Age3To5", metricType = "RespiratoryRate",
            normalMin = 22, normalMax = 30,
            attentionMin = 18, attentionMax = 22,
            doctorSoonMin = null, doctorSoonMax = 17,
        ),

        // ── Age5To7 → doc "1-6 лет" ──────────────────────────────────────────────
        SingleMetricLimitEntity(
            id = "age-5-7-heart-rate",
            ageGroup = "Age5To7", metricType = "HeartRate",
            normalMin = 105, normalMax = 110,
            attentionMin = 60, attentionMax = 105,
            doctorSoonMin = null, doctorSoonMax = 60,
        ),
        SingleMetricLimitEntity(
            id = "age-5-7-oxygen",
            ageGroup = "Age5To7", metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 90,
        ),
        SingleMetricLimitEntity(
            id = "age-5-7-respiratory",
            ageGroup = "Age5To7", metricType = "RespiratoryRate",
            normalMin = 20, normalMax = 26,
            attentionMin = 16, attentionMax = 20,
            doctorSoonMin = null, doctorSoonMax = 15,
        ),

        // ── Age7To9 → doc "7-10 лет" ─────────────────────────────────────────────
        SingleMetricLimitEntity(
            id = "age-7-9-heart-rate",
            ageGroup = "Age7To9", metricType = "HeartRate",
            normalMin = 70, normalMax = 90,
            attentionMin = 45, attentionMax = 70,
            doctorSoonMin = null, doctorSoonMax = 45,
        ),
        SingleMetricLimitEntity(
            id = "age-7-9-oxygen",
            ageGroup = "Age7To9", metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 90,
        ),
        SingleMetricLimitEntity(
            id = "age-7-9-respiratory",
            ageGroup = "Age7To9", metricType = "RespiratoryRate",
            normalMin = 18, normalMax = 24,
            attentionMin = 14, attentionMax = 18,
            doctorSoonMin = null, doctorSoonMax = 13,
        ),

        // ── Age9To11 → doc "7-10 лет" ────────────────────────────────────────────
        SingleMetricLimitEntity(
            id = "age-9-11-heart-rate",
            ageGroup = "Age9To11", metricType = "HeartRate",
            normalMin = 70, normalMax = 90,
            attentionMin = 45, attentionMax = 70,
            doctorSoonMin = null, doctorSoonMax = 45,
        ),
        SingleMetricLimitEntity(
            id = "age-9-11-oxygen",
            ageGroup = "Age9To11", metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 90,
        ),
        SingleMetricLimitEntity(
            id = "age-9-11-respiratory",
            ageGroup = "Age9To11", metricType = "RespiratoryRate",
            normalMin = 18, normalMax = 24,
            attentionMin = 12, attentionMax = 18,
            doctorSoonMin = null, doctorSoonMax = 12,
        ),

        // ── Age10To11 → doc "10-11 лет" (backward compat) ───────────────────────
        SingleMetricLimitEntity(
            id = "age-10-11-heart-rate",
            ageGroup = "Age10To11", metricType = "HeartRate",
            normalMin = 60, normalMax = 80,
            attentionMin = 45, attentionMax = 60,
            doctorSoonMin = null, doctorSoonMax = 45,
        ),
        SingleMetricLimitEntity(
            id = "age-10-11-oxygen",
            ageGroup = "Age10To11", metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 90,
        ),
        SingleMetricLimitEntity(
            id = "age-10-11-respiratory",
            ageGroup = "Age10To11", metricType = "RespiratoryRate",
            normalMin = 18, normalMax = 24,
            attentionMin = 12, attentionMax = 18,
            doctorSoonMin = null, doctorSoonMax = 12,
        ),

        // ── Age11To14 → doc "12-18 лет" ──────────────────────────────────────────
        SingleMetricLimitEntity(
            id = "age-11-14-heart-rate",
            ageGroup = "Age11To14", metricType = "HeartRate",
            normalMin = 60, normalMax = 80,
            attentionMin = 40, attentionMax = 60,
            doctorSoonMin = null, doctorSoonMax = 40,
        ),
        SingleMetricLimitEntity(
            id = "age-11-14-oxygen",
            ageGroup = "Age11To14", metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 90,
        ),
        SingleMetricLimitEntity(
            id = "age-11-14-respiratory",
            ageGroup = "Age11To14", metricType = "RespiratoryRate",
            normalMin = 16, normalMax = 22,
            attentionMin = 12, attentionMax = 16,
            doctorSoonMin = null, doctorSoonMax = 11,
        ),

        // ── Age14To18 → doc "12-18 лет" ──────────────────────────────────────────
        SingleMetricLimitEntity(
            id = "age-14-18-heart-rate",
            ageGroup = "Age14To18", metricType = "HeartRate",
            normalMin = 60, normalMax = 80,
            attentionMin = 40, attentionMax = 60,
            doctorSoonMin = null, doctorSoonMax = 40,
        ),
        SingleMetricLimitEntity(
            id = "age-14-18-oxygen",
            ageGroup = "Age14To18", metricType = "OxygenSaturation",
            normalMin = 95, normalMax = 100,
            attentionMin = 90, attentionMax = 95,
            doctorSoonMin = null, doctorSoonMax = 90,
        ),
        SingleMetricLimitEntity(
            id = "age-14-18-respiratory",
            ageGroup = "Age14To18", metricType = "RespiratoryRate",
            normalMin = 14, normalMax = 20,
            attentionMin = 10, attentionMax = 14,
            doctorSoonMin = null, doctorSoonMax = 9,
        ),
    )

    val bloodPressureLimits = listOf(
        // ── Age3To5 / Age5To7 → doc "1-6 лет" ───────────────────────────────────
        BloodPressureLimitEntity(
            id = "age-3-5-bp", ageGroup = "Age3To5",
            normalSystolicMin = 100, normalSystolicMax = 115,
            normalDiastolicMin = 60, normalDiastolicMax = 75,
            doctorSoonSystolicLow = 100, doctorSoonSystolicHigh = 115,
            doctorSoonDiastolicLow = 60, doctorSoonDiastolicHigh = 75,
        ),
        BloodPressureLimitEntity(
            id = "age-5-7-bp", ageGroup = "Age5To7",
            normalSystolicMin = 100, normalSystolicMax = 115,
            normalDiastolicMin = 60, normalDiastolicMax = 75,
            doctorSoonSystolicLow = 100, doctorSoonSystolicHigh = 115,
            doctorSoonDiastolicLow = 60, doctorSoonDiastolicHigh = 75,
        ),
        // ── Age7To9 / Age9To11 → doc "7-10 лет" ──────────────────────────────────
        BloodPressureLimitEntity(
            id = "age-7-9-bp", ageGroup = "Age7To9",
            normalSystolicMin = 100, normalSystolicMax = 125,
            normalDiastolicMin = 60, normalDiastolicMax = 80,
            doctorSoonSystolicLow = 100, doctorSoonSystolicHigh = 125,
            doctorSoonDiastolicLow = 60, doctorSoonDiastolicHigh = 80,
        ),
        BloodPressureLimitEntity(
            id = "age-9-11-bp", ageGroup = "Age9To11",
            normalSystolicMin = 100, normalSystolicMax = 125,
            normalDiastolicMin = 60, normalDiastolicMax = 80,
            doctorSoonSystolicLow = 100, doctorSoonSystolicHigh = 125,
            doctorSoonDiastolicLow = 60, doctorSoonDiastolicHigh = 80,
        ),
        // ── Age10To11 → doc "10-11 лет" ───────────────────────────────────────────
        BloodPressureLimitEntity(
            id = "age-10-11-bp", ageGroup = "Age10To11",
            normalSystolicMin = 110, normalSystolicMax = 125,
            normalDiastolicMin = 70, normalDiastolicMax = 85,
            doctorSoonSystolicLow = 110, doctorSoonSystolicHigh = 125,
            doctorSoonDiastolicLow = 70, doctorSoonDiastolicHigh = 85,
        ),
        // ── Age11To14 / Age14To18 → doc "12-18 лет" ──────────────────────────────
        BloodPressureLimitEntity(
            id = "age-11-14-bp", ageGroup = "Age11To14",
            normalSystolicMin = 110, normalSystolicMax = 135,
            normalDiastolicMin = 70, normalDiastolicMax = 85,
            doctorSoonSystolicLow = 110, doctorSoonSystolicHigh = 135,
            doctorSoonDiastolicLow = 70, doctorSoonDiastolicHigh = 85,
        ),
        BloodPressureLimitEntity(
            id = "age-14-18-bp", ageGroup = "Age14To18",
            normalSystolicMin = 110, normalSystolicMax = 135,
            normalDiastolicMin = 70, normalDiastolicMax = 85,
            doctorSoonSystolicLow = 110, doctorSoonSystolicHigh = 135,
            doctorSoonDiastolicLow = 70, doctorSoonDiastolicHigh = 85,
        ),
    )

    val surveyLink = SurveyLinkEntity(
        id = "default-survey",
        title = "Ежемесячный опрос",
        url = "https://forms.yandex.ru/u/6a2abbf095add53c1f78ef01/",
        isActive = true, createdAt = 0L, updatedAt = 0L,
    )

    val helpContacts = listOf(
        HelpContactEntity(id = "contact-doctor", title = "Доктор", phone = "+644580",
            description = null, sortOrder = 0, isActive = true),
        HelpContactEntity(id = "contact-appointment", title = "Запись на приём", phone = "+780278",
            description = null, sortOrder = 1, isActive = true),
    )

    val faqs = listOf(
        FaqEntity(id = "faq-metrics",
            question = "Что делать, если показатель не в норме?",
            answer = "Не паникуйте. Проверьте значение ещё раз и свяжитесь с врачом по номеру из раздела помощи.",
            sortOrder = 0, isActive = true),
    )
}
