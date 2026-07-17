package com.vpnch.cardioapp.core.data.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.core.model.patient.Patient
import com.vpnch.cardioapp.core.model.patient.UserType
import com.vpnch.cardioapp.core.model.patient.ValveType

/**
 * Extension function for [Preferences] (DataStore) that converts stored patient data
 * into a domain model [Patient].
 *
 * The keys defined below correspond to the fields saved in DataStore via [PatientRepositoryImpl].
 * If required fields (`PATIENT_ID` or `BIRTH_DATE`) are missing or blank, [asPatient] returns `null`,
 * indicating that the patient profile is not complete.
 *
 * For fields that may be absent, default values are used:
 * - `AGE_GROUP` → [AgeGroup.Age9To11]
 * - `USER_TYPE` → [UserType.Parent]
 * - `TAKES_WARFARIN` → `false`
 * - `VALVE_TYPE` → `null` (if not set or cannot be parsed)
 * - `USE_CUSTOM_LIMITS` → `false`
 *
 * @see PATIENT_ID
 * @see BIRTH_DATE
 * @see AGE_GROUP
 * @see USER_TYPE
 * @see TAKES_WARFARIN
 * @see VALVE_TYPE
 * @see USE_CUSTOM_LIMITS
 */

internal fun Preferences.asPatient(): Patient? {
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

internal val PATIENT_ID = stringPreferencesKey("patient_id")
internal val BIRTH_DATE = stringPreferencesKey("birth_date")
internal val AGE_GROUP = stringPreferencesKey("age_group")
internal val USER_TYPE = stringPreferencesKey("user_type")
internal val TAKES_WARFARIN = booleanPreferencesKey("takes_warfarin")
internal val VALVE_TYPE = stringPreferencesKey("valve_type")
internal val USE_CUSTOM_LIMITS = booleanPreferencesKey("use_custom_limits")
internal val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")