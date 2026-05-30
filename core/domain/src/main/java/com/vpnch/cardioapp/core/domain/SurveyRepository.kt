package com.vpnch.cardioapp.core.domain

import com.vpnch.cardioapp.core.model.SurveyLink
import kotlinx.coroutines.flow.Flow

interface SurveyRepository {
    fun observeActiveSurveyLink(): Flow<SurveyLink?>
}
