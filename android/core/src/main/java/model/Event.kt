package model

import com.yandex.mapkit.geometry.Point

abstract class Event {}

class ChangeDeviceLocation(val curLocation: Point) : Event()


