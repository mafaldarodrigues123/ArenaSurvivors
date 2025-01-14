package fcul.mei.cm.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fcul.mei.cm.app.database.CoordinatesRepository
import fcul.mei.cm.app.domain.Coordinates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArenaMapViewModel : ViewModel() {

    private val coordinatesDatabase = CoordinatesRepository()

    private val _coordinatesFlow = MutableStateFlow<Coordinates?>(null)
    val coordinatesFlow: StateFlow<Coordinates?> = _coordinatesFlow

    init {
        observeCoordinates()
    }

    private fun observeCoordinates() {
        /*coordinatesDatabase.getCoordinates { coordinates ->
            _coordinatesFlow.value = coordinates
        }*/
    }

    fun saveCoordinates(coordinates: Coordinates) {
        viewModelScope.launch {
            coordinatesDatabase.saveCoordinates(
                "2",
                coordinates
            )
        }
    }
}