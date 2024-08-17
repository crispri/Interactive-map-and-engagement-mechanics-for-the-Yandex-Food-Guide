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
import model.Filter
import model.MainScreenEvent
import model.NavigateToLocationEvent
import model.SaveInCollectionEvent
import model.SelectItemFromBottomSheet
import model.SelectItemFromMap
import model.UpdateItemsOnMap
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
        fetchRestaurants(
            _uiState.value.lowerLeft,
            _uiState.value.topRight,
            _uiState.value.filterList
        )
        fetchCollections()
    }

    private fun fetchRestaurants(lowerLeft: Point, topRight: Point, filterList: List<Filter>) {
        viewModelScope.launch {
            Log.e(
                "in fetchRestaurants",
                "${lowerLeft.latitude}, ${lowerLeft.longitude}, ${topRight.latitude}, ${topRight.longitude}, "
            )
            repository.getRestaurants(
                "Asd",
                lowerLeftLat = lowerLeft.latitude,
                lowerLeftLon = lowerLeft.longitude,
                topRightLat = topRight.latitude,
                topRightLon = topRight.longitude,
                filterList = filterList
            )
                .collect { state ->
                    when (state) {
                        is NetworkState.Failure -> {
                            Log.d("NetworkException", "NetworkFailure")

                            // Пока не работает бек, возвращаем захардкоженные данные
                            _uiState.update {
                                it.copy(
                                    errorMessage = state.cause.message,
                                    isLoading = false,
                                    restaurantsOnMap = Utils.restaurants,
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

                    }
                }
        }
    }

    private fun fetchCollections() {
        viewModelScope.launch {
            Log.e(
                "in fetchRestaurants", ""
            )
            repository.getCollections(
                "Asd",
            )
                .collect { state ->
                    when (state) {
                        is NetworkState.Failure -> {
                            Log.d("NetworkException", "NetworkFailure")

                            _uiState.update {
                                it.copy(
                                    errorMessage = state.cause.message,
                                    isLoading = false,
                                    recommendations = Utils.recommendations,
                                )
                            }
                        }

                        is NetworkState.Success -> {
                            Log.d("NetworkSuccess", "")
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    recommendations = state.data,
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

    fun send(event: MainScreenEvent) {
        when (event) {
            is SaveInCollectionEvent -> {
                saveInCollection(event.restaurantId)
            }

            is NavigateToLocationEvent -> {
                _uiState.update {
                    it.copy(centeringIsRequired = true)
                }
            }

            is CancelCentering -> {
                _uiState.update {
                    it.copy(centeringIsRequired = false)
                }
            }

            is UpdateItemsOnMap -> {
                fetchRestaurants(event.lowerLeft, event.topRight, event.filterList)
            }

            is SelectItemFromMap -> {
                _uiState.update { it.copy(selectedItemFromMapId = event.itemId, selectedItemFromBottomSheetId = null) }
            }

            is SelectItemFromBottomSheet -> {
                _uiState.update { it.copy(selectedItemFromBottomSheetId = event.itemId, selectedItemFromMapId = null) }
            }

            is SelectFilter -> {
                selectFilter(event.isAdding, event.filter)
            }
        }
    }

    private fun selectFilter(isAdding : Boolean, filter: Filter){
        val newList : MutableList<Filter> = _uiState.value.filterList
        if (isAdding){
            Log.d("selectFilter", "isAdding ${filter.property}")
            val newFilter = filter.copy(isSelected = true)
            newList.add(newFilter)
            _uiState.value.filterMap[filter.property] = true
        } else{
            Log.d("selectFilter", "remove ${filter.property}")
            newList.remove(filter)
            _uiState.value.filterMap[filter.property] = false
        }
        _uiState.update { it.copy(filterList = newList) }
    }

    private fun saveInCollection(restaurantId: String) {

    }

}