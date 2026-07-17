package com.vpnch.cardioapp.core.data.database

import android.content.Context
import androidx.core.content.edit
import com.vpnch.cardioapp.core.database.CardioDatabase
import com.vpnch.cardioapp.core.database.SeedData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// Increment this number every time you change SeedData
private const val SEED_VERSION = 1
private const val PREFS_NAME = "cardio_seed"
private const val KEY_SEED_VERSION = "seed_version"

class DatabaseSeeder @Inject constructor(
    private val database: CardioDatabase,
    @ApplicationContext private val context: Context,
) {
    suspend fun seedIfNeeded() {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val appliedVersion = prefs.getInt(KEY_SEED_VERSION, 0)
        if (appliedVersion >= SEED_VERSION) return

        database.healthRecordDao().upsertSingleMetricLimits(SeedData.singleMetricLimits)
        database.healthRecordDao().upsertBloodPressureLimits(SeedData.bloodPressureLimits)
        database.surveyDao().upsertSurveyLink(SeedData.surveyLink)
        database.helpDao().upsertHelpContacts(SeedData.helpContacts)
        database.helpDao().upsertFaqs(SeedData.faqs)

        prefs.edit { putInt(KEY_SEED_VERSION, SEED_VERSION) }
    }
}