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

    init {
        getRestaurantById("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    }
    fun send(event: RestaurantScreenEvent) {
        when (event) {
            is GetRestaurantInfo -> {
                getRestaurantById(event.restaurantId)
            }
        }
    }

    fun getRestaurantById(restaurantId: String?) {
        if (restaurantId == null) {
            _uiState.update {
                it.copy(
                    currentRestaurant = Utils.restaurants[0]
                )
            }
        }
        else {
            viewModelScope.launch {
                repository.getRestaurantById("Asd", restaurantId)
                    .collect { state ->
                        when (state) {
                            is NetworkState.Failure -> {
                                _uiState.update {
                                    it.copy(
                                        errorMessage = state.cause.message,
                                        isLoading = false,
                                        currentRestaurant = Utils.restaurants[0]
                                    )
                                }
                            }

                            is NetworkState.Success -> {
                                Log.d("NetworkSuccess", "")
                                _uiState.update {
                                    it.copy(
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