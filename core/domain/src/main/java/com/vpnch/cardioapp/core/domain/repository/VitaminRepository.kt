package com.vpnch.cardioapp.core.domain.repository

import com.vpnch.cardioapp.core.model.vitamins.Vitamin
import com.vpnch.cardioapp.core.model.vitamins.VitaminIntakeSummary
import com.vpnch.cardioapp.core.model.vitamins.TakenVitaminOnDate
import kotlinx.coroutines.flow.Flow

interface VitaminRepository {
    fun observeVitamins(patientId: String): Flow<List<Vitamin>>

    fun observeVitaminIntakes(patientId: String, date: String): Flow<List<VitaminIntakeSummary>>

    fun observeTakenVitaminsHistory(patientId: String): Flow<List<TakenVitaminOnDate>>

    suspend fun getVitamin(vitaminId: String): Vitamin?

    suspend fun saveVitamin(vitamin: Vitamin)

    suspend fun deleteVitamin(vitaminId: String)

    suspend fun setVitaminTaken(patientId: String, date: String, vitaminId: String, isTaken: Boolean)
}
