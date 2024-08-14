package presintation.mapScreen

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.mapview.MapView

class CustomMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : MapView(context, attrs, defStyleAttr) {

    private var onCameraPositionChangeFinishedListener: ((CameraPosition) -> Unit)? = null
    private val cameraListeners: MutableSet<CameraListener> = mutableSetOf()
    private val tapListeners: MutableSet<MapObjectTapListener> = mutableSetOf()


    fun addCameraListener(cameraListener: CameraListener){
        cameraListeners.add(cameraListener)
        mapWindow.map.addCameraListener(cameraListener)
    }

    fun getVisibleRegionCoordinates(listener: (CameraPosition) -> Unit) {
        onCameraPositionChangeFinishedListener = listener
    }

    fun getCurrentVisibleRegionCoordinates(): Pair<Point, Point> {
        val visibleRegion = mapWindow.map.visibleRegion
        return Pair(visibleRegion.topRight, visibleRegion.bottomLeft)
    }
}