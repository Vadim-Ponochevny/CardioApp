package com.vpnch.cardioapp.core.data

import com.vpnch.cardioapp.core.database.CardioDatabase
import com.vpnch.cardioapp.core.database.SeedData
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val database: CardioDatabase,
) {
    suspend fun seedIfNeeded() {
        // Guard: check if new multi-age-group seed was already applied
        if (database.healthRecordDao().getSingleMetricLimitById("age-3-5-respiratory") != null) return

        database.healthRecordDao().upsertSingleMetricLimits(SeedData.singleMetricLimits)
        database.healthRecordDao().upsertBloodPressureLimits(SeedData.bloodPressureLimits)
        database.surveyDao().upsertSurveyLink(SeedData.surveyLink)
        database.helpDao().upsertHelpContacts(SeedData.helpContacts)
        database.helpDao().upsertFaqs(SeedData.faqs)
    }
}
