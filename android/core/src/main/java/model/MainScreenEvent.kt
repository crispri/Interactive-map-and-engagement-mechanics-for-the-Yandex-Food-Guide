package model

import com.yandex.mapkit.geometry.Point


/**
 * This file defines a series of events related to the main screen of the application.
 * These events are used to trigger specific actions or updates in the application's UI or data.
 *
 * The events cover a range of functionalities including:
 * - Saving a restaurant to a collection.
 * - Selecting items from a map or a bottom sheet.
 * - Updating the list of restaurants and filters.
 * - Navigating to a location on the map.
 * - Adjusting the camera position.
 * - Managing user interactions such as centering the map or switching user modes.
 *
 * The events are represented as classes that inherit from the abstract base class `MainScreenEvent`.
 */
abstract class MainScreenEvent {}

class SaveInCollectionEvent(
    val collectionId: String,
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
    val w: Double,
    val h: Double
) : MainScreenEvent()

class RaiseCameraPosition(
    val raiseRequired: Boolean
) : MainScreenEvent()

class SetNewList(
    val restaurants: List<Restaurant>,
) : MainScreenEvent()


class SelectFilter(val isAdding: Boolean, val filter: Filter) : MainScreenEvent()

class UpdateListOfRestaurant(
    val listOfRestaurant: List<Restaurant>,
) : MainScreenEvent()

class HideIntersections(
    val list: List<Restaurant>,
    val w: Double,
    val h: Double
) : MainScreenEvent()

class SwitchUserModeEvent : MainScreenEvent()
