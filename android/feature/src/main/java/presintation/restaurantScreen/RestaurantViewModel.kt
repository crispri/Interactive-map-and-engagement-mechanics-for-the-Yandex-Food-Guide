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
                Log.d("SendGetRestaurant",event.restaurantId.toString())
            }
        }
    }

    private fun getRestaurantById(restaurantId: String?) {
        if (restaurantId == null) {
            _uiState.update {
                it.copy(
                    currentRestaurant = Utils.restaurants[0]
                )
            }
        } else {
            viewModelScope.launch {
                repository.getRestaurantById("session_id=5142cece-b22e-4a4f-adf9-990949d053ff", restaurantId)
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
                                        currentRestaurant = state.data,
                                        isLoading = false,
                                    )
                                }
                            }

                            is NetworkState.Loading -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = true,
                                    )
                                }
                            }

                            else -> {}
                        }
                    }
            }
        }
    }

}