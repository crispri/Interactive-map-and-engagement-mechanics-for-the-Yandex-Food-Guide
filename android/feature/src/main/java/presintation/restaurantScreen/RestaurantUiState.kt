package presintation.restaurantScreen

import model.Restaurant

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
