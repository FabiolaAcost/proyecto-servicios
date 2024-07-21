package cl.fabiacosta.proyecto_servicios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.fabiacosta.proyecto_servicios.data.MeterReading
import cl.fabiacosta.proyecto_servicios.data.MeterReadingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MeterReadingViewModel(private val repository: MeterReadingRepository) : ViewModel() {

    private val _readings = MutableStateFlow<List<MeterReading>>(emptyList())
    val readings: StateFlow<List<MeterReading>> = _readings

    init {
        loadReadings()
    }

    private fun loadReadings() {
        viewModelScope.launch {
            _readings.value = repository.obtenerTodos()
        }
    }

    fun addReading(meterReading: MeterReading) {
        viewModelScope.launch {
            repository.agregar(meterReading)
            loadReadings()
        }
    }
}