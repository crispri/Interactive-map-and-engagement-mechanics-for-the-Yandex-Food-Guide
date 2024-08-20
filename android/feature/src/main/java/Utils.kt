import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.view.View
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import com.yandex.mapkit.geometry.Point
import com.yandex.runtime.image.ImageProvider
import model.CollectionOfPlace
import model.Restaurant
import pins.CustomPinView
import pins.CustomPinViewSelected
import java.text.DecimalFormat
import kotlin.math.cos
import kotlin.math.pow


object Utils {

    fun updateOverlayHeight(zoomLevel: Int, viewHeightInPx: Int, bl: Point, tr: Point) : Double {
        val latitude = (tr.latitude + bl.latitude) / 2
        val metersPerPixel = (40075016.686 / (256 * 2.0.pow(zoomLevel))) * cos(latitude * (Math.PI / 180))
        val heightInMeters = viewHeightInPx * metersPerPixel
        val metersPerDegree = 111320.0
        val latitudeDelta = heightInMeters / metersPerDegree
        return latitudeDelta

    }

    fun updateOverlayWidth(zoomLevel: Int, viewWidthInPx: Int, bl: Point, tr: Point): Double {
        val latitude = (tr.latitude + bl.latitude) / 2
        val metersPerPixel = (40075016.686 / (256 * 2.0.pow(zoomLevel))) * cos(latitude * (Math.PI / 180))
        val widthInMeters = viewWidthInPx * metersPerPixel
        val metersPerDegreeLongitude = 40075016.686 / 360.0 * cos(latitude * (Math.PI / 180))
        val longitudeDelta = widthInMeters / metersPerDegreeLongitude
        return longitudeDelta
    }

    fun createBitmapFromView(
        view: View,
        shadowColor: Int,
        shadowRadius: Float,
        dx: Float,
        dy: Float
    ): Bitmap {
        // Создаем bitmap из view
        val originalBitmap = createBitmapFromViewForShadow(view)

        // Создаем bitmap с добавлением тени
        val shadowBitmap = Bitmap.createBitmap(
            originalBitmap.width + (shadowRadius * 2).toInt(),
            originalBitmap.height + (shadowRadius * 2).toInt(),
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(shadowBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Добавляем тень
        paint.setShadowLayer(shadowRadius, dx, dy, shadowColor)

        // Рисуем элемент, у которого будет тень
        canvas.drawRoundRect(
            0f, 0f,
            originalBitmap.width.toFloat(),
            originalBitmap.height.dp.value - 25.dp.value,
            40.dp.value,
            40.dp.value,
            paint,
        )

        // Рисуем исходный bitmap поверх тени
        canvas.drawBitmap(originalBitmap, 0f, 0f, null)

        return shadowBitmap
    }

    private fun createBitmapFromViewForShadow(view: View): Bitmap {
        view.forceLayout()
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        return view.drawToBitmap()
    }

//    fun createBitmapFromView(
//        view: View,
//        shadowColor: Int,
//        shadowRadius: Float,
//        dx: Float,
//        dy: Float
//    ): Bitmap {
//        // Создаем bitmap из view
//        val originalBitmap = createBitmapFromViewForShadow(view)
//
//        // Создаем bitmap с добавлением тени
//        val shadowBitmap = Bitmap.createBitmap(
//            originalBitmap.width + (shadowRadius * 2).toInt(),
//            originalBitmap.height + (shadowRadius * 2).toInt(),
//            Bitmap.Config.ARGB_8888
//        )
//
//        val canvas = Canvas(shadowBitmap)
//        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//
//        // Добавляем тень
//        paint.setShadowLayer(shadowRadius, dx, dy, shadowColor)
//
//        // Рисуем элемент, у которого будет тень
//        canvas.drawRoundRect(
//            0f, 0f,
//            originalBitmap.width.toFloat(),
//            originalBitmap.height.dp.value - 21.dp.value,
//            40.dp.value,
//            40.dp.value,
//            paint,
//        )
//
//        // Рисуем исходный bitmap поверх тени
//        canvas.drawBitmap(originalBitmap, 0f, 0f, null)
//
//        return shadowBitmap
//    }
//
//    private fun createBitmapFromViewForShadow(view: View): Bitmap {
//        view.forceLayout()
//        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
//        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
//
//        return view.drawToBitmap()
//    }


    fun createBitmapFromVector(art: Int, context: Context): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, art) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }


