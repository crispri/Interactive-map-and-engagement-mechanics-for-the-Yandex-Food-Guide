package presintation.mapScreen

import Utils.createBitmapFromVector
import Utils.createBitmapFromView
import Utils.invertColors
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
import model.HideIntersections
import model.MainScreenEvent
import model.PinIcon
import model.Pins
import model.SelectItemFromMap
import model.UpdateItemsOnMap
import pins.CustomPinView
import pins.CustomPinViewSelected
import pins.NormalPinView
import pins.NormalPinViewSelected
import kotlin.math.cos
import kotlin.math.pow


@Composable
fun MapScreen(
    uiState: MainUiState,
    send: (MainScreenEvent) -> Unit,
    mapView: CustomMapView,
    curLocation: MutableState<Point?>,
    bottomSheetHeight: MutableState<Dp?>
) {

    fun raiseCameraPosition(dpValue: Dp, bottomLeft: Point, topRight: Point) {

        Log.e("raiseCameraPosition", "called")

        val displayMetrics = mapView.context.resources.displayMetrics
        val pixels = dpValue * displayMetrics.density

        val latitudeSpan = bottomLeft.latitude - topRight.latitude
        val longitudeSpan = topRight.longitude - bottomLeft.longitude

        val latitudeOffset = pixels.value / (mapView.height / latitudeSpan)
        val longitudeOffset = pixels.value / (mapView.width / longitudeSpan)

        val newBottomLeft = Point(bottomLeft.latitude + latitudeOffset, bottomLeft.longitude + longitudeOffset)
        val newTopRight = Point(topRight.latitude + latitudeOffset, topRight.longitude + longitudeOffset)

        // Создаем новую позицию камеры с обновленными координатами

        val centerLong = (newBottomLeft.longitude + newTopRight.longitude) / 2.0
        val centerLat = (newBottomLeft.latitude + newTopRight.latitude) / 2.0

        val newCameraPosition = CameraPosition(
            Point(centerLat, centerLong), // Новая целевая точка (центр прямоугольника)
            //mapView.mapWindow.map.cameraPosition.zoom, // Текущий масштаб
            10.0F,
            mapView.mapWindow.map.cameraPosition.azimuth, // Текущий угол поворота
            mapView.mapWindow.map.cameraPosition.tilt // Текущий угол наклона
        )
        mapView.mapWindow.map.move(newCameraPosition)

    }

    LaunchedEffect(uiState.raiseRequired) {
        if(uiState.raiseRequired && bottomSheetHeight.value != null){
            if(bottomSheetHeight.value!! > 0.dp) {
                raiseCameraPosition(bottomSheetHeight.value!!, uiState.lowerLeft, uiState.topRight)
            }
        }
    }


    val mapObjectCollection = remember { mapView.mapWindow.map.mapObjects }

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
    val pinView = remember { CustomPinView(context = mapView.context) }
    val pinViewSelected = remember { CustomPinViewSelected(context = mapView.context) }
    pinViewSelected.setTitle("Хороший бар")
    pinViewSelected.setRating("4.9")
    pinViewSelected.setDescription("кофе от 300Р")


    pinView.setTitle("aaaaa")
    pinView.setDescription("bbb")
    pinView.setRating("2,0")

    val restaurantMarkerImageProviderMaxi = remember { mutableStateOf<ImageProvider>(ImageProvider.fromBitmap(restaurantMarkerNormal)) }
    val restaurantMarkerImageProviderMaxiSelected = remember { mutableStateOf<ImageProvider>(ImageProvider.fromBitmap(restaurantMarkerNormalSelected)) }
    val restaurantMarkerMaxi = createBitmapFromView(pinView, com.example.core.R.color.grey, 16f, 0f, 0f)
    val restaurantMarkerMaxiSelected = createBitmapFromView(pinViewSelected, com.example.core.R.color.grey, 16f, 0f, 0f)

    LaunchedEffect(Unit) {
        pinView.setImageWithGlide("https://img.razrisyika.ru/kart/23/1200/89464-kafe-9.jpg") {
            restaurantMarkerImageProviderMaxi.value = ImageProvider.fromBitmap(restaurantMarkerMaxi)
        }
        pinViewSelected.setImageWithGlide("https://baldezh.top/uploads/posts/2023-12/1703987413_baldezh-top-p-restoran-vnutri-vkontakte-2.jpg") {
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

    LaunchedEffect(uiState.recommendationIsSelected) {
        if(uiState.raiseRequired && bottomSheetHeight.value != null){
            if(bottomSheetHeight.value!! > 0.dp) {
                raiseCameraPosition(bottomSheetHeight.value!!, uiState.lowerLeft, uiState.topRight)
            } else if (uiState.recommendationIsSelected){
                mapView.mapWindow.map.move(
                    CameraPosition(Point(55.7522200, 37.6155600), 10.0F, 0.0f, 0.0f),
                    Animation(Animation.Type.LINEAR, 0.5f),
                    cameraCallback
                )
                Log.e("in LaunchedEffect", "move to Moscow called")
            } else{
                if(curLocation.value != null){
                    mapView.mapWindow.map.move(
                        CameraPosition(curLocation.value!!, uiState.zoomValue,0.0f, 0.0f),
                        Animation(Animation.Type.LINEAR, 0.5f),
                        cameraCallback
                    )
                }
                Log.e("in LaunchedEffect", "move to curLocation called")
            }
        } else if (uiState.recommendationIsSelected){
            mapView.mapWindow.map.move(
                CameraPosition(Point(55.7522200, 37.6155600), 10.0F, 0.0f, 0.0f),
                Animation(Animation.Type.LINEAR, 0.5f),
                cameraCallback
            )
        } else{
            if(curLocation.value != null){
                mapView.mapWindow.map.move(
                    CameraPosition(curLocation.value!!, uiState.zoomValue,0.0f, 0.0f),
                    Animation(Animation.Type.LINEAR, 0.5f),
                    cameraCallback
                )
            }
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

    Log.d("IN MAPSCREEN", uiState.restaurantsOnMap.toString())

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

        /*points =
            hideIntersections(
                uiState.restaurantsOnMap,
                PinIcon(
                    Pins.MINI,
                    restaurantMarkerMini?.width ?: 0,
                    restaurantMarkerMini?.height ?: 0
                ),
                PinIcon(Pins.NORMAL, restaurantMarkerNormal.width, restaurantMarkerNormal.height),
                PinIcon(Pins.MAXI, restaurantMarkerMaxi.width, restaurantMarkerMini?.height ?: 0)
            )*/

    }

    LaunchedEffect(uiState.restaurantsOnMap) {

        fun updateOverlayHeight(zoomLevel: Int, viewHeightInPx: Int, bl: Point, tr: Point) : Double {
            val latitude = (tr.latitude + bl.latitude) / 2
            val metersPerPixel = (40075016.686 / (256 * 2.0.pow(zoomLevel))) * cos(latitude * (Math.PI / 180))
            val heightInMeters = viewHeightInPx * metersPerPixel

            val metersPerDegree = 111320.0
            val latitudeDelta = heightInMeters / metersPerDegree

            return latitudeDelta

        }

        fun updateOverlayWidth(zoomLevel: Int, viewWidthInPx: Int, bl: Point, tr: Point): Double {

            val latitude = (tr.latitude + bl.latitude) / 2
            val metersPerPixel = (40075016.686 / (256 * 2.0.pow(zoomLevel))) * cos(latitude * (Math.PI / 180))
            val widthInMeters = viewWidthInPx * metersPerPixel


            val metersPerDegreeLongitude = 40075016.686 / 360.0 * cos(latitude * (Math.PI / 180))
            val longitudeDelta = widthInMeters / metersPerDegreeLongitude

            return longitudeDelta
        }
        val topRightPoint = mapView.mapWindow.map.visibleRegion.topRight
        val bottomLeftPoint = mapView.mapWindow.map.visibleRegion.bottomLeft
        val w = updateOverlayWidth(uiState.zoomValue.toInt(), restaurantMarkerMaxi.width, bottomLeftPoint, topRightPoint)
        val h = updateOverlayHeight(uiState.zoomValue.toInt(), restaurantMarkerMaxi.height, bottomLeftPoint, topRightPoint)

        if(uiState.restaurantsOnMap.isNotEmpty()){
            Log.d("send HideIntersections", "called")
            send(
                HideIntersections(
                    uiState.restaurantsOnMap,
                    w,
                    h
                )
            )
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            mapView
        },
    )
    { mapView ->

        Log.e("curLocation", "latitude = ${curLocation.value?.latitude}, longitude = ${curLocation.value?.longitude}")

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

        Log.e("POINT_UISTATE", uiState.restaurantsOnMap.toString())

        var maxiCounter = 0
        var normalCounter = 0

        for(point in uiState.restaurantsOnMap){
            val placemark = mapObjectCollection.addPlacemark()
            placemark.userData = point.id
            placemark.geometry = point.coordinates

            when(point.type){
                Pins.MAXI -> {
                    placemark.setIcon(restaurantMarkerImageProviderMaxi.value)
                }
                Pins.NORMAL -> {
                    placemark.setIcon(restaurantMarkerImageProviderNormal)
                }
                Pins.MINI -> {
                    placemark.setIcon(restaurantMarkerImageProviderMini)
                } else -> {}

            }

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