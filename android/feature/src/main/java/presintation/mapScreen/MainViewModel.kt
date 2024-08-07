package presintation.mapScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.Event
import model.SaveInCollectionEvent
import repository.RestaurantRepositoryImpl
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RestaurantRepositoryImpl,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            repository.restaurants.collect { restaurants ->
                _uiState.update {
                    it.copy(
                        restaurantsOnMap = restaurants,
                    )
                }
            }

            repository.recommendations.collect { recommendations ->
                _uiState.update {
                    it.copy(
                        recommendations = recommendations,
                    )
                }
            }
        }
    }

    fun send(event: Event) {
        when (event) {
            is SaveInCollectionEvent -> {
                saveInCollection(event.restaurantId)
            }


        }
    }

    private fun saveInCollection(restaurantId: String) {

    }

}