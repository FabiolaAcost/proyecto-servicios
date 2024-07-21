package cl.fabiacosta.proyecto_servicios.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MeterReadingDao {
    @Query("SELECT * FROM meter_readings")
    suspend fun getAll(): List<MeterReading>

    @Query("SELECT * FROM meter_readings WHERE id = :id")
    suspend fun findById(id: Int): MeterReading?

    @Query("SELECT COUNT(*) FROM meter_readings")
    suspend fun count(): Int

    @Insert
    suspend fun insertAll(vararg readings: MeterReading)

    @Update
    suspend fun update(reading: MeterReading)

    @Delete
    suspend fun delete(reading: MeterReading)
}