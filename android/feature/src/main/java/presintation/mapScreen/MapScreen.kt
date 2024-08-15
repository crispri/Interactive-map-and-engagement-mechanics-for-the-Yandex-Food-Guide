package presintation.mapScreen

import Utils.createBitmapFromVector
import Utils.createBitmapFromView
import Utils.createNormalPin
import Utils.createSuperPin
import Utils.createSuperSelectedPin
import Utils.invertColors
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
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.ScreenRect
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectDragListener
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.MapObjectVisitor
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.PolygonMapObject
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.map.SizeChangedListener
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import model.CancelCentering
import model.MainScreenEvent
import model.SelectItemFromMap
import model.UpdateItemsOnMap


@Composable
fun MapScreen(
    uiState: MainUiState,
    send: (MainScreenEvent) -> Unit,
    mapView: CustomMapView,
    curLocation: MutableState<Point?>
) {


    val mapObjectCollection = remember { mapView.mapWindow.map.mapObjects }

    /*/////////////////////////////// ниже пересечения

    fun updateFocusRect() {
        val horizontalMargin = 40f
        val verticalMargin = 60f
        mapView.mapWindow.focusRect = ScreenRect(
            ScreenPoint(horizontalMargin, verticalMargin),
            ScreenPoint(
                mapView.mapWindow.width() - horizontalMargin,
                mapView.mapWindow.height() - verticalMargin
            )
        )
    }

    val CLUSTER_RADIUS = remember { 60.0 }
    val CLUSTER_MIN_ZOOM = remember { 15 }
    lateinit var clasterizedCollection: ClusterizedPlacemarkCollection
    var isShowGeometryOnMap = remember { true }

    val mapWindowSizeChangedListener = remember { SizeChangedListener { _, _, _ -> updateFocusRect() } }


    val mapObjectCollection = remember { mapView.mapWindow.map.mapObjects }

    val pinDragListener = remember {
        object : MapObjectDragListener {
            override fun onMapObjectDragStart(p0: MapObject) {
            }

            override fun onMapObjectDrag(p0: MapObject, p1: Point) = Unit

            override fun onMapObjectDragEnd(p0: MapObject) {
                // Updates clusters position
                clasterizedCollection.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)
            }
        }
    }

    val clusterListener = ClusterListener { cluster ->
        val placemarkTypes = cluster.placemarks.map {
            (it.userData as Pair<*, *>).second
        }
        // Sets each cluster appearance using the custom view
        // that shows a cluster's pins
        cluster.appearance.setView(
            ViewProvider(
                ClusterView(this).apply {
                    setData(placemarkTypes)
                }
            )
        )
        cluster.appearance.zIndex = 100f

        //cluster.addClusterTapListener(clusterTapListener)
    }

    val geometryVisibilityVisitor = object : MapObjectVisitor {
        override fun onPlacemarkVisited(placemark: PlacemarkMapObject) = Unit

        override fun onPolylineVisited(polyline: PolylineMapObject) {
            polyline.isVisible = isShowGeometryOnMap
        }

        override fun onPolygonVisited(polygon: PolygonMapObject) {
            polygon.isVisible = isShowGeometryOnMap
        }

        override fun onCircleVisited(circle: CircleMapObject) {
            circle.isVisible = isShowGeometryOnMap
        }

        override fun onCollectionVisitStart(p0: MapObjectCollection): Boolean = true
        override fun onCollectionVisitEnd(p0: MapObjectCollection) = Unit
        override fun onClusterizedCollectionVisitStart(p0: ClusterizedPlacemarkCollection): Boolean =
            true

        override fun onClusterizedCollectionVisitEnd(p0: ClusterizedPlacemarkCollection) = Unit
    }

    ///////////////////////////////////////////////////////// выше пересечения*/






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


    //Normal - general
    val normalView = remember { createNormalPin(mapView.context) }
    val restaurantMarkerNormal = remember { createBitmapFromView(normalView) }
    val restaurantMarkerImageProviderNormal =
        remember { ImageProvider.fromBitmap(restaurantMarkerNormal) }


    //Normal - Selected
    val invertedBitmapNormal = invertColors(restaurantMarkerNormal)
    val restaurantMarkerImageProviderNormalSelected =
        remember { ImageProvider.fromBitmap(invertedBitmapNormal) }


    //Maxi - general
    val superView = remember { createSuperPin(mapView.context) }
    val restaurantMarkerMaxi = remember { createBitmapFromView(superView) }
    val restaurantMarkerImageProviderMaxi =
        remember { ImageProvider.fromBitmap(restaurantMarkerMaxi) }


    //Maxi - Selected
    val superViewSelected = remember { createSuperSelectedPin(mapView.context) }
    val restaurantMarkerMaxiSelected = remember { createBitmapFromView(superViewSelected) }
    val restaurantMarkerImageProviderMaxiSelected =
        remember { ImageProvider.fromBitmap(restaurantMarkerMaxiSelected) }


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
            //val selected = uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromMapId || uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromBottomSheetId
            val placemark = mapObjectCollection.addPlacemark()
            placemark.userData = restaurant.id

            val currentPin =
                if (index < uiState.restaurantsOnMap.size - 6) {
                    if (uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromMapId || uiState.restaurantsOnMap.reversed()[index].id == uiState.selectedItemFromBottomSheetId) {
                        Log.e(
                            "in currentPin",
                            "uiState.selectedItemFromMapId = ${uiState.selectedItemFromMapId}, uiState.selectedItemFromBottomSheetId = ${uiState.selectedItemFromBottomSheetId}"
                        )
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
                            restaurantMarkerImageProviderMaxiSelected
                        } else {
                            restaurantMarkerImageProviderMaxi
                        }

                    }


            placemark.apply {
                geometry = restaurant.coordinates
                setIcon(currentPin)
            }

            placemark.addTapListener(tapListener)

            mapView.addTabListener(tapListener)

            mapView.addCustomPlaceMark(placemark)


            ////////////////////////////////////////////


        }

        mapObjectCollection.addPlacemark().apply {
            geometry = curLocation.value ?: Point(55.733415, 37.590042)
            setIcon(curLocationMarkerImageProvider)
        }

    }
}
