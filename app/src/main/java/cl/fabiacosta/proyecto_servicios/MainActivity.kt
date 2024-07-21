package cl.fabiacosta.proyecto_servicios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.lifecycle.lifecycleScope
import cl.fabiacosta.proyecto_servicios.data.MeterReadingRepository
import cl.fabiacosta.proyecto_servicios.ui.theme.AppMeterReadings
import cl.fabiacosta.proyecto_servicios.ui.theme.meterReadingsDePrueba
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    private lateinit var meterReadingRepository: MeterReadingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        meterReadingRepository = MeterReadingRepository.getInstance(this)

        lifecycleScope.launch(Dispatchers.IO) {
            meterReadingsDePrueba().forEach {
                meterReadingRepository.agregar(it)
            }
        }

        setContent {
            AppMeterReadings()
        }
    }
}