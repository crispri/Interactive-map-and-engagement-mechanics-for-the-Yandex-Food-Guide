package model

import com.yandex.mapkit.geometry.Point

abstract class MainScreenEvent {}

class SaveInCollectionEvent(
    val restaurantId: String
) : MainScreenEvent()

class SelectItem(
    val itemId: String
) : MainScreenEvent()

class NavigateToLocationEvent : MainScreenEvent()

class CancelCentering : MainScreenEvent()

class UpdateItemsOnMap(
    val lowerLeft: Point,
    val topRight: Point,
    val filterList: List<Filter>,
) : MainScreenEvent()