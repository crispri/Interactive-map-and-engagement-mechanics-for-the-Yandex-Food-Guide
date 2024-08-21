package com.example.yandexmapeat

import Utils.loadJsonFromAsset
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import com.example.yandexmapeat.MapEatApplication.Companion.MAPKIT_API_KEY
import com.example.yandexmapeat.ui.theme.YandexMapEatTheme
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import dagger.hilt.android.AndroidEntryPoint
import presintation.mapScreen.CustomMapView
import presintation.navigation.AppNavigation


/**
 * * Main Activity is the entry point for the application, responsible for initializing the map, managing location updates,
 * and setting up the UI using Jetpack Compose. The activity integrates Yandex MapKit and handles permissions for accessing
 * location data.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("haveApiKey", true)
    }


    private lateinit var mapView: CustomMapView
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var mapkit: MapKit
    private var curLocation = mutableStateOf<Point?>(null)

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }

            permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }

            else -> {
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
        locationListener = object : LocationListener {
            override fun onLocationUpdated(p0: Location) {
                Log.e("in onLocationUpdated", "${p0.position.latitude}, ${p0.position.longitude}")
                curLocation.value = Point(p0.position.latitude, p0.position.longitude)
            }

            override fun onLocationStatusUpdated(p0: LocationStatus) {
                if (p0 == LocationStatus.NOT_AVAILABLE) {
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

        val style = loadJsonFromAsset(this, "map_style.json")
        if (style != null) {
            mapView.mapWindow.map.setMapStyle(style)
        } else {
            Log.d("jsonfile", "error")
        }


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


    override fun onResume() {
        super.onResume()
        locationManager.requestSingleUpdate(locationListener)
    }


    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }


    private fun setApiKey(savedInstanceState: Bundle?) {
        val haveApiKey = savedInstanceState?.getBoolean("haveApiKey") ?: false
        if (!haveApiKey) {
            MapKitFactory.setApiKey(MAPKIT_API_KEY)
        }
    }
}


