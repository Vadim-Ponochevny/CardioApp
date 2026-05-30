package com.vpnch.cardioapp.core.model

data class HelpContact(
    val id: String,
    val title: String,
    val phone: String,
    val description: String?,
    val sortOrder: Int,
    val isActive: Boolean,
)

data class Faq(
    val id: String,
    val question: String,
    val answer: String,
    val sortOrder: Int,
    val isActive: Boolean,
)
