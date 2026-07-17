package com.vpnch.cardioapp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vpnch.cardioapp.core.database.entity.survey.SurveyLinkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SurveyDao {
    @Query("SELECT * FROM survey_links WHERE isActive = 1 ORDER BY updatedAt DESC LIMIT 1")
    fun observeActiveSurveyLink(): Flow<SurveyLinkEntity?>

    @Upsert
    suspend fun upsertSurveyLink(surveyLink: SurveyLinkEntity)
}
