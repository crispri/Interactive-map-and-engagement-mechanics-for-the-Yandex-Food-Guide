package presintation.mapScreen

import com.yandex.mapkit.geometry.Point
import model.CollectionOfPlace
import model.Filter
import model.Pins
import model.Restaurant


/**
 * Represents the UI state for the main screen of the application.
 */
data class MainUiState(
    val selectedItemFromMapId: String? = null,
    val selectedItemFromBottomSheetId: String? = null,

    val curCoordinates: Pair<Point, Point>? = null,
    val raiseRequired: Boolean = false,

    val currentAddress: String = "Льва Толстого, 16",
    val restaurantsOnMap: List<Restaurant> = listOf(),
    val restaurantsOnMapUnsorted: List<Restaurant> = listOf(),

    val converterPins: MutableMap<Pins, Int> = mutableMapOf(
        Pins.MAXI to 0,
        Pins.NORMAL to 0,
        Pins.MINI to 0
    ),

    val recommendations: List<CollectionOfPlace> = listOf(),
    val recommendationIsSelected: Boolean = false,
    val zoomValue: Float = 16.0f,
    val centeringIsRequired: Boolean = true,
    val filterMap: HashMap<String, Boolean> = hashMapOf(),

    val lowerLeft: Point = Point(55.0, 37.0),
    val topRight: Point = Point(56.0, 38.0),
    val filterList: MutableList<Filter> = mutableListOf(),

    val listOfRestaurant: List<Restaurant> = listOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val isCollectionMode: Boolean = false,

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