package model

import com.yandex.mapkit.geometry.Point

abstract class MainScreenEvent {}

class SaveInCollectionEvent(
    val restaurantId: String
) : MainScreenEvent()

class SelectItemFromMap(
    val itemId: String?
) : MainScreenEvent()


class SelectItemFromBottomSheet(
    val itemId: String?
) : MainScreenEvent()

class RecommendationIsSelected(
    val isSelected: Boolean
) : MainScreenEvent()

class NavigateToLocationEvent : MainScreenEvent()

class CancelCentering : MainScreenEvent()

class UpdateItemsOnMap(
    val lowerLeft: Point,
    val topRight: Point,
    val filterList: List<Filter>,
) : MainScreenEvent()

class RaiseCameraPosition(
    val raiseRequired: Boolean
) : MainScreenEvent()

class SelectFilter(val isAdding: Boolean, val filter: Filter) : MainScreenEvent()

class UpdateListOfRestaurant(
    val listOfRestaurant: List<Restaurant>,
) : MainScreenEvent()
