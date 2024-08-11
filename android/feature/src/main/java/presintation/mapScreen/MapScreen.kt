package presintation.mapScreen

import Utils.createBitmapFromVector
import Utils.createBitmapFromView
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.feature.R
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.launch
import model.CancelCentering
import model.ChangeDeviceLocation
import ui.createViewNormalPinCard
import model.MainScreenEvent
import model.UpdateItemsOnMap


@Composable
fun MapScreen(
    uiState: MainUiState,
    send: (MainScreenEvent) -> Unit,
    mapView: CustomMapView,
    curLocation: MutableState<Point?>
) {
    val context = LocalContext.current
    val mapObjectCollection = remember { mapView.mapWindow.map.mapObjects }
    val restaurantMarker =
        remember { createBitmapFromVector(R.drawable.restaurant_marker, context = mapView.context) }
    val restaurantMarkerImageProvider = remember { ImageProvider.fromBitmap(restaurantMarker) }

    val curLocationMarker = remember {
        createBitmapFromVector(R.drawable.current_location_marker_svg, context = mapView.context)
    }

    val curLocationMarkerImageProvider = remember { ImageProvider.fromBitmap(curLocationMarker) }

    LaunchedEffect(Unit) {
        val cameraListener = CameraListener { p0, p1, p2, p3 ->
            if (p3) {
                val topRightPoint = mapView.mapWindow.map.visibleRegion.topRight
                val bottomLeftPoint = mapView.mapWindow.map.visibleRegion.bottomLeft
                send(UpdateItemsOnMap(bottomLeftPoint, topRightPoint))
                Log.d("CameraListener", "Top right: $topRightPoint, Bottom left: $bottomLeftPoint")
            }
        }
        mapView.addCameraListener(cameraListener)

        mapObjectCollection.addPlacemark().apply {
            //geometry = Point(55.733415, 37.590042)
            geometry = curLocation.value ?: Point(55.733415, 37.590042)
            setIcon(curLocationMarkerImageProvider)
        }
    }

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

        val mapKit = MapKitFactory.getInstance()

        mapObjectCollection.clear()
        Log.e("mapObjectCollection", "mapObjectCollection.clear()")
        uiState.restaurantsOnMap.forEach {
            mapObjectCollection.addPlacemark().apply {
                geometry = it.coordinates
                setIcon(restaurantMarkerImageProvider)
            }
        }
        mapObjectCollection.addPlacemark().apply {
            //geometry = Point(55.733415, 37.590042)
            geometry = curLocation.value ?: Point(55.733415, 37.590042)
            setIcon(curLocationMarkerImageProvider)
        }
    }
}
