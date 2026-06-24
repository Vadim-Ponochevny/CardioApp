package com.vpnch.cardioapp.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS health_records_new (
                id TEXT NOT NULL PRIMARY KEY,
                patientId TEXT NOT NULL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL,
                systolicPressure INTEGER,
                diastolicPressure INTEGER,
                respiratoryRate INTEGER,
                heartRate INTEGER,
                oxygenSaturation INTEGER
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            INSERT INTO health_records_new (
                id,
                patientId,
                createdAt,
                updatedAt,
                systolicPressure,
                diastolicPressure,
                respiratoryRate,
                heartRate,
                oxygenSaturation
            )
            SELECT
                id,
                patientId,
                createdAt,
                updatedAt,
                systolicPressure,
                diastolicPressure,
                respiratoryRate,
                heartRate,
                oxygenSaturation
            FROM health_records
            """.trimIndent(),
        )
        db.execSQL("DROP TABLE health_records")
        db.execSQL("ALTER TABLE health_records_new RENAME TO health_records")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_health_records_patientId ON health_records(patientId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_health_records_createdAt ON health_records(createdAt)")
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS index_health_records_patientId_createdAt " +
                "ON health_records(patientId, createdAt)",
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE health_records ADD COLUMN inr INTEGER")
    }
}
