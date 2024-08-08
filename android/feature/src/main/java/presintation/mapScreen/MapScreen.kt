package presintation.mapScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.feature.R
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

@Composable
fun MapScreen(uiState: MainUiState) {

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            MapView(context)
        },
    )
    { mapView ->
        fun moveToStartLocation(startLocation: Point, zoomValue: Float) {
            mapView.mapWindow.map.move(
                CameraPosition(startLocation, zoomValue, 0.0f, 0.0f)
            )
        }

        moveToStartLocation(Point(55.733415, 37.590042), 14.0f)

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

        val mapObjectCollection = mapView.mapWindow.map.mapObjects

        val restaurantMarker = createBitmapFromVector(
            R.drawable.restaurant_marker,
            context = mapView.context
        )
        val restaurantMarkerImageProvider = ImageProvider.fromBitmap(restaurantMarker)

        val curLocationMarker = createBitmapFromVector(
            R.drawable.current_location_marker_svg,
            context = mapView.context
        )
        val curLocationMarkerImageProvider = ImageProvider.fromBitmap(curLocationMarker)

        uiState.restaurantsOnMap.forEach {
            mapObjectCollection.addPlacemark().apply {
                geometry = it.coordinates
                setIcon(restaurantMarkerImageProvider)
            }
        }

        mapObjectCollection.addPlacemark().apply {
            geometry = Point(55.733415, 37.590042)
            setIcon(curLocationMarkerImageProvider)
        }
    }
}




