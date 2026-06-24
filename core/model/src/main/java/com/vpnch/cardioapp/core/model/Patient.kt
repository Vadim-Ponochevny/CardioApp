package com.vpnch.cardioapp.core.model

data class Patient(
    val id: String,
    val birthDate: String,
    val ageGroup: AgeGroup,
    val userType: UserType,
    val takesWarfarin: Boolean = false,
    val valveType: ValveType? = null,
    val useCustomLimits: Boolean = false,
)
