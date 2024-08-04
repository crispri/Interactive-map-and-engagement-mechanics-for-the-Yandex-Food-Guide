package viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.feature.R
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.flow.MutableStateFlow

class MapViewModel(mapview: MapView) : ViewModel() {

    private val startLocation = Point(59.9402, 30.315)
    private var zoomValue = MutableStateFlow<Float>(16.5f)





}