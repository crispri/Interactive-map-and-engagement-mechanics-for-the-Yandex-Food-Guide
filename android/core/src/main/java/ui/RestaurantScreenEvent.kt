package ui

import model.MainScreenEvent

abstract class RestaurantScreenEvent {}

class GetRestaurantInfo(
    val restaurantId: String?
) : RestaurantScreenEvent()