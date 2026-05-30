package com.vpnch.cardioapp.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "faqs",
    indices = [Index("sortOrder"), Index("isActive")],
)
data class FaqEntity(
    @PrimaryKey val id: String,
    val question: String,
    val answer: String,
    val sortOrder: Int,
    val isActive: Boolean,
)
