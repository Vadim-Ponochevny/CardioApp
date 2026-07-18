package com.vpnch.cardioapp.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.vpnch.cardioapp.core.data.database.asExternalModel
import com.vpnch.cardioapp.core.data.datastore.SURVEY_OPENED_ID
import com.vpnch.cardioapp.core.database.dao.SurveyDao
import com.vpnch.cardioapp.core.domain.repository.SurveyRepository
import com.vpnch.cardioapp.core.model.survey.SurveyLink
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class SurveyRepositoryImpl @Inject constructor(
    private val surveyDao: SurveyDao,
    private val dataStore: DataStore<Preferences>,
) : SurveyRepository {

    override fun observeActiveSurveyLink(): Flow<SurveyLink?> = combine(
        surveyDao.observeActiveSurveyLink(),
        dataStore.data.map { it[SURVEY_OPENED_ID] },
    ) { entity, openedId ->
        entity?.asExternalModel()?.copy(isNew = entity.id != openedId)
    }

    override suspend fun getCurrentSurveyId(): String? = surveyDao.getActiveSurveyId()

    override suspend fun markSurveyOpened(surveyId: String) {
        dataStore.edit { it[SURVEY_OPENED_ID] = surveyId }
    }
}
