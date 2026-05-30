package com.vpnch.cardioapp.core.data

import com.vpnch.cardioapp.core.database.dao.HealthRecordDao
import com.vpnch.cardioapp.core.domain.HealthRecordRepository
import com.vpnch.cardioapp.core.model.AgeGroup
import com.vpnch.cardioapp.core.model.BloodPressureLimits
import com.vpnch.cardioapp.core.model.HealthRecord
import com.vpnch.cardioapp.core.model.SingleMetricLimits
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HealthRecordRepositoryImpl @Inject constructor(
    private val healthRecordDao: HealthRecordDao,
) : HealthRecordRepository {

    override fun observeRecordsForDate(
        patientId: String,
        date: String,
    ): Flow<List<HealthRecord>> {
        val range = dateRangeOf(date)
        return healthRecordDao
            .observeRecordsForDate(patientId, range.startOfDay, range.startOfNextDay)
            .map { records -> records.map { it.asExternalModel() } }
    }

    override fun observeLatestRecord(patientId: String): Flow<HealthRecord?> {
        return healthRecordDao.observeLatestRecord(patientId).map { it?.asExternalModel() }
    }

    override suspend fun getRecord(recordId: String): HealthRecord? {
        return healthRecordDao.getRecord(recordId)?.asExternalModel()
    }

    override suspend fun addRecord(record: HealthRecord) {
        healthRecordDao.upsertRecord(record.asEntity())
    }

    override suspend fun updateRecord(record: HealthRecord) {
        healthRecordDao.upsertRecord(record.asEntity())
    }

    override suspend fun getSingleMetricLimits(ageGroup: AgeGroup): List<SingleMetricLimits> {
        return healthRecordDao.getSingleMetricLimits(ageGroup.name).map { it.asExternalModel() }
    }

    override suspend fun getBloodPressureLimits(ageGroup: AgeGroup): BloodPressureLimits? {
        return healthRecordDao.getBloodPressureLimits(ageGroup.name)?.asExternalModel()
    }
}
