package com.vpnch.cardioapp.core.data.repository

import com.vpnch.cardioapp.core.data.database.asExternalModel
import com.vpnch.cardioapp.core.database.dao.HelpDao
import com.vpnch.cardioapp.core.domain.repository.HelpRepository
import com.vpnch.cardioapp.core.model.help.Faq
import com.vpnch.cardioapp.core.model.help.HelpContact
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HelpRepositoryImpl @Inject constructor(
    private val helpDao: HelpDao,
) : HelpRepository {

    override fun observeHelpContacts(): Flow<List<HelpContact>> {
        return helpDao.observeHelpContacts().map { contacts ->
            contacts.map { it.asExternalModel() }
        }
    }

    override fun observeFaqs(): Flow<List<Faq>> {
        return helpDao.observeFaqs().map { faqs ->
            faqs.map { it.asExternalModel() }
        }
    }
}
