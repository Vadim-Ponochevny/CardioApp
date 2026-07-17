package com.vpnch.cardioapp.core.model.help

data class Faq(
    val id: String,
    val question: String,
    val answer: String,
    val sortOrder: Int,
    val isActive: Boolean,
)
