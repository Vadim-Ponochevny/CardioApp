package com.vpnch.cardioapp.core.model.help

data class HelpContact(
    val id: String,
    val title: String,
    val phone: String,
    val description: String?,
    val sortOrder: Int,
    val isActive: Boolean,
)