package com.vpnch.cardioapp.core.domain

import com.vpnch.cardioapp.core.model.BloodPressureLimits
import com.vpnch.cardioapp.core.model.MetricStatus
import com.vpnch.cardioapp.core.model.SingleMetricLimits

class CheckHealthMetricLimitsUseCase {
    fun checkSingleValue(value: Int, limits: SingleMetricLimits?): MetricStatus {
        if (limits == null) return MetricStatus.Unknown

        return when {
            isBelowOrEqual(value, limits.doctorSoonMax) -> MetricStatus.DoctorSoon
            isInside(value, limits.normalMin, limits.normalMax) -> MetricStatus.Normal
            isInside(value, limits.attentionMin, limits.attentionMax) -> MetricStatus.Attention
            else -> MetricStatus.Attention
        }
    }

    fun checkBloodPressure(
        systolic: Int,
        diastolic: Int,
        limits: BloodPressureLimits?,
    ): MetricStatus {
        if (limits == null) return MetricStatus.Unknown

        val doctorSoon = isOutOfPressureDoctorSoonRange(systolic, diastolic, limits)
        val normal = isInside(systolic, limits.normalSystolicMin, limits.normalSystolicMax) &&
            isInside(diastolic, limits.normalDiastolicMin, limits.normalDiastolicMax)

        return when {
            doctorSoon -> MetricStatus.DoctorSoon
            normal -> MetricStatus.Normal
            else -> MetricStatus.Attention
        }
    }

    private fun isBelowOrEqual(value: Int, max: Int?): Boolean = max != null && value <= max

    private fun isInside(value: Int, min: Int?, max: Int?): Boolean {
        return min != null && max != null && value in min..max
    }

    private fun isOutOfPressureDoctorSoonRange(
        systolic: Int,
        diastolic: Int,
        limits: BloodPressureLimits,
    ): Boolean {
        return isLessThan(systolic, limits.doctorSoonSystolicLow) ||
            isMoreThan(systolic, limits.doctorSoonSystolicHigh) ||
            isLessThan(diastolic, limits.doctorSoonDiastolicLow) ||
            isMoreThan(diastolic, limits.doctorSoonDiastolicHigh)
    }

    private fun isLessThan(value: Int, limit: Int?): Boolean = limit != null && value < limit

    private fun isMoreThan(value: Int, limit: Int?): Boolean = limit != null && value > limit
}
