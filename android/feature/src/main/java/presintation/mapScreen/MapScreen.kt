package presintation.mapScreen

import Utils.createBitmapFromVector
import Utils.createBitmapFromView
import Utils.invertColors
import Utils.updateOverlayHeight
import Utils.updateOverlayWidth
import android.graphics.Bitmap
import android.graphics.PointF
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
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import model.CancelCentering
import model.HideIntersections
import model.MainScreenEvent
import model.PinIcon
import model.Pins
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
import java.text.DecimalFormat
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
            12.0F,
            mapView.mapWindow.map.cameraPosition.azimuth, // Текущий угол поворота
            mapView.mapWindow.map.cameraPosition.tilt // Текущий угол наклона
        )
        mapView.mapWindow.map.move(newCameraPosition)

    }

    val mapObjectCollection = remember { mapView.mapWindow.map.mapObjects }

    //Mini
    val restaurantMarkerMini = remember { createBitmapFromVector(com.example.core.R.drawable.ic_mini_pin, context = mapView.context) }
    val restaurantMarkerMiniUltima = remember { createBitmapFromVector(com.example.core.R.drawable.ic_ultima_pin, context = mapView.context) }
    val restaurantMarkerMiniOpenK = remember { createBitmapFromVector(com.example.core.R.drawable.ic_openk_pin, context = mapView.context) }
    val restaurantMarkerImageProviderMini = remember { ImageProvider.fromBitmap(restaurantMarkerMini) }
    val restaurantMarkerImageProviderMiniUltima = remember { ImageProvider.fromBitmap(restaurantMarkerMiniUltima) }
    val restaurantMarkerImageProviderMiniOpenK = remember { ImageProvider.fromBitmap(restaurantMarkerMiniOpenK) }

    val pinViewNorm: NormalPinView = NormalPinView(context = mapView.context, isFavorite = false)
    val restaurantMarkerNormal =
        remember { createBitmapFromView(pinViewNorm, com.example.core.R.color.grey, 32f, 0f, 0f) }

    //Maxi
    val pinView = remember { CustomPinView(context = mapView.context, isFavorite = false, isUltima = false, isOpenKitchen = false) }
    val restaurantMarkerMaxiWidth = remember { mutableStateOf<Int>(0) }
    val restaurantMarkerMaxiHeight = remember { mutableStateOf<Int>(0) }
    val restaurantMarkerImageProviderMaxi = remember { mutableStateOf<ImageProvider>(ImageProvider.fromBitmap(restaurantMarkerNormal)) }

    LaunchedEffect(Unit) {
        val restaurantMarkerMaxi = createBitmapFromView(pinView, com.example.core.R.color.grey, 16f, 0f, 0f)
        restaurantMarkerMaxiWidth.value = restaurantMarkerMaxi.width
        restaurantMarkerMaxiHeight.value = restaurantMarkerMaxi.height
        restaurantMarkerImageProviderMaxi.value = ImageProvider.fromBitmap(restaurantMarkerMaxi)
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
            } else if (uiState.recommendationIsSelected){
                mapView.mapWindow.map.move(
                    CameraPosition(Point(55.7522200, 37.6155600), 12.0F, 0.0f, 0.0f),
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

    LaunchedEffect(Unit) {

        val cameraListener = CameraListener { _, _, _, actionIsFinished ->
            if (actionIsFinished) {
                val topRightPoint = mapView.mapWindow.map.visibleRegion.topRight
                val bottomLeftPoint = mapView.mapWindow.map.visibleRegion.bottomLeft
                val dx = (topRightPoint.latitude - bottomLeftPoint.latitude)*0.25
                val dy = (topRightPoint.longitude - bottomLeftPoint.longitude)*0.2

                val newTopRight = Point(topRightPoint.latitude-dx, topRightPoint.longitude-dy)
                val newBottomLeft = Point(bottomLeftPoint.latitude+dx, bottomLeftPoint.longitude+dy)

                val w = updateOverlayWidth(uiState.zoomValue.toInt(), restaurantMarkerMaxiWidth.value, newBottomLeft, newTopRight)
                val h = updateOverlayHeight(uiState.zoomValue.toInt(), restaurantMarkerMaxiHeight.value, newBottomLeft, newTopRight)
                send(UpdateItemsOnMap(bottomLeftPoint, topRightPoint, uiState.filterList, w, h))
            }
        }
        mapView.addCameraListener(cameraListener)
    }

    // general
    val superPin = remember {
        mutableStateOf(CustomPinView(context = mapView.context, isFavorite = false, isOpenKitchen = false, isUltima = false))
    }
    val superPinUltima = remember {
        mutableStateOf(CustomPinView(context = mapView.context, isFavorite = false, isOpenKitchen = false, isUltima = true))
    }
    val superPinOpenk = remember {
        mutableStateOf(CustomPinView(context = mapView.context, isFavorite = false, isOpenKitchen = true, isUltima = false))
    }
    val superPinFav = remember {
        mutableStateOf(CustomPinView(context = mapView.context, isFavorite = true, isOpenKitchen = false, isUltima = false))
    }
    val superPinUltimaOpenk = remember {
        mutableStateOf(CustomPinView(context = mapView.context, isFavorite = false, isOpenKitchen = true, isUltima = true))
    }
    val superPinOpenkFav = remember {
        mutableStateOf(CustomPinView(context = mapView.context, isFavorite = true, isOpenKitchen = true, isUltima = false))
    }
    val superPinUtlimaFav = remember {
        mutableStateOf(CustomPinView(context = mapView.context, isFavorite = true, isOpenKitchen = false, isUltima = true))
    }
    val superPinUtlimaOpenkFav = remember {
        mutableStateOf(CustomPinView(context = mapView.context, isFavorite = true, isOpenKitchen = true, isUltima = true))
    }

    //selected
    val superPinSelected = remember {
        mutableStateOf(CustomPinViewSelected(context = mapView.context, isFavorite = false, isOpenKitchen = false, isUltima = false))
    }
    val superPinSelectedUltima = remember {
        mutableStateOf(CustomPinViewSelected(context = mapView.context, isFavorite = false, isOpenKitchen = false, isUltima = true))
    }
    val superPinSelectedOpenk = remember {
        mutableStateOf(CustomPinViewSelected(context = mapView.context, isFavorite = false, isOpenKitchen = true, isUltima = false))
    }
    val superPinSelectedFav = remember {
        mutableStateOf(CustomPinViewSelected(context = mapView.context, isFavorite = true, isOpenKitchen = false, isUltima = false))
    }
    val superPinSelectedUltimaOpenk = remember {
        mutableStateOf(CustomPinViewSelected(context = mapView.context, isFavorite = false, isOpenKitchen = true, isUltima = true))
    }
    val superPinSelectedOpenkFav = remember {
        mutableStateOf(CustomPinViewSelected(context = mapView.context, isFavorite = true, isOpenKitchen = true, isUltima = false))
    }
    val superPinSelectedUtlimaFav = remember {
        mutableStateOf(CustomPinViewSelected(context = mapView.context, isFavorite = true, isOpenKitchen = false, isUltima = true))
    }
    val superPinSelectedUtlimaOpenkFav = remember {
        mutableStateOf(CustomPinViewSelected(context = mapView.context, isFavorite = true, isOpenKitchen = true, isUltima = true))
    }

    //norm pin
    val normPinFav = remember {
        mutableStateOf(NormalPinView(context = mapView.context, isFavorite = true))
    }
    LaunchedEffect(uiState.raiseRequired) {
        if (uiState.raiseRequired == true && uiState.selectedItemFromMapId == null) {
            val topRightPoint= mapView.mapWindow.map.visibleRegion.topRight
            val bottomLeftPoint:Point = mapView.mapWindow.map.visibleRegion.bottomLeft

            val w = updateOverlayWidth(uiState.zoomValue.toInt(), restaurantMarkerMaxiWidth.value, bottomLeftPoint, topRightPoint)
            val h = updateOverlayHeight(uiState.zoomValue.toInt(), restaurantMarkerMaxiHeight.value, bottomLeftPoint, topRightPoint)

            val raisedBottomLeftPoint = Point(
                bottomLeftPoint.latitude + ((topRightPoint.latitude - bottomLeftPoint.latitude) * 0.7),
                bottomLeftPoint.longitude
            )
            Log.d("CameraListener", "uiState.raiseRequired = Top right: ${topRightPoint.latitude} : ${topRightPoint.longitude}, Bottom left:  ${raisedBottomLeftPoint.latitude} : ${raisedBottomLeftPoint.longitude}, last Left:   ${bottomLeftPoint.latitude} : ${bottomLeftPoint.longitude}")
            send(UpdateItemsOnMap(raisedBottomLeftPoint, topRightPoint, uiState.filterList, w, h))
            Log.e("CameraListener", "size = ${uiState.restaurantsOnMap.size}  list = ${uiState.restaurantsOnMap}")

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

    fun convertToSuperPin(isFavorite: Boolean, isOpenK: Boolean, isUltima: Boolean) : MutableState<CustomPinView> {
        if(!isFavorite && !isOpenK && !isUltima){
            return superPin
        } else if(!isFavorite && !isOpenK && isUltima){
            return superPinUltima
        } else if(!isFavorite && isOpenK && !isUltima){
            return superPinOpenk
        } else if(!isFavorite && isOpenK && isUltima){
            return superPinUltimaOpenk
        } else if(isFavorite && !isOpenK && !isUltima){
            return superPinFav
        } else if(isFavorite && !isOpenK && isUltima){
            return superPinUtlimaFav
        } else if(isFavorite && isOpenK && !isUltima){
            return superPinOpenkFav
        } else {
            return superPinUtlimaOpenkFav
        }
    }

    fun convertToSuperSelectedPin(isFavorite: Boolean, isOpenK: Boolean, isUltima: Boolean) : MutableState<CustomPinViewSelected> {
        if(!isFavorite && !isOpenK && !isUltima){
            return superPinSelected
        } else if(!isFavorite && !isOpenK && isUltima){
            return superPinSelectedUltima
        } else if(!isFavorite && isOpenK && !isUltima){
            return superPinSelectedOpenk
        } else if(!isFavorite && isOpenK && isUltima){
            return superPinSelectedUltimaOpenk
        } else if(isFavorite && !isOpenK && !isUltima){
            return superPinSelectedFav
        } else if(isFavorite && !isOpenK && isUltima){
            return superPinSelectedUtlimaFav
        } else if(isFavorite && isOpenK && !isUltima){
            return superPinSelectedOpenkFav
        } else {
            return superPinSelectedUtlimaOpenkFav
        }
    }

    LaunchedEffect(uiState.restaurantsOnMap, uiState.selectedItemFromMapId, uiState.selectedItemFromBottomSheetId) {

        mapObjectCollection.clear()

        mapObjectCollection.addPlacemark().apply {
            geometry = curLocation.value ?: Point(55.733415, 37.590042)
            setIcon(curLocationMarkerImageProvider)
        }

        mapObjectCollection.addPlacemark().apply {
            geometry = curLocation.value ?: Point(55.733415, 37.590042)
            setIcon(curLocationMarkerImageProvider)
        }

        for(restaurant in uiState.restaurantsOnMap){
            val isFavorite = restaurant.isFavorite
            val isOpenKitchen = "Открытая кухня" in restaurant.tags
            val isUltima = "ULTIMA GUIDE" in restaurant.tags
            val placemark = mapObjectCollection.addPlacemark()

            val pinSelected = convertToSuperSelectedPin(isFavorite, isOpenKitchen, isUltima)
            val pinCommon = convertToSuperPin(isFavorite, isOpenKitchen, isUltima)

            placemark.userData = restaurant.id
            placemark.geometry = restaurant.coordinates
            when(restaurant.type){
                Pins.MAXI -> {
                    if(uiState.selectedItemFromMapId == restaurant.id || uiState.selectedItemFromBottomSheetId == restaurant.id){
                        pinSelected.value.setTitle(restaurant.name)
                        pinSelected.value.setDescription(restaurant.additionalInfo)
                        pinSelected.value.setRating(DecimalFormat("#.#").format(restaurant.rating))
                        val superPinMarkerSelected = createBitmapFromView(pinSelected.value, com.example.core.R.color.grey, 16f, 0f, 0f)
                        val restaurantMarkerImageProviderSuperSelected =  ImageProvider.fromBitmap(superPinMarkerSelected)
                        placemark.setIcon(restaurantMarkerImageProviderSuperSelected)
                        placemark.zIndex = 100f
                    } else if (uiState.selectedItemFromMapId != null || uiState.selectedItemFromBottomSheetId != null){
                        if(isUltima){
                            placemark.setIcon(restaurantMarkerImageProviderMiniUltima)
                        } else if(isOpenKitchen){
                            placemark.setIcon(restaurantMarkerImageProviderMiniOpenK)
                        } else{
                            placemark.setIcon(restaurantMarkerImageProviderMini)
                        }
                    } else{
                        pinCommon.value.setTitle(restaurant.name)
                        pinCommon.value.setDescription(restaurant.additionalInfo)
                        pinCommon.value.setRating(DecimalFormat("#.#").format(restaurant.rating))
                        val superPinMarker = createBitmapFromView(pinCommon.value, com.example.core.R.color.grey, 16f, 0f, 0f)
                        val restaurantMarkerImageProviderSuper =  ImageProvider.fromBitmap(superPinMarker)
                        placemark.setIcon(restaurantMarkerImageProviderSuper)

                        placemark.zIndex = 10f
                    }
                }
                Pins.NORMAL -> {
                    if(uiState.selectedItemFromMapId == restaurant.id || uiState.selectedItemFromBottomSheetId == restaurant.id){
                        pinSelected.value.setTitle(restaurant.name)
                        pinSelected.value.setDescription(restaurant.additionalInfo)
                        pinSelected.value.setRating(DecimalFormat("#.#").format(restaurant.rating))
                        val superPinMarkerSelected = createBitmapFromView(pinSelected.value, com.example.core.R.color.grey, 16f, 0f, 0f)
                        val restaurantMarkerImageProviderSuperSelected = ImageProvider.fromBitmap(superPinMarkerSelected)
                        placemark.setIcon(restaurantMarkerImageProviderSuperSelected)

                        placemark.zIndex = 100f
                    } else if (uiState.selectedItemFromMapId != null || uiState.selectedItemFromBottomSheetId != null){
                        if(isUltima){
                            placemark.setIcon(restaurantMarkerImageProviderMiniUltima)
                        } else if(isOpenKitchen){
                            placemark.setIcon(restaurantMarkerImageProviderMiniOpenK)
                        } else{
                            placemark.setIcon(restaurantMarkerImageProviderMini)
                        }
                    } else{
                        val pinViewNormal: NormalPinView = if(isFavorite){
                            normPinFav.value
                        } else{
                            normPinFav.value
                        }
                        pinViewNormal.setTitle(restaurant.name)
                        pinViewNormal.setRating(DecimalFormat("#.#").format(restaurant.rating))

                        val restaurantMarkerN = createBitmapFromView(pinViewNormal, com.example.core.R.color.grey, 32f, 0f, 0f)
                        val restaurantMarkerImageProviderNormal = ImageProvider.fromBitmap(restaurantMarkerN)
                        placemark.setIcon(restaurantMarkerImageProviderNormal)
                        placemark.zIndex = 50f
                    }
                }
                Pins.MINI -> {
                    if(uiState.selectedItemFromMapId == restaurant.id || uiState.selectedItemFromBottomSheetId == restaurant.id){
                        pinSelected.value.setTitle(restaurant.name)
                        pinSelected.value.setDescription(restaurant.additionalInfo)
                        pinSelected.value.setRating(DecimalFormat("#.#").format(restaurant.rating))
                        val superPinMarkerSelected = createBitmapFromView(pinSelected.value, com.example.core.R.color.grey, 16f, 0f, 0f)
                        val restaurantMarkerImageProviderSuperSelected =  ImageProvider.fromBitmap(superPinMarkerSelected)
                        placemark.setIcon(restaurantMarkerImageProviderSuperSelected)
                    } else{
                        if(isUltima){
                            placemark.setIcon(restaurantMarkerImageProviderMiniUltima)
                        } else if(isOpenKitchen){
                            placemark.setIcon(restaurantMarkerImageProviderMiniOpenK)
                        } else{
                            placemark.setIcon(restaurantMarkerImageProviderMini)
                        }
                    }

                } else -> {}
            }

            placemark.addTapListener(tapListener)
            mapView.addTabListener(tapListener)
            mapView.addCustomPlaceMark(placemark)
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
    }
}
