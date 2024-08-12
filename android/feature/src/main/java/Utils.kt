import android.graphics.Bitmap
import android.util.Log
import android.view.View
import com.yandex.mapkit.geometry.Point
import model.Recommendation
import model.Restaurant


object Utils {
    fun createBitmapFromView(view: View): Bitmap {
        Log.d("View", view.toString())
        Log.d("ViewWidth", view.width.toString())
        Log.d("ViewHeight", view.height.toString())

        if (view.width <= 0 || view.height <= 0) {
            throw IllegalArgumentException("Width and height must be greater than 0")
        }
        return Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

    }

    val places = arrayListOf(
        Point(55.736863, 37.596052),
        Point(55.734252, 37.588973),
        Point(55.732005, 37.587676),
        Point(55.731359, 37.589837),
        Point(55.732321, 37.592902),
        Point(55.736012, 37.595277),
        Point(55.730026, 37.589179),
    )

    val recommendations = listOf(
        Recommendation(
            "Собрали для вас",
            "Рекоммендации от наших экспертов"
        ),
        Recommendation(
            "Завтраки вне дома",
            "Куда сходить  Места"
        ),
        Recommendation(
            "Пиво и футбол",
            "Куда сходить  Места"
        ),
        Recommendation(
            "Где перекусить спортсмену",
            "Куда сходить  Места"
        )

    )

    val restaurants = listOf(
        Restaurant(
            id = "",
            coordinates = Point(55.736863, 37.596052),
            name = "Blanc",
            description = "Ресторан авторской кухни, расположенный в исторической части города",
            address = "До 00:00, м. Китай-город, 28мин на машине",
            isApproved = true,
            rating = 4.8,
            priceLowerBound = 1000,
            priceUpperBound = 2500,
            tags = listOf(
                "Европейская", "Коктейли", "Завтрак"
            ),
            isFavorite = false,
            openTime = "",
            closeTime = "",
        ),
        Restaurant(
            id = "",
            coordinates = Point(55.734252, 37.588973),
            name = "Lions Head",
            description = "Классический ирландский паб, который предлагает своим гостям широкий выбор напитков",
            address = "До 00:00, м. Тургеневская, 29мин на машине",
            isApproved = true,
            rating = 4.8,
            priceLowerBound = 1100,
            priceUpperBound = 2600,
            tags = listOf(
                "Европейская", "Коктейли", "Завтрак"
            ),
            isFavorite = false,
            openTime = "",
            closeTime = "",
        ),
        Restaurant(
            id = "",
            coordinates = Point(55.732005, 37.587676),
            name = "Lions Head",
            description = "Классический ирландский паб, который предлагает своим гостям широкий выбор напитков",
            address = "До 00:00, м. Тургеневская, 29мин на машине",
            isApproved = true,
            rating = 4.8,
            priceLowerBound = 1100,
            priceUpperBound = 2600,
            tags = listOf(
                "Европейская", "Коктейли", "Завтрак"
            ),
            isFavorite = false,
            openTime = "",
            closeTime = "",
        ),
        Restaurant(
            id = "",
            coordinates = Point(55.731359, 37.589837),
            name = "Lions Head",
            description = "Классический ирландский паб, который предлагает своим гостям широкий выбор напитков",
            address = "До 00:00, м. Тургеневская, 29мин на машине",
            isApproved = true,
            rating = 4.8,
            priceLowerBound = 1100,
            priceUpperBound = 2600,
            tags = listOf(
                "Европейская", "Коктейли", "Завтрак"
            ),
            isFavorite = false,
            openTime = "",
            closeTime = "",
        ),
        Restaurant(
            id = "",
            coordinates = Point(55.732321, 37.592902),
            name = "Lions Head",
            description = "Классический ирландский паб, который предлагает своим гостям широкий выбор напитков",
            address = "До 00:00, м. Тургеневская, 29мин на машине",
            isApproved = true,
            rating = 4.8,
            priceLowerBound = 1100,
            priceUpperBound = 2600,
            tags = listOf(
                "Европейская", "Коктейли", "Завтрак"
            ),
            isFavorite = false,
            openTime = "",
            closeTime = "",
        ),

        Restaurant(
            id = "",
            coordinates = Point(55.736012, 37.595277),
            name = "Lions Head",
            description = "Классический ирландский паб, который предлагает своим гостям широкий выбор напитков",
            address = "До 00:00, м. Тургеневская, 29мин на машине",
            isApproved = true,
            rating = 4.8,
            priceLowerBound = 1100,
            priceUpperBound = 2600,
            tags = listOf(
                "Европейская", "Коктейли", "Завтрак"
            ),
            isFavorite = false,
            openTime = "",
            closeTime = "",
        ),
        Restaurant(
            id = "",
            coordinates = Point(55.730026, 37.589179),
            name = "Lions Head",
            description = "Классический ирландский паб, который предлагает своим гостям широкий выбор напитков",
            address = "До 00:00, м. Тургеневская, 29мин на машине",
            isApproved = true,
            rating = 4.8,
            priceLowerBound = 1100,
            priceUpperBound = 2600,
            tags = listOf(
                "Европейская", "Коктейли", "Завтрак"
            ),
            isFavorite = false,
            openTime = "",
            closeTime = "",
        ),
    )
}