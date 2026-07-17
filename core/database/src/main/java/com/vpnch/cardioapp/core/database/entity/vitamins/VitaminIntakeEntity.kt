package com.vpnch.cardioapp.core.database.entity.vitamins

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vitamin_intakes",
    foreignKeys = [
        ForeignKey(
            entity = VitaminIntakeDayEntity::class,
            parentColumns = ["id"],
            childColumns = ["intakeDayId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = VitaminEntity::class,
            parentColumns = ["id"],
            childColumns = ["vitaminId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("intakeDayId"),
        Index("vitaminId"),
        Index(value = ["intakeDayId", "vitaminId"], unique = true),
    ],
)
data class VitaminIntakeEntity(
    @PrimaryKey val id: String,
    val intakeDayId: String,
    val vitaminId: String,
    val isTaken: Boolean,
    val takenAt: Long?,
    val updatedAt: Long,
)
