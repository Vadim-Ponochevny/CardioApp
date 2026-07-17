package com.vpnch.cardioapp.core.model.vitamins

data class Vitamin(
    val id: String,
    val patientId: String,
    val name: String,
    val dose: String?,
)
