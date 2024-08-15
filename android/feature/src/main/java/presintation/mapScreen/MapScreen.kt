package presintation.mapScreen

import Utils.createBitmapFromVector
import Utils.createBitmapFromView
import Utils.createNormalPin
import Utils.createSuperPin
import Utils.createSuperSelectedPin
import Utils.invertColors
import android.graphics.Color
import android.util.Log
import android.view.View
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
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import model.CancelCentering
import model.MainScreenEvent
import model.SelectItemFromMap
import model.UpdateItemsOnMap
import pins.CustomPinView
import pins.NormalPinView


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
        remember {
            createBitmapFromVector(
                com.example.core.R.drawable.ic_mini_pin,
                context = mapView.context
            )
        }
    val restaurantMarkerImageProviderMini =
        remember { ImageProvider.fromBitmap(restaurantMarkerMini) }

    //Normal
    val pinViewN: NormalPinView = NormalPinView(context = mapView.context)

    pinViewN.setTitle("Хороший бар")
    pinViewN.setRating("4.9")
    val restaurantMarkerNormal =
        remember { createBitmapFromView(pinViewN, com.example.core.R.color.grey, 32f, 0f, 0f) }
    // remember { createBitmapFromVector(R.drawable.restaurant_marker, context = mapView.context) }
    val restaurantMarkerImageProviderNormal =
        remember { ImageProvider.fromBitmap(restaurantMarkerNormal) }

    //Maxi
    val pinView: CustomPinView = CustomPinView(context = mapView.context)
    pinView.setImageWithCoil("https://img.razrisyika.ru/kart/23/1200/89464-kafe-9.jpg")
    Log.d("imageReady", "Ready")
    pinView.setTitle("Хороший бар")
    pinView.setRating("4.9")
    pinView.setDescription("кофе от 300Р")
    val restaurantMarkerMaxi =
        remember { createBitmapFromView(pinView, com.example.core.R.color.grey, 16f, 0f, 0f) }
    val restaurantMarkerImageProviderMaxi =
        remember { ImageProvider.fromBitmap(restaurantMarkerMaxi) }

    // curLocation
    val curLocationMarker = remember {
        createBitmapFromVector(R.drawable.current_location_marker_svg, context = mapView.context)
    }
    val curLocationMarkerImageProvider = remember { ImageProvider.fromBitmap(curLocationMarker) }

    val cameraCallback = remember { Map.CameraCallback {} }

    val tapListener = remember {
        MapObjectTapListener { mapObject, _ ->
            send(SelectItemFromMap(mapObject.userData.toString()))
            true
        }
    }

    LaunchedEffect(Unit) {

        //mapView.mapWindow.map.isNightModeEnabled = true // Включаем ночной режим

        val cameraListener = CameraListener { _, _, _, actionIsFinished ->
            if (actionIsFinished) {
                val topRightPoint = mapView.mapWindow.map.visibleRegion.topRight
                val bottomLeftPoint = mapView.mapWindow.map.visibleRegion.bottomLeft
                send(UpdateItemsOnMap(bottomLeftPoint, topRightPoint, uiState.filterList))
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
            val selected = uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromMapId || uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromBottomSheetId
            val currentPin =
                if (index < uiState.restaurantsOnMap.size - 8) {
                    if (selected) {
                        restaurantMarkerImageProviderMiniSelected
                    } else {
                        restaurantMarkerImageProviderMini
                    }
                } else
                    if (index < uiState.restaurantsOnMap.size - 4) {
                        if(selected){
                            restaurantMarkerImageProviderNormalSelected
                        } else {
                            restaurantMarkerImageProviderNormal
                        }
                    } else {
                        if (selected) {
                            restaurantMarkerImageProviderMaxiSelected
                        } else {
                            restaurantMarkerImageProviderMaxi
                        }

                    }

            val placemark = mapObjectCollection.addPlacemark().apply {
                geometry = restaurant.coordinates
                setIcon(currentPin)
            }
            placemark.userData = restaurant.id
            placemark.addTapListener(tapListener)

            mapView.addTabListener(tapListener)
            mapView.addCustomPlaceMark(placemark)
        }

        mapObjectCollection.addPlacemark().apply {
            geometry = curLocation.value ?: Point(55.733415, 37.590042)
            setIcon(curLocationMarkerImageProvider)
        }

    }
}
