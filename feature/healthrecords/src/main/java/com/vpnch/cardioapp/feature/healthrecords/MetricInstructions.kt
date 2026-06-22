package com.vpnch.cardioapp.feature.healthrecords

val HealthMetricKind.measurementInstruction: String?
    get() = when (this) {
        HealthMetricKind.RespiratoryRate ->
            "Положи ладонь на свой животик и посиди спокойно. Посчитай, сколько раз твой живот поднялся вверх за один вдох, за минуту"

        HealthMetricKind.HeartRate ->
            "Положи два пальчика на запястье другой руки или на шею и почувствуй, как внутри кто-то стучит. Посчитай, сколько таких ударов-толчков ты насчитаешь за одну минуту."

        HealthMetricKind.OxygenSaturation ->
            "Надень специальный приборчик (пульсоксиметр) на пальчик, как будто колечко, и держи ручку спокойно. Посиди тихонечко несколько секунд, пока прибор светит на твой ноготок, и посмотри на экран"

        HealthMetricKind.BloodPressure -> null
    }
