package cl.fabiacosta.proyecto_servicios.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "meter_readings")
data class MeterReading(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val readingValue: Int,
    val date: LocalDate,
    val meterId: String,
    val notes: String
)