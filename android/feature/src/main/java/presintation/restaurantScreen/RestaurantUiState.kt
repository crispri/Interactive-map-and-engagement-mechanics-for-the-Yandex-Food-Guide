package presintation.restaurantScreen

import model.Restaurant

/**
 * A data class representing the UI state for the restaurant screen.
 *
 * This class holds all the necessary information to represent the current state of the restaurant screen in the UI.
 * It includes details about the current restaurant, loading state, potential error messages, and a list of items.
 */
data class RestaurantUiState(
    val currentRestaurant: Restaurant? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val itemsList: List<String> = listOf(
        "Музыка громче",
        "Завтрак",
        "Винотека",
        "Европейская",
        "Коктели",
        "Можно с собакой",
        "Веранда"
    ),
)
