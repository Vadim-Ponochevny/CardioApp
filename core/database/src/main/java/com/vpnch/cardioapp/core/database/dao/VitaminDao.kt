package com.vpnch.cardioapp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.vpnch.cardioapp.core.database.entity.vitamins.VitaminEntity
import com.vpnch.cardioapp.core.database.entity.vitamins.VitaminIntakeDayEntity
import com.vpnch.cardioapp.core.database.entity.vitamins.VitaminIntakeEntity
import com.vpnch.cardioapp.core.database.projection.TakenVitaminHistoryRow
import com.vpnch.cardioapp.core.database.projection.VitaminIntakeSummaryEntity
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
    suspend fun upsertIntakeDayAndIntake(
        intakeDay: VitaminIntakeDayEntity,
        intake: VitaminIntakeEntity,
    ) {
        upsertIntakeDay(intakeDay)
        upsertIntake(intake)
    }

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

    @Query(
        """
        SELECT
            vitamin_intake_days.date AS date,
            vitamins.id AS vitaminId,
            vitamins.name AS vitaminName
        FROM vitamin_intakes
        INNER JOIN vitamin_intake_days ON vitamin_intakes.intakeDayId = vitamin_intake_days.id
        INNER JOIN vitamins ON vitamin_intakes.vitaminId = vitamins.id
        WHERE vitamin_intake_days.patientId = :patientId AND vitamin_intakes.isTaken = 1
        ORDER BY vitamin_intake_days.date DESC, vitamins.name ASC
        """,
    )
    fun observeTakenVitaminsHistory(patientId: String): Flow<List<TakenVitaminHistoryRow>>
}
