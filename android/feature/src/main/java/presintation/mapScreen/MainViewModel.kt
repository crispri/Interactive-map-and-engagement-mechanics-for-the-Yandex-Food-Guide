package presintation.mapScreen

import Utils
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.CancelCentering
import model.ChangeDeviceLocation
import model.Event
import model.NavigateToLocationEvent
import model.SaveInCollectionEvent
import network.util.NetworkState
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
            repository.getRestaurants("Asd", lowerLeftLat = 55.0, lowerLeftLon = 37.0, topRightLat = 56.0, topRightLon =  38.0, maxCount = 0)
                .collect { state ->
                    when (state) {
                        is NetworkState.Failure -> {
                            Log.d("NetworkException", "NetworkFailure")

                            // Пока не работает бек, возвращаем захардкоженные данные
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    restaurantsOnMap = Utils.restaurants,
                                    recommendations = Utils.recommendations,
                                    listOfRestaurant = Utils.restaurants,
                                )
                            }
                        }

                        is NetworkState.Success -> {
                            Log.d("NetworkSuccess", "")
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    restaurantsOnMap = state.data,
                                    recommendations = Utils.recommendations,
                                    listOfRestaurant = state.data,
                                )
                            }
                        }

                        is NetworkState.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }

                        else -> {}
                    }
                }

//            repository.restaurants.collect { restaurants ->
//                _uiState.update {
//                    it.copy(
//                        restaurantsOnMap = restaurants,
//                    )
//                }
//            }
//
//            repository.recommendations.collect { recommendations ->
//                _uiState.update {
//                    it.copy(
//                        recommendations = recommendations,
//                    )
//                }
//            }
        }
    }

    fun send(event: Event) {                       //распарсить все события из Event
        when (event) {
            is SaveInCollectionEvent -> {
                saveInCollection(event.restaurantId)
            }
            is NavigateToLocationEvent -> {
                _uiState.update {
                    it.copy( centeringIsRequired = true)
                }
            }
            is CancelCentering -> {
                _uiState.update {
                    it.copy( centeringIsRequired = false)
                }
            }
            /*is ChangeDeviceLocation -> {
                _uiState.update {
                    it.copy( currentDeviceLocation = event.curLocation)
                }
            }*/



        }
    }

    private fun saveInCollection(restaurantId: String) {

    }

}