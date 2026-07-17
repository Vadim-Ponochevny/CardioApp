package com.vpnch.cardioapp.core.domain.repository

import com.vpnch.cardioapp.core.model.help.Faq
import com.vpnch.cardioapp.core.model.help.HelpContact
import kotlinx.coroutines.flow.Flow

interface HelpRepository {
    fun observeHelpContacts(): Flow<List<HelpContact>>

    fun observeFaqs(): Flow<List<Faq>>
}
