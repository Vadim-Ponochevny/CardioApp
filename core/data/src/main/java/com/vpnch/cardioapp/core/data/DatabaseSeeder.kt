package com.vpnch.cardioapp.core.data

import com.vpnch.cardioapp.core.database.CardioDatabase
import com.vpnch.cardioapp.core.database.SeedData
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val database: CardioDatabase,
) {
    suspend fun seedIfNeeded() {
        if (database.vitaminDao().getVitamin(SeedData.vitamins.first().id) != null) return

        database.healthRecordDao().upsertSingleMetricLimits(SeedData.singleMetricLimits)
        database.healthRecordDao().upsertBloodPressureLimits(SeedData.bloodPressureLimits)
        SeedData.vitamins.forEach { database.vitaminDao().upsertVitamin(it) }
        database.surveyDao().upsertSurveyLink(SeedData.surveyLink)
        database.helpDao().upsertHelpContacts(SeedData.helpContacts)
        database.helpDao().upsertFaqs(SeedData.faqs)
    }
}
