package com.vpnch.cardioapp.feature.profile

import com.vpnch.cardioapp.core.model.AgeGroup
import com.vpnch.cardioapp.core.model.BloodPressureLimits
import com.vpnch.cardioapp.core.model.SingleMetricLimits
import com.vpnch.cardioapp.core.model.ValveType

data class ProfileUiState(
    val isLoading: Boolean = true,
    val patientId: String = "",
    val birthDate: String = "",
    val ageGroup: AgeGroup = AgeGroup.Age9To11,
    val takesWarfarin: Boolean = false,
    val valveType: ValveType? = null,
    val useCustomLimits: Boolean = false,
    val limitsForAgeGroup: AgeGroupLimitsUi? = null,
    val customLimitsEdit: CustomLimitsEditState? = null,
    val isSaved: Boolean = false,
)

data class AgeGroupLimitsUi(
    val singleMetricLimits: List<SingleMetricLimits>,
    val bloodPressureLimits: BloodPressureLimits?,
)

data class CustomLimitsEditState(
    val rrNormalMin: String = "",
    val rrNormalMax: String = "",
    val hrNormalMin: String = "",
    val hrNormalMax: String = "",
    val spO2NormalMin: String = "",
    val spO2NormalMax: String = "",
    val bpSystolicMin: String = "",
    val bpSystolicMax: String = "",
    val bpDiastolicMin: String = "",
    val bpDiastolicMax: String = "",
)
