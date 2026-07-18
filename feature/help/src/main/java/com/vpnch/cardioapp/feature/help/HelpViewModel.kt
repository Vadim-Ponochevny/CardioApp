package com.vpnch.cardioapp.feature.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.analytics.Analytics
import com.vpnch.cardioapp.core.domain.analytics.AnalyticsEvent
import com.vpnch.cardioapp.core.domain.repository.HelpRepository
import com.vpnch.cardioapp.core.domain.repository.SurveyRepository
import com.vpnch.cardioapp.core.model.help.HelpContact
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HelpViewModel @Inject constructor(
    helpRepository: HelpRepository,
    private val surveyRepository: SurveyRepository,
    private val analytics: Analytics,
) : ViewModel() {

    val uiState = combine(
        helpRepository.observeHelpContacts(),
        surveyRepository.observeActiveSurveyLink(),
    ) { contacts, surveyLink ->
        HelpUiState(
            contacts = contacts,
            surveyUrl = surveyLink?.url,
            surveyId = surveyLink?.id,
            surveyIsNew = surveyLink?.isNew ?: false,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HelpUiState(),
    )

    fun onCallPressed() {
        analytics.report(AnalyticsEvent.CallButtonPressed)
    }

    fun onSurveyOpened(surveyId: String) {
        analytics.report(AnalyticsEvent.QuestionnaireOpened)
        viewModelScope.launch {
            surveyRepository.markSurveyOpened(surveyId)
        }
    }
}

data class HelpUiState(
    val contacts: List<HelpContact> = emptyList(),
    val surveyUrl: String? = null,
    val surveyId: String? = null,
    val surveyIsNew: Boolean = false,
    val isLoading: Boolean = true,
)
