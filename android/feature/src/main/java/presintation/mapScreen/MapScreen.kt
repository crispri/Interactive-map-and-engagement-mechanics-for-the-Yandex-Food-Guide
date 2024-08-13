package presintation.mapScreen

import Utils.createBitmapFromVector
import Utils.createBitmapFromView
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.feature.R
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider
import model.CancelCentering
import model.MainScreenEvent
import model.UpdateItemsOnMap


@Composable
fun MapScreen(
    uiState: MainUiState,
    send: (MainScreenEvent) -> Unit,
    mapView: CustomMapView,
    curLocation: MutableState<Point?>
) {
    val mapObjectCollection = remember { mapView.mapWindow.map.mapObjects }

    //Mini
    val restaurantMarkerMini =
        remember { createBitmapFromVector(com.example.core.R.drawable.ic_mini_pin, context = mapView.context) }
    val restaurantMarkerImageProviderMini = remember { ImageProvider.fromBitmap(restaurantMarkerMini) }

    //Normal
    val restaurantMarkerNormal =
        remember { createBitmapFromVector(R.drawable.restaurant_marker, context = mapView.context) }
    val restaurantMarkerImageProviderNormal = remember { ImageProvider.fromBitmap(restaurantMarkerNormal) }

    //Maxi
    val restaurantMarkerMaxi =
        remember { createBitmapFromView(mapView.context) }
    val restaurantMarkerImageProviderMaxi = remember { ImageProvider.fromBitmap(restaurantMarkerMaxi) }

    // curLocation
    val curLocationMarker = remember {
        createBitmapFromVector(R.drawable.current_location_marker_svg, context = mapView.context)
    }
    val curLocationMarkerImageProvider = remember { ImageProvider.fromBitmap(curLocationMarker) }

    val cameraCallback = remember{ Map.CameraCallback {} }

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
                CameraPosition(curLocation, zoomValue, 0.0f, 0.0f),
                Animation(Animation.Type.LINEAR, 0.5f),
                cameraCallback
            )
        }

        /*if(curLocation != uiState.currentDeviceLocation && uiState.currentDeviceLocation != null){
            send(ChangeDeviceLocation(curLocation))
        }*/


        /*if(curLocation.value != null){
            send(ChangeDeviceLocation(curLocation.value!!))
        }*/

        if (uiState.centeringIsRequired && curLocation.value != null) {
            moveToStartLocation(curLocation.value!!, uiState.zoomValue)
            send(CancelCentering())
        }

        val mapKit = MapKitFactory.getInstance()

        mapObjectCollection.clear()

        /*uiState.restaurantsOnMap.forEach {
            mapObjectCollection.addPlacemark().apply {
                geometry = it.coordinates
                setIcon(restaurantMarkerImageProvider)
            }
        }*/

        uiState.restaurantsOnMap.reversed().forEachIndexed { index, restaurant ->
            Log.e("uiState.restaurantsOnMap.reversed().forEachIndexed", "index = $index")
            val currentPin =
                if ( index < uiState.restaurantsOnMap.size - 8) {
                    Log.e("uiState.restaurantsOnMap.reversed().forEachIndexed", "Mini")
                    restaurantMarkerImageProviderMini
                } else
                    if (index < uiState.restaurantsOnMap.size - 4) {
                        Log.e("uiState.restaurantsOnMap.reversed().forEachIndexed", "Normal")
                        restaurantMarkerImageProviderNormal
                    } else {
                        Log.e("uiState.restaurantsOnMap.reversed().forEachIndexed", "Maxi")
                        restaurantMarkerImageProviderMaxi
                    }

            mapObjectCollection.addPlacemark().apply {
                geometry = restaurant.coordinates
                setIcon(currentPin)
            }
        }


        mapObjectCollection.addPlacemark().apply {
            geometry = curLocation.value ?: Point(55.733415, 37.590042)
            setIcon(curLocationMarkerImageProvider)
        }
    }
}
