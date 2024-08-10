package presintation.restaurantScreen

import com.yandex.mapkit.geometry.Point
import model.Recommendation
import model.Restaurant

data class RestaurantUiState(
    val currentRestaurant: Restaurant? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val itemsList: List<String> = listOf(
        "Музыка громче",
        "Завтраки",
        "Винотека",
        "Европейская",
        "Коктели",
        "Можно с собакой",
        "Веранда"
    ),


    )