package com.vpnch.cardioapp.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.vpnch.cardioapp.core.data.datastore.AGE_GROUP
import com.vpnch.cardioapp.core.data.datastore.BIRTH_DATE
import com.vpnch.cardioapp.core.data.datastore.PATIENT_ID
import com.vpnch.cardioapp.core.data.datastore.TAKES_WARFARIN
import com.vpnch.cardioapp.core.data.datastore.USER_TYPE
import com.vpnch.cardioapp.core.data.datastore.USE_CUSTOM_LIMITS
import com.vpnch.cardioapp.core.data.datastore.VALVE_TYPE
import com.vpnch.cardioapp.core.data.datastore.NOTIFICATIONS_ENABLED
import com.vpnch.cardioapp.core.data.datastore.asPatient
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.core.model.patient.Patient
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
class PatientRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val externalScope: CoroutineScope,
) : PatientRepository {

    private val _currentPatient = MutableStateFlow<Patient?>(null)
    override val currentPatient: StateFlow<Patient?> = _currentPatient.asStateFlow()

    init {
        externalScope.launch {
            _currentPatient.value = dataStore.data.first().asPatient()
        }
    }

    override suspend fun getCurrentPatient(): Patient? {
        return _currentPatient.value ?: dataStore.data.first().asPatient().also {
            _currentPatient.value = it
        }
    }

    override suspend fun savePatient(patient: Patient) {
        dataStore.edit { preferences ->
            preferences[PATIENT_ID] = patient.id
            preferences[BIRTH_DATE] = patient.birthDate
            preferences[AGE_GROUP] = patient.ageGroup.name
            preferences[USER_TYPE] = patient.userType.name
            preferences[TAKES_WARFARIN] = patient.takesWarfarin
            preferences[VALVE_TYPE] = patient.valveType?.name ?: ""
            preferences[USE_CUSTOM_LIMITS] = patient.useCustomLimits
        }
        _currentPatient.value = patient
    }

    override suspend fun getNotificationsEnabled(): Boolean {
        return dataStore.data.first()[NOTIFICATIONS_ENABLED] ?: false
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled }
    }

    override fun isProfileComplete(): Boolean {
        val patient = _currentPatient.value
        return patient != null && patient.id.isNotBlank() && patient.birthDate.isNotBlank()
    }
}
