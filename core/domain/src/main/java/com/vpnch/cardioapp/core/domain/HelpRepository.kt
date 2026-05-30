package com.vpnch.cardioapp.core.domain

import com.vpnch.cardioapp.core.model.Faq
import com.vpnch.cardioapp.core.model.HelpContact
import kotlinx.coroutines.flow.Flow

interface HelpRepository {
    fun observeHelpContacts(): Flow<List<HelpContact>>

    fun observeFaqs(): Flow<List<Faq>>
}
