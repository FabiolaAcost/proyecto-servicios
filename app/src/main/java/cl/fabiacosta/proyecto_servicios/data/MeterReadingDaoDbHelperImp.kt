package cl.fabiacosta.proyecto_servicios.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.time.LocalDate

class MeterReadingDaoDbHelperImp(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), MeterReadingDao {
    companion object {
        const val DB_NAME = "meter_readings.db"
        const val DB_VERSION = 3
        const val TABLE_NAME = "meter_readings"
        const val COL_ID = "id"
        const val COL_METER_ID = "meter_id"
        const val COL_DATE = "date"
        const val COL_METER_TYPE = "meter_type"
        const val DB_SQL_CREATE_TABLES = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_METER_ID TEXT,
                $COL_DATE INTEGER,
                $COL_METER_TYPE TEXT
            );
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DB_SQL_CREATE_TABLES)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
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
                val meterId = getString(getColumnIndexOrThrow(COL_METER_ID))
                val meterType = getString(getColumnIndexOrThrow(COL_METER_TYPE))
                val dateNum = getLong(getColumnIndexOrThrow(COL_DATE))
                val date = LocalDate.ofEpochDay(dateNum)
                val reading = MeterReading(id, meterId, meterType,date)
                readings.add(reading)
            }
        }
        return readings
    }

    override suspend fun findById(id: Int): MeterReading? {
        TODO("Not yet implemented")
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
            put(COL_METER_ID, reading.meterId)
            put(COL_METER_TYPE, reading.meterType)
            put(COL_DATE, reading.date.toEpochDay())
        }
        this.writableDatabase.insert(TABLE_NAME, null, valores)
    }
}