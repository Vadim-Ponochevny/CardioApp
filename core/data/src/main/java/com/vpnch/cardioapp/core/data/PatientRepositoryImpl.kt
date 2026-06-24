package com.vpnch.cardioapp.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vpnch.cardioapp.core.domain.PatientRepository
import com.vpnch.cardioapp.core.model.AgeGroup
import com.vpnch.cardioapp.core.model.Patient
import com.vpnch.cardioapp.core.model.UserType
import com.vpnch.cardioapp.core.model.ValveType
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PatientRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PatientRepository {

    override fun observeCurrentPatient(): Flow<Patient?> {
        return dataStore.data.map { it.asPatient() }
    }

    override suspend fun getCurrentPatient(): Patient? {
        return dataStore.data.first().asPatient()
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
    }

    override fun isProfileComplete(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            val id = preferences[PATIENT_ID]
            val birthDate = preferences[BIRTH_DATE]
            !id.isNullOrBlank() && !birthDate.isNullOrBlank()
        }
    }

    private fun Preferences.asPatient(): Patient? {
        val id = this[PATIENT_ID]?.takeIf { it.isNotBlank() } ?: return null
        val birthDate = this[BIRTH_DATE]?.takeIf { it.isNotBlank() } ?: return null
        val ageGroup = this[AGE_GROUP]?.let { runCatching { AgeGroup.valueOf(it) }.getOrNull() }
            ?: AgeGroup.Age9To11
        val userType = this[USER_TYPE]?.let { runCatching { UserType.valueOf(it) }.getOrNull() }
            ?: UserType.Parent
        val takesWarfarin = this[TAKES_WARFARIN] ?: false
        val valveType = this[VALVE_TYPE]?.takeIf { it.isNotBlank() }
            ?.let { runCatching { ValveType.valueOf(it) }.getOrNull() }
        val useCustomLimits = this[USE_CUSTOM_LIMITS] ?: false

        return Patient(
            id = id,
            birthDate = birthDate,
            ageGroup = ageGroup,
            userType = userType,
            takesWarfarin = takesWarfarin,
            valveType = valveType,
            useCustomLimits = useCustomLimits,
        )
    }

    private companion object {
        val PATIENT_ID = stringPreferencesKey("patient_id")
        val BIRTH_DATE = stringPreferencesKey("birth_date")
        val AGE_GROUP = stringPreferencesKey("age_group")
        val USER_TYPE = stringPreferencesKey("user_type")
        val TAKES_WARFARIN = booleanPreferencesKey("takes_warfarin")
        val VALVE_TYPE = stringPreferencesKey("valve_type")
        val USE_CUSTOM_LIMITS = booleanPreferencesKey("use_custom_limits")
    }
}
