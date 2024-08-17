package com.example.yandexmapeat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.example.feature.R
import com.example.yandexmapeat.ui.theme.YandexMapEatTheme
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.ScreenRect
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterTapListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectDragListener
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.MapObjectVisitor
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.PolygonMapObject
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.map.SizeChangedListener
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import presintation.mapScreen.CustomMapView
import presintation.navigation.AppNavigation

private const val CLUSTER_RADIUS = 60.0
private const val CLUSTER_MIN_ZOOM = 15

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var mapView: CustomMapView
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var mapkit: MapKit
    private var curLocation = mutableStateOf<Point?>(null)

    ////////////////////
    /*private lateinit var collection: MapObjectCollection
    private lateinit var clasterizedCollection: ClusterizedPlacemarkCollection
    private var isShowGeometryOnMap = true

    private fun updateFocusRect() {
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

    private val mapWindowSizeChangedListener = SizeChangedListener { _, _, _ ->
        updateFocusRect()
    }

    private val polylineTapListener = MapObjectTapListener { mapObject, _ ->
        true
    }

    private val circleTapListener = MapObjectTapListener { mapObject, _ ->
        true
    }

    private val placemarkTapListener = MapObjectTapListener { mapObject, _ ->
        true
    }

    private val pinDragListener = object : MapObjectDragListener {
        override fun onMapObjectDragStart(p0: MapObject) {
        }

        override fun onMapObjectDrag(p0: MapObject, p1: Point) = Unit

        override fun onMapObjectDragEnd(p0: MapObject) {
            // Updates clusters position
            clasterizedCollection.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)
        }
    }

    private val clusterListener = ClusterListener { cluster ->

        for(i in cluster.placemarks.indices){
            cluster.placemarks[i].isVisible = (i == 0)
        }

        cluster.appearance.zIndex = 100f

        cluster.addClusterTapListener(clusterTapListener)
    }

    private val clusterTapListener = ClusterTapListener {
        true
    }

    // Iterates through map objects and update polylines, circle, and polygons visibility.
    private val geometryVisibilityVisitor = object : MapObjectVisitor {
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

    private val singlePlacemarkTapListener = MapObjectTapListener { _, _ ->
        true
    }

    private val animatedPlacemarkTapListener = MapObjectTapListener { _, _ ->
        true
    }*/
    ////////////////////////////

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
            // No location access granted.
        }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setApiKey(savedInstanceState)
        MapKitFactory.initialize(this)

        mapkit = MapKitFactory.getInstance()

        locationManager = mapkit.createLocationManager()
        locationListener = object : LocationListener{
            override fun onLocationUpdated(p0: Location) {
                Log.e("in onLocationUpdated", "${p0.position.latitude}, ${p0.position.longitude}")
                curLocation.value = Point(p0.position.latitude, p0.position.longitude)
            }

            override fun onLocationStatusUpdated(p0: LocationStatus) {
                if(p0 == LocationStatus.NOT_AVAILABLE){
                    Log.e("LocationStatus", "LocationStatus is NOT_AVAILABLE")
                }
            }
        }

        locationManager.requestSingleUpdate(locationListener)

        locationPermissionRequest.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        enableEdgeToEdge()

        val context = this

        mapView = CustomMapView(context)

        setContent {
            YandexMapEatTheme {
                AppNavigation(mapView, curLocation)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        locationManager.requestSingleUpdate(locationListener)
    }

    private fun setApiKey(savedInstanceState: Bundle?) {
        val haveApiKey = savedInstanceState?.getBoolean("haveApiKey") ?: false
        if (!haveApiKey) {
            MapKitFactory.setApiKey(MAPKIT_API_KEY)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("haveApiKey", true)
    }

    companion object {
        const val MAPKIT_API_KEY = "8e691497-5f95-489d-862e-b24bd7507b87"
    }

}