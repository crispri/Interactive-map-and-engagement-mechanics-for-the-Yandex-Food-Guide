import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.view.View
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import com.yandex.mapkit.geometry.Point
import model.CollectionOfPlace
import model.Restaurant


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
            id = "1",
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
        ),
        Restaurant(
            id = "2",
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
        ),
        Restaurant(
            id = "3",
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

            ),
        Restaurant(
            id = "4",
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
        ),
        Restaurant(
            id = "5",
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

        ),
        Restaurant(
            id = "6",
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
        ),
        Restaurant(
            id = "7",
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
        ),
        Restaurant(
            id = "8",
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