package presintation.restaurantScreen

import Utils
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.Restaurant
import network.util.NetworkState
import repository.RestaurantRepositoryImpl
import ui.GetRestaurantInfo
import ui.RestaurantScreenEvent
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val repository: RestaurantRepositoryImpl,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantUiState())
    val uiState = _uiState.asStateFlow()

    fun send(event: RestaurantScreenEvent) {
        when (event) {
            is GetRestaurantInfo -> {
                getRestaurantById(event.restaurantId)
            }
        }
    }

    fun getRestaurantById(restaurantId: String){
        viewModelScope.launch {
            repository.getRestaurants("Asd", lowerLeftLat = 55.0, lowerLeftLon = 37.0, topRightLat = 56.0, topRightLon =  38.0, maxCount = 0)
                .collect { state ->
                    when (state) {
                        is NetworkState.Failure -> {
                            Utils.restaurants[0]
                        }

                        is NetworkState.Success -> {
                            val index = state.data.indexOfFirst { it.id == restaurantId }
                            Log.d("NetworkSuccess", "")
                            _uiState.update {
                                it.copy(
                                    currentRestaurant = state.data[index],
                                )
                            }
                        }

                        is NetworkState.Loading -> {

                        }

                        else -> {}
                    }
                }
        }
    }

}