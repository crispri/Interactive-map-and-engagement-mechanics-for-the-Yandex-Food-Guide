package presintation.mapScreen

import Utils.createBitmapFromView
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.feature.R
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import model.CancelCentering
import model.Event
import ui.createViewNormalPinCard


@Composable
fun MapScreen(
    uiState: MainUiState,
    send: (Event) -> Unit,
    mapView: MapView
) {
    val context = LocalContext.current
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            mapView
        },
    )
    { mapView ->

        fun moveToStartLocation(startLocation: Point, zoomValue: Float) {
            mapView.mapWindow.map.move(
                CameraPosition(startLocation, zoomValue, 0.0f, 0.0f)
            )
        }

        if (uiState.centeringIsRequired) {
            moveToStartLocation(uiState.currentDeviceLocation, uiState.zoomValue)
            send(CancelCentering())
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

        val mapObjectCollection = mapView.mapWindow.map.mapObjects

//         Изначальные Дашины пины
//        val restaurantMarker = createBitmapFromVector(
//            R.drawable.restaurant_marker,
//            context = mapView.context
//        )
//        val restaurantMarkerImageProvider = ImageProvider.fromBitmap(restaurantMarker)

        val restaurantMarkerMini = createBitmapFromVector(
            com.example.core.R.drawable.ic_mini_pin,
            context = mapView.context
        )
        val restaurantMarkerImageProviderMini = ImageProvider.fromBitmap(restaurantMarkerMini)

//       val restaurantMarkerNormal = createBitmapFromView(createViewNormalPinCard(context))

//        val restaurantMarkerImageProviderNormal = ImageProvider.fromBitmap(restaurantMarkerNormal)
//
//        val restaurantMarkerMax = createBitmapFromView(createViewSuperPinCard(context))
//        val restaurantMarkerImageProviderMax = ImageProvider.fromBitmap(restaurantMarkerMax)

        val curLocationMarker = createBitmapFromVector(
            R.drawable.current_location_marker_svg,
            context = mapView.context
        )
        val curLocationMarkerImageProvider = ImageProvider.fromBitmap(curLocationMarker)

        uiState.restaurantsOnMap.forEachIndexed { index, restaurant ->
            val currentPin =
                if (index < 3) {
                    // Показываются большие пины - restaurantMarkerImageProviderMax
                    restaurantMarkerImageProviderMini
                } else
                    if (index < 7) {
                        // Показываются средние пины - restaurantMarkerImageProviderNormal
                        restaurantMarkerImageProviderMini
                    } else {
                        // Показываются средние пины - restaurantMarkerImageProviderMini
                        restaurantMarkerImageProviderMini
                    }

            mapObjectCollection.addPlacemark().apply {
                geometry = restaurant.coordinates
                setIcon(currentPin)
            }
        }

        mapObjectCollection.addPlacemark().apply {
            geometry = Point(55.733415, 37.590042)
            setIcon(curLocationMarkerImageProvider)
        }
    }
}

