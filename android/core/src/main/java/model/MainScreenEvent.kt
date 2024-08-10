package model

abstract class MainScreenEvent {}

class SaveInCollectionEvent(
    val restaurantId: String
) : MainScreenEvent()

class NavigateToLocationEvent : MainScreenEvent()

class CancelCentering : MainScreenEvent()
