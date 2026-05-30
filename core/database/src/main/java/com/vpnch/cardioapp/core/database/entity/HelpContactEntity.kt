package com.vpnch.cardioapp.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "help_contacts",
    indices = [Index("sortOrder"), Index("isActive")],
)
data class HelpContactEntity(
    @PrimaryKey val id: String,
    val title: String,
    val phone: String,
    val description: String?,
    val sortOrder: Int,
    val isActive: Boolean,
)
