package com.vpnch.cardioapp.feature.vitamins

import com.vpnch.cardioapp.core.model.vitamins.Vitamin

data class VitaminsUiState(
    val vitamins: List<Vitamin> = emptyList(),
)
