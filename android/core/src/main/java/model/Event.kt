package model

import com.yandex.mapkit.geometry.Point

abstract class Event {}

class SaveInCollectionEvent(
    val restaurantId: String
) : Event()

class NavigateToLocationEvent : Event()

class CancelCentering : Event()

class ChangeDeviceLocation(val curLocation: Point) : Event()

