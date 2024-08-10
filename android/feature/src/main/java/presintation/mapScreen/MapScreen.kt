package presintation.mapScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.feature.R
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import model.CancelCentering
import model.ChangeDeviceLocation
import model.Event


@Composable
fun MapScreen(
    uiState: MainUiState,
    send: (Event) -> Unit,
    mapView: MapView,
    curLocation: MutableState<Point?>
) {

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            mapView
        },
    )
    { mapView ->

        fun moveToStartLocation(curLocation: Point, zoomValue: Float) {
            mapView.mapWindow.map.move(
                CameraPosition(curLocation, zoomValue, 0.0f, 0.0f)
            )
        }

        /*if(curLocation != uiState.currentDeviceLocation && uiState.currentDeviceLocation != null){
            send(ChangeDeviceLocation(curLocation))
        }*/
        if(curLocation.value != null){
            send(ChangeDeviceLocation(curLocation.value!!))
        }

        if (uiState.centeringIsRequired && curLocation.value != null) {
            moveToStartLocation(curLocation.value!!, uiState.zoomValue)
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

        if(curLocation.value != null){
            mapObjectCollection.addPlacemark().apply {
                geometry = curLocation.value!!
                setIcon(curLocationMarkerImageProvider)
            }
        }
    }
}
