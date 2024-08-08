package com.example.yandexmapeat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.feature.R
import com.example.yandexmapeat.ui.theme.YandexMapEatTheme
import dagger.hilt.android.AndroidEntryPoint
import presintation.navigation.AppNavigation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import network.NetworkRepository
import network.api.YandexMapEatApi
import network.datasource.NetworkDatasource
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    val locationPermissionRequest = registerForActivityResult(
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

    @Inject
    lateinit var networkDatasource: NetworkDatasource
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationPermissionRequest.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        val networkRepository = NetworkRepository(networkDatasource)
        runBlocking {
            networkRepository.getList()
        }

        MapKitFactory.setApiKey("8e691497-5f95-489d-862e-b24bd7507b87")
        MapKitFactory.initialize(this)

        locationPermissionRequest.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        enableEdgeToEdge()

        setContent {
            YandexMapEatTheme {
                AppNavigation()
            }
        }

    }


}

