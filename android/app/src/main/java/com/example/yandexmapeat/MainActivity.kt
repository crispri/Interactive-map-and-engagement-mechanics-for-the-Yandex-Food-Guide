package com.example.yandexmapeat

import Utils
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.feature.R
import com.example.yandexmapeat.ui.theme.YandexMapEatTheme
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import ui.MapScreen

class MainActivity : ComponentActivity() {
    private lateinit var mapView: MapView
    private lateinit var mapObjectCollection: MapObjectCollection
    private lateinit var placemarkMapObject: PlacemarkMapObject


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

    private val placemarkTapListener = MapObjectTapListener { _, point ->
        Toast.makeText(
            this@MainActivity,
            "Tapped the point (${point.longitude}, ${point.latitude})",
            Toast.LENGTH_SHORT
        ).show()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey("8e691497-5f95-489d-862e-b24bd7507b87")
        MapKitFactory.initialize(this)

        locationPermissionRequest.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)
        )

        enableEdgeToEdge()

        val context = this

        mapView = MapView(context)
        moveToStartLocation(Point(55.733415, 37.590042), 14.0f)

        mapObjectCollection = mapView.mapWindow.map.mapObjects
        val marker = createBitmapFromVector(R.drawable.current_location_marker_svg)
        val marker2 = createBitmapFromVector(R.drawable.general_place_marker_svg)
        //placemarkMapObject = mapObjectCollection.addPlacemark(Point(55.733415, 37.590042), ImageProvider.fromBitmap(marker))
        //placemarkMapObject = mapObjectCollection.addPlacemark(startLocation, ImageProvider.fromResource(this, marker))

        placemarkMapObject = mapObjectCollection.addPlacemark().apply {
            geometry = Point(55.733415, 37.590042)
            setIcon(ImageProvider.fromBitmap(marker))
        }


        val imageProvider = ImageProvider.fromBitmap(marker2)
        Utils.places.forEach { point ->
            mapObjectCollection.addPlacemark().apply {
                geometry = point
                setIcon(imageProvider)
            }
        }

        placemarkMapObject.addTapListener(placemarkTapListener)

        setContent {
            YandexMapEatTheme {
                MapScreen(mapView = mapView)
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

    private fun moveToStartLocation(startLocation: Point, zoomValue: Float) {
        mapView.mapWindow.map.move(
            CameraPosition(startLocation, zoomValue, 0.0f, 0.0f)
        )
    }

    private fun createBitmapFromVector(art: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(this, art) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        Log.e(TAG, bitmap.toString())
        return bitmap
    }

}

