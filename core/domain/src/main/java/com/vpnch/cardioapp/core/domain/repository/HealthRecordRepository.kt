package com.vpnch.cardioapp.core.domain.repository

import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.core.model.health.limits.BloodPressureLimits
import com.vpnch.cardioapp.core.model.health.HealthRecord
import com.vpnch.cardioapp.core.model.health.limits.SingleMetricLimits
import kotlinx.coroutines.flow.Flow

interface HealthRecordRepository {
    fun observeRecordsForDate(patientId: String, date: String): Flow<List<HealthRecord>>

    fun observeAllRecords(patientId: String): Flow<List<HealthRecord>>

    fun observeLatestRecord(patientId: String): Flow<HealthRecord?>

    suspend fun getRecord(recordId: String): HealthRecord?

    suspend fun saveRecord(record: HealthRecord)

    suspend fun deleteRecord(recordId: String)

    fun observeRecord(recordId: String): Flow<HealthRecord?>

    suspend fun getSingleMetricLimits(ageGroup: AgeGroup): List<SingleMetricLimits>

    suspend fun getBloodPressureLimits(ageGroup: AgeGroup): BloodPressureLimits?

    suspend fun upsertSingleMetricLimits(limits: List<SingleMetricLimits>)

    suspend fun upsertBloodPressureLimits(limits: List<BloodPressureLimits>)
}
