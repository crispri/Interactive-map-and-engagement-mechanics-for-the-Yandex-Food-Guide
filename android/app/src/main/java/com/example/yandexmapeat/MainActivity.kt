package com.example.yandexmapeat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.example.feature.R
import com.example.yandexmapeat.ui.theme.YandexMapEatTheme
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.AndroidEntryPoint
import presintation.navigation.AppNavigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var mapView: MapView
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
                Log.e("Location", "latitude: ${p0.position.latitude}")
                Log.e("Location", "longitude: ${p0.position.longitude}")
            }

            override fun onLocationStatusUpdated(p0: LocationStatus) {
                if(p0 == LocationStatus.NOT_AVAILABLE){
                    Log.e("LocationStatus", "LocationStatus is NOT_AVAILABLE")
                }
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        enableEdgeToEdge()

        val context = this

        mapView = MapView(context)


        setContent {
            YandexMapEatTheme {
                AppNavigation(mapView)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
        locationManager.requestSingleUpdate(locationListener)
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