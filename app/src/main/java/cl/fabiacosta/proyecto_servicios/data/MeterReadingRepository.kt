package cl.fabiacosta.proyecto_servicios.data

import android.content.Context
import androidx.room.Room

class MeterReadingRepository(
    private val meterReadingDao: MeterReadingDao
) {
    suspend fun obtenerTodos(): List<MeterReading> = meterReadingDao.getAll()

    suspend fun agregar(reading: MeterReading) = meterReadingDao.insertAll(reading)

    suspend fun contarRegistros(): Int = meterReadingDao.count()

    companion object {
        @Volatile
        private var instance: MeterReadingRepository? = null

        fun getInstance(context: Context): MeterReadingRepository {
            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meter_readings.db"
                ).build()
                MeterReadingRepository(database.meterReadingDao()).also {
                    instance = it
                }
            }
        }
    }
}
