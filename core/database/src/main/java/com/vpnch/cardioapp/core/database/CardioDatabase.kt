package com.vpnch.cardioapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vpnch.cardioapp.core.database.dao.HealthRecordDao
import com.vpnch.cardioapp.core.database.dao.HelpDao
import com.vpnch.cardioapp.core.database.dao.SurveyDao
import com.vpnch.cardioapp.core.database.dao.VitaminDao
import com.vpnch.cardioapp.core.database.entity.health.limits.BloodPressureLimitEntity
import com.vpnch.cardioapp.core.database.entity.help.FaqEntity
import com.vpnch.cardioapp.core.database.entity.health.HealthRecordEntity
import com.vpnch.cardioapp.core.database.entity.help.HelpContactEntity
import com.vpnch.cardioapp.core.database.entity.health.limits.SingleMetricLimitEntity
import com.vpnch.cardioapp.core.database.entity.survey.SurveyLinkEntity
import com.vpnch.cardioapp.core.database.entity.vitamins.VitaminEntity
import com.vpnch.cardioapp.core.database.entity.vitamins.VitaminIntakeDayEntity
import com.vpnch.cardioapp.core.database.entity.vitamins.VitaminIntakeEntity

@Database(
    entities = [
        HealthRecordEntity::class,
        SingleMetricLimitEntity::class,
        BloodPressureLimitEntity::class,
        VitaminEntity::class,
        VitaminIntakeDayEntity::class,
        VitaminIntakeEntity::class,
        SurveyLinkEntity::class,
        HelpContactEntity::class,
        FaqEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class CardioDatabase : RoomDatabase() {
    abstract fun healthRecordDao(): HealthRecordDao

    abstract fun vitaminDao(): VitaminDao

    abstract fun surveyDao(): SurveyDao

    abstract fun helpDao(): HelpDao
}
