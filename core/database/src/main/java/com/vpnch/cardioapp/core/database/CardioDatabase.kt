package com.vpnch.cardioapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vpnch.cardioapp.core.database.dao.HealthRecordDao
import com.vpnch.cardioapp.core.database.dao.HelpDao
import com.vpnch.cardioapp.core.database.dao.SurveyDao
import com.vpnch.cardioapp.core.database.dao.VitaminDao
import com.vpnch.cardioapp.core.database.entity.BloodPressureLimitEntity
import com.vpnch.cardioapp.core.database.entity.FaqEntity
import com.vpnch.cardioapp.core.database.entity.HealthRecordEntity
import com.vpnch.cardioapp.core.database.entity.HelpContactEntity
import com.vpnch.cardioapp.core.database.entity.SingleMetricLimitEntity
import com.vpnch.cardioapp.core.database.entity.SurveyLinkEntity
import com.vpnch.cardioapp.core.database.entity.VitaminEntity
import com.vpnch.cardioapp.core.database.entity.VitaminIntakeDayEntity
import com.vpnch.cardioapp.core.database.entity.VitaminIntakeEntity

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
    version = 2,
    exportSchema = false,
)
abstract class CardioDatabase : RoomDatabase() {
    abstract fun healthRecordDao(): HealthRecordDao

    abstract fun vitaminDao(): VitaminDao

    abstract fun surveyDao(): SurveyDao

    abstract fun helpDao(): HelpDao
}
