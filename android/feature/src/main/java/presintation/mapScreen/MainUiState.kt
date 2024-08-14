package presintation.mapScreen

import com.yandex.mapkit.geometry.Point
import model.Recommendation
import model.Restaurant


//все отображаемые элементы
data class MainUiState(
    val selectedItemId: String? = null,
    val currentAddress: String = "Льва Толстого, 16",
    val restaurantsOnMap: List<Restaurant> = listOf(),
    val recommendations: List<Recommendation> = listOf(),
    val zoomValue: Float = 16.0f,
    val centeringIsRequired: Boolean = true,

    val lowerLeft: Point = Point(55.0, 37.0),
    val topRight: Point = Point(56.0, 38.0),

    val listOfRestaurant: List<Restaurant> = listOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    /*val mapObjectCollection: MapObjectCollection? = null,
    val placemarkMapObject: PlacemarkMapObject? = null,*/


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