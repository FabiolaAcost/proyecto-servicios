package cl.fabiacosta.proyecto_servicios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.fabiacosta.proyecto_servicios.data.MeterReadingRepository

class MeterReadingViewModelFactory(
    private val repository: MeterReadingRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeterReadingViewModel::class.java)) {
            return MeterReadingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}