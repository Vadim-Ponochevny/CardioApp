package com.vpnch.cardioapp.core.data

import com.vpnch.cardioapp.core.database.dao.SurveyDao
import com.vpnch.cardioapp.core.domain.SurveyRepository
import com.vpnch.cardioapp.core.model.SurveyLink
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
