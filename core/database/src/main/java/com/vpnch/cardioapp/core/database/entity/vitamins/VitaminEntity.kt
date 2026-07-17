package com.vpnch.cardioapp.core.database.entity.vitamins

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vitamins",
    indices = [Index("patientId")],
)
data class VitaminEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val name: String,
    val dose: String?,
)
