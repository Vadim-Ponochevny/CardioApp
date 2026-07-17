package com.vpnch.cardioapp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vpnch.cardioapp.core.database.entity.help.FaqEntity
import com.vpnch.cardioapp.core.database.entity.help.HelpContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HelpDao {
    @Query("SELECT * FROM help_contacts WHERE isActive = 1 ORDER BY sortOrder")
    fun observeHelpContacts(): Flow<List<HelpContactEntity>>

    @Query("SELECT * FROM faqs WHERE isActive = 1 ORDER BY sortOrder")
    fun observeFaqs(): Flow<List<FaqEntity>>

    @Upsert
    suspend fun upsertHelpContacts(helpContacts: List<HelpContactEntity>)

    @Upsert
    suspend fun upsertFaqs(faqs: List<FaqEntity>)
}
