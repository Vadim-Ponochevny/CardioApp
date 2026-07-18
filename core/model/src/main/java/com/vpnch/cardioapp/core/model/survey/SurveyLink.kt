package com.vpnch.cardioapp.core.model.survey

data class SurveyLink(
    val id: String,
    val title: String,
    val url: String,
    val isActive: Boolean,
    val isNew: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
)