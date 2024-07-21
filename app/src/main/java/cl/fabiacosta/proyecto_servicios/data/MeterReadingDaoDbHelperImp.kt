package cl.fabiacosta.proyecto_servicios.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import cl.fabiacosta.proyecto_servicios.LocalDateConverter
import java.time.LocalDate

class MeterReadingDaoDbHelperImp(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), MeterReadingDao {

    companion object {
        const val DB_NAME = "meter_readings.db"
        const val DB_VERSION = 1
        const val TABLE_NAME = "meter_readings"
        const val COL_ID = "id"
        const val COL_READING_VALUE = "reading_value"
        const val COL_DATE = "date"
        const val COL_METER_ID = "meter_id"
        const val COL_NOTES = "notes"
        const val DB_SQL_CREATE_TABLES = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_READING_VALUE INTEGER,
                $COL_DATE INTEGER,
                $COL_METER_ID TEXT,
                $COL_NOTES TEXT
            );
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DB_SQL_CREATE_TABLES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Implementación vacía por ahora
    }

    override suspend fun getAll(): List<MeterReading> {
        val cursor = this.readableDatabase.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val readings = mutableListOf<MeterReading>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COL_ID))
                val readingValue = getInt(getColumnIndexOrThrow(COL_READING_VALUE))
                val dateNum = getLong(getColumnIndexOrThrow(COL_DATE))
                val date = LocalDateConverter().fromTimestamp(dateNum) ?: LocalDate.now()
                val meterId = getString(getColumnIndexOrThrow(COL_METER_ID))
                val notes = getString(getColumnIndexOrThrow(COL_NOTES))
                val reading = MeterReading(id, readingValue, date, meterId, notes)
                readings.add(reading)
            }
        }
        return readings
    }

    override suspend fun findById(id: Int): MeterReading? {
        // Implementación vacía por ahora
        return null
    }

    override suspend fun count(): Int {
        val cursor = this.readableDatabase.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME", null)
        return if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            0
        }
    }

    override suspend fun insertAll(vararg readings: MeterReading) {
        readings.forEach {
            insert(it)
        }
    }

    private suspend fun insert(reading: MeterReading) {
        Log.v("MeterReadingDaoDbHelperImp", "::insert()")
        val valores = ContentValues().apply {
            put(COL_READING_VALUE, reading.readingValue)
            put(COL_DATE, LocalDateConverter().dateToTimestamp(reading.date))
            put(COL_METER_ID, reading.meterId)
            put(COL_NOTES, reading.notes)
        }
        this.writableDatabase.insert(TABLE_NAME, null, valores)
    }

    override suspend fun update(reading: MeterReading) {
        // Implementación vacía por ahora
    }

    override suspend fun delete(reading: MeterReading) {
        // Implementación vacía por ahora
    }
}
