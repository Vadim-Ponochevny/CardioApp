package com.vpnch.cardioapp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vpnch.cardioapp.core.database.entity.BloodPressureLimitEntity
import com.vpnch.cardioapp.core.database.entity.HealthRecordEntity
import com.vpnch.cardioapp.core.database.entity.SingleMetricLimitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {
    @Query(
        """
        SELECT * FROM health_records
        WHERE patientId = :patientId AND createdAt >= :startOfDay AND createdAt < :startOfNextDay
        ORDER BY createdAt DESC
        """,
    )
    fun observeRecordsForDate(
        patientId: String,
        startOfDay: Long,
        startOfNextDay: Long,
    ): Flow<List<HealthRecordEntity>>

    @Query(
        """
        SELECT * FROM health_records
        WHERE patientId = :patientId
        ORDER BY createdAt DESC
        """,
    )
    fun observeAllRecords(patientId: String): Flow<List<HealthRecordEntity>>

    @Query(
        """
        SELECT * FROM health_records
        WHERE patientId = :patientId
        ORDER BY createdAt DESC
        LIMIT 1
        """,
    )
    fun observeLatestRecord(patientId: String): Flow<HealthRecordEntity?>

    @Query("SELECT * FROM health_records WHERE id = :recordId")
    suspend fun getRecord(recordId: String): HealthRecordEntity?

    @Query("SELECT * FROM health_records WHERE id = :recordId")
    fun observeRecord(recordId: String): Flow<HealthRecordEntity?>

    @Query("DELETE FROM health_records WHERE id = :recordId")
    suspend fun deleteRecord(recordId: String)

    @Upsert
    suspend fun upsertRecord(record: HealthRecordEntity)

    @Upsert
    suspend fun upsertSingleMetricLimits(metricLimits: List<SingleMetricLimitEntity>)

    @Upsert
    suspend fun upsertBloodPressureLimits(metricLimits: List<BloodPressureLimitEntity>)

    @Query("SELECT * FROM single_metric_limits WHERE id = :id LIMIT 1")
    suspend fun getSingleMetricLimitById(id: String): SingleMetricLimitEntity?

    @Query("SELECT * FROM single_metric_limits WHERE ageGroup = :ageGroup")
    suspend fun getSingleMetricLimits(ageGroup: String): List<SingleMetricLimitEntity>

    @Query("SELECT * FROM blood_pressure_limits WHERE ageGroup = :ageGroup")
    suspend fun getBloodPressureLimits(ageGroup: String): BloodPressureLimitEntity?
}
