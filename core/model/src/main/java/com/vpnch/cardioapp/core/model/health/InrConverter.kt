package com.vpnch.cardioapp.core.model.health

// INR is stored as integer tenths (e.g. 2.5 → 25) so the domain model stays Int-only.
object InrConverter {
    fun toStoredInt(input: String): Int? =
        input.toDoubleOrNull()?.times(10)?.toInt()?.takeIf { it > 0 }

    fun fromStoredInt(stored: Int): String {
        val v = stored / 10.0
        return if (v == v.toLong().toDouble()) v.toLong().toString() else v.toString()
    }
}
