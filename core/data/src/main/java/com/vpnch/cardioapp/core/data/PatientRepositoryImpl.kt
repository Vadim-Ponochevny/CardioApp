package com.vpnch.cardioapp.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vpnch.cardioapp.core.database.SeedData
import com.vpnch.cardioapp.core.domain.PatientRepository
import com.vpnch.cardioapp.core.model.AgeGroup
import com.vpnch.cardioapp.core.model.Patient
import com.vpnch.cardioapp.core.model.UserType
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PatientRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PatientRepository {
    private val defaultPatient = Patient(
        id = SeedData.PATIENT_ID,
        birthDate = "2014-01-01",
        ageGroup = AgeGroup.Age10To11,
        userType = UserType.Parent,
    )

    override fun observeCurrentPatient(): Flow<Patient?> {
        return dataStore.data.map { preferences -> preferences.asPatient() ?: defaultPatient }
    }

    override suspend fun getCurrentPatient(): Patient? {
        return dataStore.data.first().asPatient() ?: defaultPatient
    }

    override suspend fun savePatient(patient: Patient) {
        dataStore.edit { preferences ->
            preferences[PATIENT_ID] = patient.id
            preferences[BIRTH_DATE] = patient.birthDate
            preferences[AGE_GROUP] = patient.ageGroup.name
            preferences[USER_TYPE] = patient.userType.name
        }
    }

    private fun Preferences.asPatient(): Patient? {
        val id = this[PATIENT_ID] ?: return null
        val birthDate = this[BIRTH_DATE] ?: return null
        val ageGroup = this[AGE_GROUP]?.let(AgeGroup::valueOf) ?: return null
        val userType = this[USER_TYPE]?.let(UserType::valueOf) ?: return null

        return Patient(
            id = id,
            birthDate = birthDate,
            ageGroup = ageGroup,
            userType = userType,
        )
    }

    private companion object {
        val PATIENT_ID = stringPreferencesKey("patient_id")
        val BIRTH_DATE = stringPreferencesKey("birth_date")
        val AGE_GROUP = stringPreferencesKey("age_group")
        val USER_TYPE = stringPreferencesKey("user_type")
    }
}
