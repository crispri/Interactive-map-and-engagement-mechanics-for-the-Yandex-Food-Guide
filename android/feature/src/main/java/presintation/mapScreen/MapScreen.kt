package presintation.mapScreen

import Utils.createBitmapFromVector
import Utils.createBitmapFromView
import Utils.invertColors
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import pins.CustomPinViewSelected
import pins.NormalPinView
import pins.NormalPinViewSelected


@Composable
fun MapScreen(
    uiState: MainUiState,
    send: (MainScreenEvent) -> Unit,
    mapView: CustomMapView,
    curLocation: MutableState<Point?>
) {
    val mapObjectCollection = remember { mapView.mapWindow.map.mapObjects }

    //Mini
    //Mini - general
    val restaurantMarkerMini = remember {
        createBitmapFromVector(
            com.example.core.R.drawable.ic_mini_pin,
            context = mapView.context
        )
    }
    val restaurantMarkerImageProviderMini =
        remember { ImageProvider.fromBitmap(restaurantMarkerMini) }


    //Mini - Selected
    var invertedBitmap = remember { restaurantMarkerMini }
    if (restaurantMarkerMini != null) invertedBitmap = invertColors(restaurantMarkerMini)
    val restaurantMarkerImageProviderMiniSelected =
        remember { ImageProvider.fromBitmap(invertedBitmap) }


    //Normal
    val pinViewN: NormalPinView = NormalPinView(context = mapView.context)
    val pinViewNSelected: NormalPinViewSelected = NormalPinViewSelected(context = mapView.context)

    pinViewN.setTitle("Хороший бар")
    pinViewN.setRating("4.9")
    pinViewNSelected.setTitle("Хороший бар")
    pinViewNSelected.setRating("4.9")

    val restaurantMarkerNormal =
        remember { createBitmapFromView(pinViewN, com.example.core.R.color.grey, 32f, 0f, 0f) }

    val restaurantMarkerNormalSelected =
        remember { createBitmapFromView(pinViewNSelected, com.example.core.R.color.grey, 32f, 0f, 0f) }
    // remember { createBitmapFromVector(R.drawable.restaurant_marker, context = mapView.context) }
    val restaurantMarkerImageProviderNormal =
        remember { ImageProvider.fromBitmap(restaurantMarkerNormal) }

    val restaurantMarkerImageProviderNormalSelected =
        remember { ImageProvider.fromBitmap(restaurantMarkerNormalSelected) }

    //Maxi

    //Maxi
    val pinView = remember { CustomPinView(context = mapView.context) }
    val pinViewSelected = remember { CustomPinViewSelected(context = mapView.context) }

    pinView.setTitle("aaaaa")
    pinView.setDescription("bbb")
    pinView.setRating("2,0")

    pinViewSelected.setTitle("aaaaa")
    pinViewSelected.setDescription("bbb")
    pinViewSelected.setRating("2,0")

    val restaurantMarkerImageProviderMaxi = remember { mutableStateOf<ImageProvider>(ImageProvider.fromBitmap(restaurantMarkerNormal)) }
    val restaurantMarkerImageProviderMaxiSelected = remember { mutableStateOf<ImageProvider>(ImageProvider.fromBitmap(restaurantMarkerNormalSelected)) }
    LaunchedEffect(Unit) {
        pinView.setImageWithGlide("https://img.razrisyika.ru/kart/23/1200/89464-kafe-9.jpg") {
            val restaurantMarkerMaxi = createBitmapFromView(pinView, com.example.core.R.color.grey, 16f, 0f, 0f)
            restaurantMarkerImageProviderMaxi.value = ImageProvider.fromBitmap(restaurantMarkerMaxi)
        }
        pinViewSelected.setImageWithGlide("https://baldezh.top/uploads/posts/2023-12/1703987413_baldezh-top-p-restoran-vnutri-vkontakte-2.jpg") {
            val restaurantMarkerMaxiSelected = createBitmapFromView(pinViewSelected, com.example.core.R.color.grey, 16f, 0f, 0f)
            restaurantMarkerImageProviderMaxiSelected.value = ImageProvider.fromBitmap(restaurantMarkerMaxiSelected)
        }
    }

    // curLocation
    val curLocationMarker = remember {
        createBitmapFromVector(R.drawable.current_location_marker_svg, context = mapView.context)
    }
    val curLocationMarkerImageProvider = remember { ImageProvider.fromBitmap(curLocationMarker) }

    val cameraCallback = remember { Map.CameraCallback {} }

    // for double tap
    val last = remember { mutableStateOf<String?>(null) }
    val cur = remember { mutableStateOf<String?>(null) }

    val tapListener = remember {
        MapObjectTapListener { mapObject, _ ->
            cur.value = mapObject.userData.toString()
            false
        }
    }


    LaunchedEffect(cur.value) {
        if (cur.value != "") {

            if (last.value == cur.value) {
                send(SelectItemFromMap(null))
                last.value = null
            } else {
                send(SelectItemFromMap(cur.value))
                last.value = cur.value
            }
            cur.value = ""
        }
    }

    // end for double tap

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

        Log.d("chastota", "message1111")

        fun moveToStartLocation(curLocation: Point, zoomValue: Float) {
            mapView.mapWindow.map.move(
                CameraPosition(curLocation, zoomValue, 0.0f, 0.0f),
                Animation(Animation.Type.LINEAR, 0.5f),
                cameraCallback
            )
        }

        mapObjectCollection.clear()

        if (uiState.centeringIsRequired && curLocation.value != null) {
            moveToStartLocation(curLocation.value!!, uiState.zoomValue)
            send(CancelCentering())
        }


        uiState.restaurantsOnMap.reversed().forEachIndexed { index, restaurant ->

            Log.d("maponmap11", "1111111itemId == ${uiState.selectedItemFromMapId}")
            //val selected = uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromMapId || uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromBottomSheetId
            val placemark = mapObjectCollection.addPlacemark()
            placemark.userData = restaurant.id

            val currentPin =
                if (index < uiState.restaurantsOnMap.size - 6) {
                    if (uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromMapId || uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromBottomSheetId) {
                        restaurantMarkerImageProviderMiniSelected
                    } else {
                        restaurantMarkerImageProviderMini
                    }
                } else
                    if (index < uiState.restaurantsOnMap.size - 3) {
                        if (uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromMapId || uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromBottomSheetId) {
                            restaurantMarkerImageProviderNormalSelected
                        } else {
                            restaurantMarkerImageProviderNormal
                        }
                    } else {
                        if (uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromMapId || uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromBottomSheetId) {
                            restaurantMarkerImageProviderMaxiSelected.value
                        } else {
                            restaurantMarkerImageProviderMaxi.value
                        }
                    }

            /*clasterizedCollection.addPlacemark().apply {
                geometry = restaurant.coordinates
                setIcon(currentPin)
                isDraggable = true
                setDragListener(pinDragListener)
            }*/

            placemark.apply {
                geometry = restaurant.coordinates
                setIcon(currentPin)
            }

            placemark.addTapListener(tapListener)

            mapView.addTabListener(tapListener)

            mapView.addCustomPlaceMark(placemark)


        }

        mapObjectCollection.addPlacemark().apply {
            geometry = curLocation.value ?: Point(55.733415, 37.590042)
            setIcon(curLocationMarkerImageProvider)
        }

        Log.d("chastota", "message2222")

    }
}
