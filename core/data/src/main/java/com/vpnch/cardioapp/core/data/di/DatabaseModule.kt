package com.vpnch.cardioapp.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.vpnch.cardioapp.core.database.CardioDatabase
import com.vpnch.cardioapp.core.database.migration.MIGRATION_1_2
import com.vpnch.cardioapp.core.database.migration.MIGRATION_2_3
import com.vpnch.cardioapp.core.database.dao.HealthRecordDao
import com.vpnch.cardioapp.core.database.dao.HelpDao
import com.vpnch.cardioapp.core.database.dao.SurveyDao
import com.vpnch.cardioapp.core.database.dao.VitaminDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideCardioDatabase(
        @ApplicationContext context: Context,
    ): CardioDatabase {
        return Room.databaseBuilder(
            context,
            CardioDatabase::class.java,
            "cardio.db",
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserProfileDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { File(context.filesDir, "user_profile.preferences_pb") },
        )
    }

    @Provides
    fun provideHealthRecordDao(database: CardioDatabase): HealthRecordDao = database.healthRecordDao()

    @Provides
    fun provideVitaminDao(database: CardioDatabase): VitaminDao = database.vitaminDao()

    @Provides
    fun provideSurveyDao(database: CardioDatabase): SurveyDao = database.surveyDao()

    @Provides
    fun provideHelpDao(database: CardioDatabase): HelpDao = database.helpDao()
}
