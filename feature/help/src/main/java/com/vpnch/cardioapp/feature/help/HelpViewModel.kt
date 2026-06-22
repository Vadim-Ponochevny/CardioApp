package com.vpnch.cardioapp.feature.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpnch.cardioapp.core.domain.HelpRepository
import com.vpnch.cardioapp.core.domain.SurveyRepository
import com.vpnch.cardioapp.core.model.HelpContact
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HelpViewModel @Inject constructor(
    helpRepository: HelpRepository,
    surveyRepository: SurveyRepository,
) : ViewModel() {

    val uiState = combine(
        helpRepository.observeHelpContacts(),
        surveyRepository.observeActiveSurveyLink(),
    ) { contacts, surveyLink ->
        HelpUiState(
            contacts = contacts,
            surveyUrl = surveyLink?.url,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HelpUiState(),
    )
}

data class HelpUiState(
    val contacts: List<HelpContact> = emptyList(),
    val surveyUrl: String? = null,
    val isLoading: Boolean = true,
)
