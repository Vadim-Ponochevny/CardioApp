package com.vpnch.cardioapp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.vpnch.cardioapp.core.database.entity.VitaminEntity
import com.vpnch.cardioapp.core.database.entity.VitaminIntakeDayEntity
import com.vpnch.cardioapp.core.database.entity.VitaminIntakeEntity
import com.vpnch.cardioapp.core.database.model.VitaminIntakeSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VitaminDao {
    @Query("SELECT * FROM vitamins WHERE patientId = :patientId ORDER BY name")
    fun observeVitamins(patientId: String): Flow<List<VitaminEntity>>

    @Query("SELECT * FROM vitamins WHERE id = :vitaminId")
    suspend fun getVitamin(vitaminId: String): VitaminEntity?

    @Upsert
    suspend fun upsertVitamin(vitamin: VitaminEntity)

    @Query("DELETE FROM vitamins WHERE id = :vitaminId")
    suspend fun deleteVitamin(vitaminId: String)

    @Query("SELECT * FROM vitamin_intake_days WHERE patientId = :patientId AND date = :date")
    suspend fun getIntakeDay(patientId: String, date: String): VitaminIntakeDayEntity?

    @Upsert
    suspend fun upsertIntakeDay(intakeDay: VitaminIntakeDayEntity)

    @Upsert
    suspend fun upsertIntake(intake: VitaminIntakeEntity)

    @Transaction
    @Query(
        """
        SELECT vitamins.*,
               vitamin_intakes.id AS intakeId,
               vitamin_intakes.intakeDayId AS intakeDayId,
               vitamin_intakes.vitaminId AS intakeVitaminId,
               vitamin_intakes.isTaken AS isTaken,
               vitamin_intakes.takenAt AS takenAt,
               vitamin_intakes.updatedAt AS intakeUpdatedAt
        FROM vitamins
        LEFT JOIN vitamin_intake_days
            ON vitamin_intake_days.patientId = vitamins.patientId AND vitamin_intake_days.date = :date
        LEFT JOIN vitamin_intakes
            ON vitamin_intakes.intakeDayId = vitamin_intake_days.id AND vitamin_intakes.vitaminId = vitamins.id
        WHERE vitamins.patientId = :patientId
        ORDER BY vitamins.name
        """,
    )
    fun observeVitaminIntakes(patientId: String, date: String): Flow<List<VitaminIntakeSummaryEntity>>
}
