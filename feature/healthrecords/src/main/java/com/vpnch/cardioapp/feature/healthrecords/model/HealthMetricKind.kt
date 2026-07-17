package com.vpnch.cardioapp.feature.healthrecords.model

enum class HealthMetricKind(val routeKey: String) {
    BloodPressure("blood_pressure"),
    RespiratoryRate("respiratory"),
    HeartRate("heart_rate"),
    OxygenSaturation("oxygen"),
    INR("inr"),
    ;

    companion object {
        fun fromRouteKey(key: String): HealthMetricKind {
            return entries.first { it.routeKey == key }
        }
    }
}
