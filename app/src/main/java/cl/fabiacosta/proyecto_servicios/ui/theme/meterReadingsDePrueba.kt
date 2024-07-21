package cl.fabiacosta.proyecto_servicios.ui.theme

import cl.fabiacosta.proyecto_servicios.data.MeterReading
import java.time.LocalDate

fun meterReadingsDePrueba(): List<MeterReading> {
    return listOf(
        MeterReading(1, 12345, LocalDate.now(), "Meter1", "Initial reading"),
        MeterReading(2, 12360, LocalDate.now(), "Meter2", "Second reading"),
        MeterReading(3, 12400, LocalDate.now(), "Meter3", "Third reading")
    )
}