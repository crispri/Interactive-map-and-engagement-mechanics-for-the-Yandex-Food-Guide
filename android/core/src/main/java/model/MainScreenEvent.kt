package model


//новый файл
//смена ui state экрана

class SaveInCollectionEvent(
    val restaurantId: String
) : Event()

class NavigateToLocationEvent : Event()

class CancelCentering : Event()