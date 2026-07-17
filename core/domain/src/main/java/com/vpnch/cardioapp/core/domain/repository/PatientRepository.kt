package com.vpnch.cardioapp.core.domain.repository

import com.vpnch.cardioapp.core.model.patient.Patient
import kotlinx.coroutines.flow.StateFlow

interface PatientRepository {

    val currentPatient: StateFlow<Patient?>

    suspend fun getCurrentPatient(): Patient?

    suspend fun savePatient(patient: Patient)

    fun isProfileComplete(): Boolean

    suspend fun getNotificationsEnabled(): Boolean

    suspend fun setNotificationsEnabled(enabled: Boolean)
}
