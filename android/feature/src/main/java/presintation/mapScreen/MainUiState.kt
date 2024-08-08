package presintation.mapScreen

import android.graphics.Bitmap
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import model.Recommendation
import model.Restaurant

data class MainUiState(
    val currentDeviceLocation: Point = Point(55.733415, 37.590042),
    val currentAddress: String = "Льва Толстого, 16",
    val restaurantsOnMap: List<Restaurant> = listOf(),
    val recommendations: List<Recommendation> = listOf(),
    val zoomValue: Float = 16.5f,
    val centeringIsRequired: Boolean = true,

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