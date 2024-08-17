import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Picture
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import com.example.feature.R
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import model.CollectionOfPlace
import model.Restaurant
import pins.CustomPinView
import pins.CustomPinViewSelected
import pins.NormalPinView
import ui.SuperPinCard


object Utils {

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
            originalBitmap.height.dp.value - 21.dp.value,
            40.dp.value,
            40.dp.value,
            paint,
        )

        // Рисуем исходный bitmap поверх тени
        canvas.drawBitmap(originalBitmap, 0f, 0f, null)

        return shadowBitmap
    }

    fun createSuperPin(
        context: Context,
        tittle: String = "Хороший бар",
        raiting: Double = 4.9,
        description: String = "кофе от 300Р"
    ): View {
        val pinView = CustomPinView(context = context)
        pinView.setTitle(tittle)
        pinView.setRating(raiting.toString())
        pinView.setDescription(description)

        return pinView
    }

    fun createSuperSelectedPin(
        context: Context,
        tittle: String = "Хороший бар",
        raiting: Double = 4.9,
        description: String = "кофе от 300Р"
    ): View {
        val pinView = CustomPinViewSelected(context = context)
        pinView.setTitle(tittle)
        pinView.setRating(raiting.toString())
        pinView.setDescription(description)

        return pinView
    }

    fun createNormalPin(
        context: Context,
        tittle: String = "Нормальный бар",
        raiting: Double = 4.8,
    ): View {
        val pinView = NormalPinView(context = context)
        pinView.setTitle(tittle)
        pinView.setRating(raiting.toString())
        return pinView
    }

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

    private fun createBitmapFromViewForShadow(view: View): Bitmap {
        view.forceLayout()
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        return view.drawToBitmap()
    }


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
        ),
        CollectionOfPlace(
            "",
            "Завтраки вне дома",
            "Куда сходить  Места",
            0
        ),
        CollectionOfPlace(
            "",
            "Пиво и футбол",
            "Куда сходить  Места",
            1,
        ),
        CollectionOfPlace(
            "",
            "Где перекусить спортсмену",
            "Куда сходить  Места",
            0
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
            inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg")
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
            closeTime = "", inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg")

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
            closeTime = "", inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg")
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
            closeTime = "", inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg")

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
            closeTime = "", inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg")

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
            closeTime = "", inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg")

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
            closeTime = "", inCollection = false,
            pin = "https://storage.yandexcloud.net/yandex-guide/restaurants/a4279cc6-24a0-4b36-b65c-016868e9fda2.jpg",
            pictures = listOf("https://storage.yandexcloud.net/yandex-guide/restaurants/interior/i_8.jpg")

        ),
        )

}