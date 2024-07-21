package cl.fabiacosta.proyecto_servicios.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cl.fabiacosta.proyecto_servicios.LocalDateConverter

@Database(entities = [MeterReading::class], version = 1)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun meterReadingDao(): MeterReadingDao
}