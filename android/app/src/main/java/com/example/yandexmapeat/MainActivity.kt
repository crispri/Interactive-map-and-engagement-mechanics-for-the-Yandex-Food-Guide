package com.example.yandexmapeat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.yandexmapeat.ui.theme.YandexMapEatTheme
import dagger.hilt.android.AndroidEntryPoint
import presintation.navigation.AppNavigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


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

//    @Inject
//    lateinit var networkDatasource: YandexMapEatApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationPermissionRequest.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
//        val networkRepository = RestaurantRepositoryImpl()
//
//        runBlocking {
//            networkRepository.getList()
//        }
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

