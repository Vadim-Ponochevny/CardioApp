package com.vpnch.cardioapp.core.ui

import androidx.annotation.DrawableRes

data class MetricDisplayItem(
    @DrawableRes val iconRes: Int,
    val value: String,
    val label: String,
)
