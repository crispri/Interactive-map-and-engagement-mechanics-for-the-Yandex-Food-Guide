package presintation.mapScreen

import Utils.createBitmapFromVector
import Utils.createBitmapFromView
import Utils.createNormalPin
import Utils.createSuperPin
import Utils.createSuperSelectedPin
import Utils.invertColors
import android.graphics.Bitmap
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
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider
import model.CancelCentering
import model.MainScreenEvent
import model.UpdateItemsOnMap
import pins.CustomPinView


@Composable
fun MapScreen(
    uiState: MainUiState,
    send: (MainScreenEvent) -> Unit,
    mapView: CustomMapView,
    curLocation: MutableState<Point?>
) {
    val mapObjectCollection = remember { mapView.mapWindow.map.mapObjects }

    //Mini - general
    val restaurantMarkerMini = remember { createBitmapFromVector(com.example.core.R.drawable.ic_mini_pin, context = mapView.context) }
    val restaurantMarkerImageProviderMini = remember { ImageProvider.fromBitmap(restaurantMarkerMini) }


    //Mini - Selected
    var invertedBitmap = remember { restaurantMarkerMini }
    if(restaurantMarkerMini != null) invertedBitmap = invertColors(restaurantMarkerMini)
    val restaurantMarkerImageProviderMiniSelected = remember { ImageProvider.fromBitmap(invertedBitmap) }


    //Normal - general
    //val restaurantMarkerNormal = remember { createBitmapFromVector(R.drawable.restaurant_marker, context = mapView.context) }
    val normalView = remember { createNormalPin(mapView.context) }
    val restaurantMarkerNormal = remember { createBitmapFromView(normalView) }
    val restaurantMarkerImageProviderNormal = remember { ImageProvider.fromBitmap(restaurantMarkerNormal) }


    //Normal - Selected
    val invertedBitmapNormal = invertColors(restaurantMarkerNormal)
    val restaurantMarkerImageProviderNormalSelected = remember { ImageProvider.fromBitmap(invertedBitmapNormal) }


    //Maxi - general
    val superView = remember { createSuperPin(mapView.context) }
    val restaurantMarkerMaxi = remember { createBitmapFromView(superView) }
    val restaurantMarkerImageProviderMaxi = remember { ImageProvider.fromBitmap(restaurantMarkerMaxi) }


    //Maxi - Selected
    val superViewSelected = remember { createSuperSelectedPin(mapView.context) }
    val restaurantMarkerMaxiSelected = remember { createBitmapFromView(superViewSelected) }
    val restaurantMarkerImageProviderMaxiSelected = remember { ImageProvider.fromBitmap(restaurantMarkerMaxiSelected) }


    // curLocation
    val curLocationMarker = remember {
        createBitmapFromVector(R.drawable.current_location_marker_svg, context = mapView.context)
    }
    val curLocationMarkerImageProvider = remember { ImageProvider.fromBitmap(curLocationMarker) }

    val cameraCallback = remember{ Map.CameraCallback {} }

    LaunchedEffect(Unit) {
        val cameraListener = CameraListener { _, _, _, actionIsFinished ->
            if (actionIsFinished) {
                val topRightPoint = mapView.mapWindow.map.visibleRegion.topRight
                val bottomLeftPoint = mapView.mapWindow.map.visibleRegion.bottomLeft
                send(UpdateItemsOnMap(bottomLeftPoint, topRightPoint))
                Log.d("CameraListener", "Top right: $topRightPoint, Bottom left: $bottomLeftPoint")
            }
        }
        mapView.addCameraListener(cameraListener)

        mapObjectCollection.addPlacemark().apply {
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

        if (uiState.centeringIsRequired && curLocation.value != null) {
            moveToStartLocation(curLocation.value!!, uiState.zoomValue)
            send(CancelCentering())
        }

        mapObjectCollection.clear()

        uiState.restaurantsOnMap.reversed().forEachIndexed { index, restaurant ->
            val currentPin =
                if ( index < uiState.restaurantsOnMap.size - 8) {
                    //restaurantMarkerImageProviderMini
                    if(uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemId){
                        restaurantMarkerImageProviderMiniSelected
                    } else {
                        restaurantMarkerImageProviderMini
                    }
                } else
                    if (index < uiState.restaurantsOnMap.size - 4) {
                        if(uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemId){
                            restaurantMarkerImageProviderNormalSelected
                        } else{
                            restaurantMarkerImageProviderNormal
                        }
                    } else {
                        if(uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemId){
                            restaurantMarkerImageProviderMaxiSelected
                        } else{
                            restaurantMarkerImageProviderMaxi
                        }

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
