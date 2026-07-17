package com.vpnch.cardioapp.core.domain.repository

import com.vpnch.cardioapp.core.model.survey.SurveyLink
import kotlinx.coroutines.flow.Flow

interface SurveyRepository {
    fun observeActiveSurveyLink(): Flow<SurveyLink?>
}
