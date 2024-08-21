package ui

abstract class RestaurantScreenEvent {}

class GetRestaurantInfo(
    val restaurantId: String?
) : RestaurantScreenEvent()