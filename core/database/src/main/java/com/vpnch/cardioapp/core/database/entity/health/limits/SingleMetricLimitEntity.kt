package com.vpnch.cardioapp.core.database.entity.health.limits

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "single_metric_limits",
    indices = [Index(value = ["ageGroup", "metricType"], unique = true)],
)
data class SingleMetricLimitEntity(
    @PrimaryKey val id: String,
    val ageGroup: String,
    val metricType: String,
    val normalMin: Int,
    val normalMax: Int,
    val attentionMin: Int?,
    val attentionMax: Int?,
    val doctorSoonMin: Int?,
    val doctorSoonMax: Int?,
)
