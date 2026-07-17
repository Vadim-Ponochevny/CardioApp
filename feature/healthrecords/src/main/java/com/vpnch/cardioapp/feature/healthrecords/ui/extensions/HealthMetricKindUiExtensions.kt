package com.vpnch.cardioapp.feature.healthrecords.ui.extensions

import androidx.annotation.DrawableRes
import com.vpnch.cardioapp.feature.healthrecords.model.HealthMetricKind
import com.vpnch.cardioapp.core.ui.R as CoreUiR

val HealthMetricKind.unitLabel: String?
    get() = when (this) {
        HealthMetricKind.RespiratoryRate -> "ЧД/мин"
        HealthMetricKind.HeartRate -> "УД/мин"
        HealthMetricKind.OxygenSaturation -> "%"
        else -> null
    }

val HealthMetricKind.displayTitle: String
    get() = when (this) {
        HealthMetricKind.BloodPressure -> "Давление"
        HealthMetricKind.RespiratoryRate -> "Дыхание"
        HealthMetricKind.HeartRate -> "Пульс"
        HealthMetricKind.OxygenSaturation -> "Кислород"
        HealthMetricKind.INR -> "МНО"
    }

@get:DrawableRes
val HealthMetricKind.iconRes: Int
    get() = when (this) {
        HealthMetricKind.BloodPressure -> CoreUiR.drawable.ic_metric_pressure
        HealthMetricKind.RespiratoryRate -> CoreUiR.drawable.ic_metric_breath
        HealthMetricKind.HeartRate -> CoreUiR.drawable.ic_metric_pulse
        HealthMetricKind.OxygenSaturation -> CoreUiR.drawable.ic_metric_o2
        HealthMetricKind.INR -> CoreUiR.drawable.ic_metric_inr
    }

val HealthMetricKind.measurementInstruction: String?
    get() = when (this) {
        HealthMetricKind.RespiratoryRate ->
            "Частота дыхания — это сколько раз в минуту делается вдох. Специальный прибор или врач помогут измерить это точно."

        HealthMetricKind.HeartRate ->
            "Пульс показывает, сколько раз в минуту бьётся сердце. Его можно измерить пульсоксиметром, тонометром или фитнес-браслетом."

        HealthMetricKind.OxygenSaturation ->
            "Насыщение крови кислородом измеряет пульсоксиметр — небольшой прибор, который надевается на палец."

        HealthMetricKind.BloodPressure -> null
        HealthMetricKind.INR ->
            "МНО показывает, насколько хорошо свёртывается кровь. Его определяют по анализу крови из вены или пальца. При приёме варфарина важно, чтобы значение держалось в нужном коридоре — его задаёт врач."
    }

