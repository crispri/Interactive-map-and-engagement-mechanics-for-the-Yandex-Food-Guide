package ui

/**
 * This file contains classes related to events for the restaurant screen.
 *
 * It includes:
 * - [RestaurantScreenEvent]: An abstract base class for different types of events that can occur
 *   in the restaurant screen.
 * - [GetRestaurantInfo]: A specific event that represents the action of retrieving information
 *   about a particular restaurant based on its unique identifier.
 *
 * These classes are used to define and handle events within the restaurant-related features of the application.
 */
abstract class RestaurantScreenEvent {}

class GetRestaurantInfo(
    val restaurantId: String?
) : RestaurantScreenEvent()