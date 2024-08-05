package presintation.mapScreen

import androidx.lifecycle.ViewModel
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.flow.MutableStateFlow

class MapViewModel(mapview: MapView) : ViewModel() {

    private val startLocation = Point(59.9402, 30.315)
    private var zoomValue = MutableStateFlow<Float>(16.5f)





}