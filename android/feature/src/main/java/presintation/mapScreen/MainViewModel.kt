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
import model.RaiseCameraPosition
import model.RecommendationIsSelected
import model.Restaurant
import model.SaveInCollectionEvent
import model.SelectFilter
import model.SelectItemFromBottomSheet
import model.SelectItemFromMap
import model.SetNewList
import model.SwitchUserModeEvent
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
            _uiState.value.filterList,
            0.0,
            0.0
        )
        fetchCollections()
    }

    private fun fetchRestaurants(lowerLeft: Point, topRight: Point, filterList: List<Filter>, w:Double, h: Double) {
        viewModelScope.launch {
            Log.e(
                "in fetchRestaurants",
                "${lowerLeft.latitude}, ${lowerLeft.longitude}, ${topRight.latitude}, ${topRight.longitude}, "
            )
            repository.getRestaurants(
                "session_id=5142cece-b22e-4a4f-adf9-990949d053ff",
                lowerLeftLat = lowerLeft.latitude,
                lowerLeftLon = lowerLeft.longitude,
                topRightLat = topRight.latitude,
                topRightLon = topRight.longitude,
                onCollection = uiState.value.isCollectionMode,
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
                            Log.e("CameraListener", "1size = ${state.data.size}  list = ${state.data}")
                            val ls = filterNonOverlappingRestaurants(state.data, w, h)
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    restaurantsOnMap = ls,
                                    listOfRestaurant = ls,
                                )
                            }
                            Log.e("CameraListener", "2size = ${state.data.size}  list = ${state.data}")
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
                "session_id=5142cece-b22e-4a4f-adf9-990949d053ff",
                false,
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
        }
    }

    private fun fetchUserCollections() {
        viewModelScope.launch {
            Log.e(
                "in fetchRestaurants", ""
            )
            repository.getCollections(
                "session_id=5142cece-b22e-4a4f-adf9-990949d053ff",
                true,
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
        }
    }

    fun send(event: MainScreenEvent) {
        when (event) {
            is SaveInCollectionEvent -> {
                saveInCollection(event.collectionId, event.restaurantId)
            }

            is SwitchUserModeEvent -> {
                switchUserMode()
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
                fetchRestaurants(event.lowerLeft, event.topRight, event.filterList, event.w, event.h)
                _uiState.update { it.copy(curCoordinates = Pair(event.lowerLeft, event.topRight)) }
            }

            is SelectItemFromMap -> {
                _uiState.update {
                    it.copy(
                        selectedItemFromMapId = event.itemId,
                        selectedItemFromBottomSheetId = null
                    )
                }
            }

            is SelectItemFromBottomSheet -> {
                _uiState.update {
                    it.copy(
                        selectedItemFromBottomSheetId = event.itemId,
                        selectedItemFromMapId = null
                    )
                }
            }

            is RecommendationIsSelected -> {
                _uiState.update { it.copy(recommendationIsSelected = event.isSelected) }
            }

            is RaiseCameraPosition -> {
                _uiState.update { it.copy(raiseRequired = event.raiseRequired) }
            }


            is SelectFilter -> {
                selectFilter(event.isAdding, event.filter)
            }

            is SetNewList -> {
                _uiState.update { it.copy(restaurantsOnMap =  event.restaurants) }
            }
        }
    }

    private fun selectFilter(isAdding: Boolean, filter: Filter) {
        val newList: MutableList<Filter> = _uiState.value.filterList
        if (isAdding) {
            Log.d("selectFilter", "isAdding ${filter.property}")
            val newFilter = filter.copy(isSelected = true)
            newList.add(newFilter)
            _uiState.value.filterMap[filter.property] = true
            fetchRestaurants(uiState.value.lowerLeft, uiState.value.topRight, newList, 0.0,0.0)
        } else {
            Log.d("selectFilter", "remove ${filter.property}")
            newList.remove(filter)
            _uiState.value.filterMap[filter.property] = false
            fetchRestaurants(uiState.value.lowerLeft, uiState.value.topRight, newList, 0.0,0.0)
        }
        _uiState.update { it.copy(filterList = newList) }
    }

    private fun saveInCollection(collectionId: String, restaurantId: String) {
        viewModelScope.launch {
            Log.e(
                "in fetchRestaurants", ""
            )
            repository.addItemInCollection("session_id=5142cece-b22e-4a4f-adf9-990949d053ff", collectionId, restaurantId).collect { state ->
                when (state) {
                    is NetworkState.Failure -> {
                        Log.d("NetworkException", "NetworkFailure")

                        _uiState.update {
                            it.copy(
                                errorMessage = state.cause.message,
                                isLoading = false,
                            )
                        }
                    }

                    is NetworkState.Success -> {
                        Log.d("NetworkSuccess", "")
                        _uiState.update {
                            it.copy(
                                isLoading = false,
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


        Log.d("AddItem", "${collectionId} ${restaurantId}")
    }

    fun filterNonOverlappingRestaurants(
        restaurants: List<Restaurant>,
        rectWidth: Double,
        rectHeight: Double
    ): List<Restaurant> {

        val nonOverlappingRestaurants = mutableListOf<Restaurant>()

        for (restaurant in restaurants) {
            val restaurantRect = createRectangle(restaurant.coordinates, rectWidth, rectHeight)


            var isOverlapping = false
            for (filteredRestaurant in nonOverlappingRestaurants) {
                val filteredRestaurantRect = createRectangle(filteredRestaurant.coordinates, rectWidth, rectHeight)

                if (rectanglesOverlap(restaurantRect, filteredRestaurantRect)) {
                    isOverlapping = true
                    break
                }
            }

            if (!isOverlapping) {
                nonOverlappingRestaurants.add(restaurant)
            }
        }

        return nonOverlappingRestaurants
    }

    fun createRectangle(center: Point, width: Double, height: Double): Rect {
        val halfWidth = width / 2
        val halfHeight = height / 2
        return Rect(
            bottomLeft = Point(center.latitude - halfHeight, center.longitude - halfWidth),
            topRight = Point(center.latitude + halfHeight, center.longitude + halfWidth)
        )
    }

    fun rectanglesOverlap(rect1: Rect, rect2: Rect): Boolean {
        Log.d("rectanglesOverlap", "rect1.topRight.latitude = ${rect1.topRight.latitude}, rect2.bottomLeft.latitude = ${rect2.bottomLeft.latitude }")
        Log.d("rectanglesOverlap", "rect1.bottomLeft.latitude = ${rect1.bottomLeft.latitude}, rect2.topRight.latitude = ${rect2.topRight.latitude }")
        Log.d("rectanglesOverlap", "rect1.topRight.longitude = ${rect1.topRight.longitude}, rect2.bottomLeft.longitude = ${rect2.bottomLeft.longitude }")
        Log.d("rectanglesOverlap", " rect1.bottomLeft.longitude = ${ rect1.bottomLeft.longitude}, rect2.topRight.longitude = ${rect2.topRight.longitude }")
        Log.d("rectanglesOverlap", " rect1.bottomLeft.longitude = ${ (rect1.topRight.latitude < rect2.bottomLeft.latitude || rect1.bottomLeft.latitude > rect2.topRight.latitude ||  rect1.topRight.longitude < rect2.bottomLeft.longitude ||  rect1.bottomLeft.longitude > rect2.topRight.longitude)}")
        return !(rect1.topRight.latitude < rect2.bottomLeft.latitude || // rect1 ниже rect2
                rect1.bottomLeft.latitude > rect2.topRight.latitude || // rect1 выше rect2
                rect1.topRight.longitude < rect2.bottomLeft.longitude || // rect1 левее rect2
                rect1.bottomLeft.longitude > rect2.topRight.longitude)  // rect1 правее rect2
    }

    // Данные прямоугольника
    data class Rect(val bottomLeft: Point, val topRight: Point)

    private fun switchUserMode() {
        if (!uiState.value.isCollectionMode) {
            fetchUserCollections()
            _uiState.update {
                it.copy(
                    isCollectionMode = true,
                )
            }
            fetchRestaurants(uiState.value.lowerLeft, uiState.value.topRight, uiState.value.filterList, 0.0,0.0)
    } else{
            fetchCollections()
            _uiState.update {
                it.copy(
                    isCollectionMode = false,
                )
            }
            fetchRestaurants(uiState.value.lowerLeft, uiState.value.topRight, uiState.value.filterList, 0.0,0.0)
        }
    }

}