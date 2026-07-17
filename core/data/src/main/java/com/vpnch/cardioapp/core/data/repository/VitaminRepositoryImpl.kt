package com.vpnch.cardioapp.core.data.repository

import com.vpnch.cardioapp.core.data.database.asEntity
import com.vpnch.cardioapp.core.data.database.asExternalModel
import com.vpnch.cardioapp.core.database.dao.VitaminDao
import com.vpnch.cardioapp.core.database.entity.vitamins.VitaminIntakeDayEntity
import com.vpnch.cardioapp.core.database.entity.vitamins.VitaminIntakeEntity
import com.vpnch.cardioapp.core.domain.repository.VitaminRepository
import com.vpnch.cardioapp.core.model.vitamins.Vitamin
import com.vpnch.cardioapp.core.model.vitamins.VitaminIntakeSummary
import com.vpnch.cardioapp.core.model.vitamins.TakenVitaminOnDate
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VitaminRepositoryImpl @Inject constructor(
    private val vitaminDao: VitaminDao,
) : VitaminRepository {

    override fun observeVitamins(patientId: String): Flow<List<Vitamin>> {
        return vitaminDao.observeVitamins(patientId).map { vitamins ->
            vitamins.map { it.asExternalModel() }
        }
    }

    override fun observeVitaminIntakes(
        patientId: String,
        date: String,
    ): Flow<List<VitaminIntakeSummary>> {
        return vitaminDao.observeVitaminIntakes(patientId, date).map { summaries ->
            summaries.map { it.asExternalModel() }
        }
    }

    override fun observeTakenVitaminsHistory(patientId: String): Flow<List<TakenVitaminOnDate>> {
        return vitaminDao.observeTakenVitaminsHistory(patientId).map { rows ->
            rows.map { row ->
                TakenVitaminOnDate(
                    date = row.date,
                    vitaminId = row.vitaminId,
                    vitaminName = row.vitaminName,
                )
            }
        }
    }

    override suspend fun getVitamin(vitaminId: String): Vitamin? {
        return vitaminDao.getVitamin(vitaminId)?.asExternalModel()
    }

    override suspend fun saveVitamin(vitamin: Vitamin) {
        vitaminDao.upsertVitamin(vitamin.asEntity())
    }

    override suspend fun deleteVitamin(vitaminId: String) {
        vitaminDao.deleteVitamin(vitaminId)
    }

    override suspend fun setVitaminTaken(
        patientId: String,
        date: String,
        vitaminId: String,
        isTaken: Boolean,
    ) {
        val now = System.currentTimeMillis()
        val intakeDay = vitaminDao.getIntakeDay(patientId, date) ?: createIntakeDay(patientId, date, now)
        val intakeEntity = VitaminIntakeEntity(
            id = "${intakeDay.id}-$vitaminId",
            intakeDayId = intakeDay.id,
            vitaminId = vitaminId,
            isTaken = isTaken,
            takenAt = if (isTaken) now else null,
            updatedAt = now,
        )
        vitaminDao.upsertIntakeDayAndIntake(intakeDay, intakeEntity)
    }

    private fun createIntakeDay(
        patientId: String,
        date: String,
        now: Long,
    ) = VitaminIntakeDayEntity(
        id = UUID.randomUUID().toString(),
        patientId = patientId,
        date = date,
        createdAt = now,
        updatedAt = now,
    )
}
