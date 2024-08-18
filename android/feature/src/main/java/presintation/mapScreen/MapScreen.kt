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
import model.MainScreenEvent
import model.Restaurant
import model.SelectItemFromMap
import model.SetNewList
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

//    LaunchedEffect(uiState.raiseRequired) {
//        if(uiState.raiseRequired && bottomSheetHeight.value != null){
//            if(bottomSheetHeight.value!! > 0.dp) {
//                raiseCameraPosition(bottomSheetHeight.value!!, uiState.lowerLeft, uiState.topRight)
//            }
//        }
//    }


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

    pinViewSelected.setTitle("aaaaa")
    pinViewSelected.setDescription("bbb")
    pinViewSelected.setRating("2,0")

    val restaurantMarkerImageProviderMaxi = remember { mutableStateOf<ImageProvider>(ImageProvider.fromBitmap(restaurantMarkerNormal)) }

    val restaurantMarkerMaxiWidth = remember { mutableStateOf<Int>(0) }
    val restaurantMarkerMaxiHeight = remember { mutableStateOf<Int>(0) }


    val restaurantMarkerImageProviderMaxiSelected = remember { mutableStateOf<ImageProvider>(ImageProvider.fromBitmap(restaurantMarkerNormalSelected)) }

    LaunchedEffect(Unit) {
        pinView.setImageWithGlide("https://img.razrisyika.ru/kart/23/1200/89464-kafe-9.jpg") {
            val restaurantMarkerMaxi = createBitmapFromView(pinView, com.example.core.R.color.grey, 16f, 0f, 0f)
            restaurantMarkerMaxiWidth.value = restaurantMarkerMaxi.width
            restaurantMarkerMaxiHeight.value = restaurantMarkerMaxi.height
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

    // end for double tap

    LaunchedEffect(uiState.raiseRequired) {
        if (uiState.raiseRequired == true && uiState.selectedItemFromMapId == null) {
            val topRightPoint= mapView.mapWindow.map.visibleRegion.topRight
            val bottomLeftPoint:Point = mapView.mapWindow.map.visibleRegion.bottomLeft


            val w = updateOverlayWidth(uiState.zoomValue.toInt(), restaurantMarkerMaxiWidth.value, bottomLeftPoint, topRightPoint)
            val h = updateOverlayHeight(uiState.zoomValue.toInt(), restaurantMarkerMaxiHeight.value, bottomLeftPoint, topRightPoint)

            Log.d("CameraListener", "w = $w h = $h")

            val raisedBottomLeftPoint = Point(
                bottomLeftPoint.latitude + ((topRightPoint.latitude - bottomLeftPoint.latitude) * 0.7),
                bottomLeftPoint.longitude
            )
            Log.d("CameraListener", "uiState.raiseRequired = Top right: ${topRightPoint.latitude} : ${topRightPoint.longitude}, Bottom left:  ${raisedBottomLeftPoint.latitude} : ${raisedBottomLeftPoint.longitude}, last Left:   ${bottomLeftPoint.latitude} : ${bottomLeftPoint.longitude}")
            send(UpdateItemsOnMap(raisedBottomLeftPoint, topRightPoint, uiState.filterList, w, h))
            Log.e("CameraListener", "size = ${uiState.restaurantsOnMap.size}  list = ${uiState.restaurantsOnMap}")
//            send(SetNewList(ls))

        }
        else {
            val topRightPoint= mapView.mapWindow.map.visibleRegion.topRight
            val bottomLeftPoint:Point = mapView.mapWindow.map.visibleRegion.bottomLeft
            val w = updateOverlayWidth(uiState.zoomValue.toInt(), restaurantMarkerMaxiWidth.value, bottomLeftPoint, topRightPoint)
            val h = updateOverlayHeight(uiState.zoomValue.toInt(), restaurantMarkerMaxiHeight.value, bottomLeftPoint, topRightPoint)
            Log.d("CameraListener", "uiState.raiseRequired = Top right: $topRightPoint, Bottom left: $bottomLeftPoint")
            send(UpdateItemsOnMap(bottomLeftPoint, topRightPoint, uiState.filterList, w, h))
        }
    }
    LaunchedEffect(Unit) {

        //mapView.mapWindow.map.isNightModeEnabled = true // Включаем ночной режим

        val cameraListener = CameraListener { _, _, _, actionIsFinished ->
            if (actionIsFinished) {
                val topRightPoint = mapView.mapWindow.map.visibleRegion.topRight
                val bottomLeftPoint = mapView.mapWindow.map.visibleRegion.bottomLeft
                val w = updateOverlayWidth(uiState.zoomValue.toInt(), restaurantMarkerMaxiWidth.value, bottomLeftPoint, topRightPoint)
                val h = updateOverlayHeight(uiState.zoomValue.toInt(), restaurantMarkerMaxiHeight.value, bottomLeftPoint, topRightPoint)
                send(UpdateItemsOnMap(bottomLeftPoint, topRightPoint, uiState.filterList, w, h))
//                Log.d("CameraListener", "Top right: $topRightPoint, Bottom left: $bottomLeftPoint")
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

        mapObjectCollection.clear()

        if (uiState.centeringIsRequired && curLocation.value != null) {
            moveToStartLocation(curLocation.value!!, uiState.zoomValue)
            send(CancelCentering())
        }


        uiState.restaurantsOnMap.reversed().forEachIndexed { index, restaurant ->
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

    }
}


fun updateOverlayHeight(zoomLevel: Int, viewHeightInPx: Int, bl: Point, tr: Point) : Double {


// Средняя широта, на которой находится карта
    val latitude = (tr.latitude + bl.latitude) / 2

// Конвертируем высоту вью в метры
    val metersPerPixel = (40075016.686 / (256 * 2.0.pow(zoomLevel))) * cos(latitude * (Math.PI / 180))
    val heightInMeters = viewHeightInPx * metersPerPixel

// Конвертируем метры в градусы широты
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


fun filterNonOverlappingRestaurants(
    restaurants: List<Restaurant>,
    rectWidth: Double, // Ширина прямоугольника в градусах
    rectHeight: Double
): List<Restaurant> {

    val nonOverlappingRestaurants = mutableListOf<Restaurant>()

    for (restaurant in restaurants) {
        val restaurantRect = createRectangle(restaurant.coordinates, rectWidth, rectHeight)


        var isOverlapping = false
        for (filteredRestaurant in nonOverlappingRestaurants) {
            val filteredRestaurantRect = createRectangle(filteredRestaurant.coordinates, rectWidth, rectHeight)

            if (rectanglesOverlap(restaurantRect, filteredRestaurantRect)) {
                isOverlapping = true
                break
            }
        }

        if (!isOverlapping) {
            nonOverlappingRestaurants.add(restaurant)
        }
    }

    return nonOverlappingRestaurants
}

fun createRectangle(center: Point, width: Double, height: Double): Rect {
    val halfWidth = width / 2
    val halfHeight = height / 2
    return Rect(
        bottomLeft = Point(center.latitude - halfHeight, center.longitude - halfWidth),
        topRight = Point(center.latitude + halfHeight, center.longitude + halfWidth)
    )
}

fun rectanglesOverlap(rect1: Rect, rect2: Rect): Boolean {
    Log.d("rectanglesOverlap", "rect1.topRight.latitude = ${rect1.topRight.latitude}, rect2.bottomLeft.latitude = ${rect2.bottomLeft.latitude }")
    Log.d("rectanglesOverlap", "rect1.bottomLeft.latitude = ${rect1.bottomLeft.latitude}, rect2.topRight.latitude = ${rect2.topRight.latitude }")
    Log.d("rectanglesOverlap", "rect1.topRight.longitude = ${rect1.topRight.longitude}, rect2.bottomLeft.longitude = ${rect2.bottomLeft.longitude }")
    Log.d("rectanglesOverlap", " rect1.bottomLeft.longitude = ${ rect1.bottomLeft.longitude}, rect2.topRight.longitude = ${rect2.topRight.longitude }")
    Log.d("rectanglesOverlap", " rect1.bottomLeft.longitude = ${ (rect1.topRight.latitude < rect2.bottomLeft.latitude || rect1.bottomLeft.latitude > rect2.topRight.latitude ||  rect1.topRight.longitude < rect2.bottomLeft.longitude ||  rect1.bottomLeft.longitude > rect2.topRight.longitude)}")
    return !(rect1.topRight.latitude < rect2.bottomLeft.latitude || // rect1 ниже rect2
            rect1.bottomLeft.latitude > rect2.topRight.latitude || // rect1 выше rect2
            rect1.topRight.longitude < rect2.bottomLeft.longitude || // rect1 левее rect2
            rect1.bottomLeft.longitude > rect2.topRight.longitude)  // rect1 правее rect2
}

// Данные прямоугольника
data class Rect(val bottomLeft: Point, val topRight: Point)


/*fun updateFocusRect(mapView: CustomMapView) {
    val horizontalMargin = 40f
    val verticalMargin = 60f
    mapView.mapWindow.focusRect = ScreenRect(
        ScreenPoint(horizontalMargin, verticalMargin),
        ScreenPoint(
            mapView.mapWindow.width() - horizontalMargin,
            mapView.mapWindow.height() - verticalMargin
        )
    )
}*/
