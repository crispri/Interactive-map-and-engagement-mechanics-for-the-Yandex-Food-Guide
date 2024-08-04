package ui

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.feature.R
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

@Composable
fun MapScreen(mapView: MapView){

    lateinit var mapObjectCollection: MapObjectCollection
    lateinit var placemarkMapObject: PlacemarkMapObject

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->

            mapView.apply {}
        },

    ){


    }
}


