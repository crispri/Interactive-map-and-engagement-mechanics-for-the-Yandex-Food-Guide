package model

abstract class Event {}

class SaveInCollectionEvent(
    val restaurantId: String
) : Event()