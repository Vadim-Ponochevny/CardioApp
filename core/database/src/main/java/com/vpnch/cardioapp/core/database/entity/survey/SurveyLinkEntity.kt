package com.vpnch.cardioapp.core.database.entity.survey

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "survey_links",
    indices = [Index("isActive")],
)
data class SurveyLinkEntity(
    @PrimaryKey val id: String,
    val title: String,
    val url: String,
    val isActive: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)