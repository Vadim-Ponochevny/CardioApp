package com.vpnch.cardioapp.core.domain

import com.vpnch.cardioapp.core.model.Patient
import kotlinx.coroutines.flow.Flow

interface PatientRepository {
    fun observeCurrentPatient(): Flow<Patient?>

    suspend fun getCurrentPatient(): Patient?

    suspend fun savePatient(patient: Patient)
}
