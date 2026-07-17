package com.vpnch.cardioapp.core.data.repository

import com.vpnch.cardioapp.core.data.database.asExternalModel
import com.vpnch.cardioapp.core.database.dao.SurveyDao
import com.vpnch.cardioapp.core.domain.repository.SurveyRepository
import com.vpnch.cardioapp.core.model.survey.SurveyLink
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SurveyRepositoryImpl @Inject constructor(
    private val surveyDao: SurveyDao,
) : SurveyRepository {

    override fun observeActiveSurveyLink(): Flow<SurveyLink?> {
        return surveyDao.observeActiveSurveyLink().map { it?.asExternalModel() }
    }
}
