package com.vpnch.cardioapp.core.model.survey

data class SurveyLink(
    val id: String,
    val title: String,
    val url: String,
    val isActive: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)