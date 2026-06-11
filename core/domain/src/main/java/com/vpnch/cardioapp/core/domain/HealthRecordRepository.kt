package com.vpnch.cardioapp.core.domain

import com.vpnch.cardioapp.core.model.AgeGroup
import com.vpnch.cardioapp.core.model.BloodPressureLimits
import com.vpnch.cardioapp.core.model.HealthRecord
import com.vpnch.cardioapp.core.model.SingleMetricLimits
import kotlinx.coroutines.flow.Flow

interface HealthRecordRepository {
    fun observeRecordsForDate(patientId: String, date: String): Flow<List<HealthRecord>>

    fun observeAllRecords(patientId: String): Flow<List<HealthRecord>>

    fun observeLatestRecord(patientId: String): Flow<HealthRecord?>

    suspend fun getRecord(recordId: String): HealthRecord?

    suspend fun addRecord(record: HealthRecord)

    suspend fun updateRecord(record: HealthRecord)

    suspend fun deleteRecord(recordId: String)

    fun observeRecord(recordId: String): Flow<HealthRecord?>

    suspend fun getSingleMetricLimits(ageGroup: AgeGroup): List<SingleMetricLimits>

    suspend fun getBloodPressureLimits(ageGroup: AgeGroup): BloodPressureLimits?
}