    val recommendations = listOf(
        CollectionOfPlace(
            "",
            "Собрали для вас",
            "Рекоммендации от наших экспертов",
            1,
            "https://storage.yandexcloud.net/yandex-guide/guides/adc42f8a-683d-430e-82fe-18e7ae6139ef.jpg",
            "https://openkitchen.eda.yandex/article/places/guides/chem-zanyatsya-v-vykhodnye-3-4-avgusta-v-moskve",
        ),
        CollectionOfPlace(
            "",
            "Завтраки вне дома",
            "Куда сходить  Места",
            0,
            "https://storage.yandexcloud.net/yandex-guide/guides/adc42f8a-683d-430e-82fe-18e7ae6139ef.jpg",
            "https://openkitchen.eda.yandex/article/places/guides/chem-zanyatsya-v-vykhodnye-3-4-avgusta-v-moskve",
        ),
        CollectionOfPlace(
            "",
            "Пиво и футбол",
            "Куда сходить  Места",
            1,
            "https://storage.yandexcloud.net/yandex-guide/guides/adc42f8a-683d-430e-82fe-18e7ae6139ef.jpg",
            "https://openkitchen.eda.yandex/article/places/guides/chem-zanyatsya-v-vykhodnye-3-4-avgusta-v-moskve",
        ),
        CollectionOfPlace(
            "",
            "Где перекусить спортсмену",
            "Куда сходить  Места",
            0,
            "https://storage.yandexcloud.net/yandex-guide/guides/adc42f8a-683d-430e-82fe-18e7ae6139ef.jpg",
            "https://openkitchen.eda.yandex/article/places/guides/chem-zanyatsya-v-vykhodnye-3-4-avgusta-v-moskve",
        )

    )

    val restaurants = listOf(
        Restaurant(
            id = "66668db7-be95-4dd0-a010-37633a756b1e",
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
            inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg"),
            score = 20,
            additionalInfo = "1000-2500",
        ),
        Restaurant(
            id = "75d07f8a-a2e8-4557-b879-bfc585738ec6",
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
            closeTime = "", inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg"),
            score = 40,
            additionalInfo = "1000-2500",
        ),
        Restaurant(
            id = "f1483fdb-c304-41bc-8392-c33b218c533e",
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
            closeTime = "", inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg"),
            score = 30,
            additionalInfo = "1000-2500",

            ),
        Restaurant(
            id = "d853dcd7-1394-4ac3-bc67-16361a853ab0",
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
            closeTime = "", inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg"),
            score = 50,
            additionalInfo = "1000-2500",

            ),
        Restaurant(
            id = "77903d89-9490-40f6-be6a-97dbf5d6368c",
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
            closeTime = "", inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg"),
            score = 60,
            additionalInfo = "1000-2500",
        ),

        Restaurant(
            id = "0a5605a0-cf38-4bff-8678-2c866d52f7b3",
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
            closeTime = "", inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg"),
            score = 23,
            additionalInfo = "1000-2500",
        ),
        Restaurant(
            id = "2fb96cda-96ee-4e53-b9aa-6d5c379cd620",
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
            closeTime = "", inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg"),
            score = 45,
            additionalInfo = "1000-2500",
        ),
    )

    fun invertColors(bitmap: Bitmap): Bitmap {
        val invertedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(invertedBitmap)
        val paint = Paint()

        val colorMatrix = ColorMatrix()
        colorMatrix.set(
            floatArrayOf(
                -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
                0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
                0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            )
        )
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return invertedBitmap
    }
}