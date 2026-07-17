package com.vpnch.cardioapp.feature.profile

import com.vpnch.cardioapp.core.model.patient.AgeGroup
import com.vpnch.cardioapp.core.model.health.limits.BloodPressureLimits
import com.vpnch.cardioapp.core.model.health.limits.SingleMetricLimits
import com.vpnch.cardioapp.core.model.patient.ValveType

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
    val customLimitsStep: Int? = null, // null=main, 0=ЧСС, 1=АД, 2=SpO2, 3=МНО
    val isSingleMetricEdit: Boolean = false, // открыт конкретный шаг, а не весь мастер
    val isSaved: Boolean = false,
    val notificationsEnabled: Boolean = false,
)

data class AgeGroupLimitsUi(
    val singleMetricLimits: List<SingleMetricLimits>,
    val bloodPressureLimits: BloodPressureLimits?,
)

data class CustomLimitsEditState(
    // Heart Rate (3 zones)
    val hrNormalMin: String = "", val hrNormalMax: String = "",
    val hrAttentionMin: String = "", val hrAttentionMax: String = "",
    val hrDoctorMax: String = "",
    // Oxygen Saturation (3 zones)
    val spO2NormalMin: String = "", val spO2NormalMax: String = "",
    val spO2AttentionMin: String = "", val spO2AttentionMax: String = "",
    val spO2DoctorMax: String = "",
    // Respiratory Rate (3 zones)
    val rrNormalMin: String = "", val rrNormalMax: String = "",
    val rrAttentionMin: String = "", val rrAttentionMax: String = "",
    val rrDoctorMax: String = "",
    // Blood Pressure (normal range; doctor = same bounds)
    val bpSystolicMin: String = "", val bpSystolicMax: String = "",
    val bpDiastolicMin: String = "", val bpDiastolicMax: String = "",
    // МНО — stored as ×10 in DB (2.5 → 25); user enters decimals here
    val inrNormalMin: String = "", val inrNormalMax: String = "",
    val inrAttentionMin: String = "", val inrAttentionMax: String = "",
    val inrDoctorMax: String = "",
)
