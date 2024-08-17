package presintation.mapScreen

import Utils.createBitmapFromVector
import Utils.createBitmapFromView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.compose.runtime.remember
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

class CustomMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : MapView(context, attrs, defStyleAttr) {

    private var onCameraPositionChangeFinishedListener: ((CameraPosition) -> Unit)? = null
    private val cameraListeners: MutableSet<CameraListener> = mutableSetOf()
    private val placeMarks: MutableList<PlacemarkMapObject> = mutableListOf()
    private val tapListeners: MutableList<MapObjectTapListener> = mutableListOf()


    fun addCameraListener(cameraListener: CameraListener){
        cameraListeners.add(cameraListener)
        mapWindow.map.addCameraListener(cameraListener)
    }

    fun addCustomPlaceMark(placeMark : PlacemarkMapObject) {
        placeMarks.add(placeMark)
    }

    fun addTabListener(tapListener: MapObjectTapListener){
        tapListeners.add(tapListener)
    }

    fun removeTapListener(tapListener: MapObjectTapListener) {
        tapListeners.remove(tapListener)
    }


    /*fun customClear(){
        mapWindow.map.mapObjects.clear()
        placeMarks.clear()
    }

    fun addPlaceMarkSuperPin(point: Point, isSelected: Boolean, tittle: String, raiting: Double, description: String){
        val superView = createSuperPin(context, tittle, raiting, description)
        val restaurantMarkerSuper = createBitmapFromView(superView)
        var result = restaurantMarkerSuper
        if(isSelected){
            result = invertColors(restaurantMarkerSuper)
        }
        val restaurantMarkerImageProviderSuper = ImageProvider.fromBitmap(result)

        addCustomPlaceMark(point, restaurantMarkerImageProviderSuper)
    }

    fun addPlaceMarkNormalPin(point: Point, isSelected: Boolean, tittle: String, raiting: Double){
        val normalView = createNormalPin(context, tittle, raiting)
        val restaurantMarkerNormal = createBitmapFromView(normalView)
        var result = restaurantMarkerNormal
        if(isSelected){
            result = invertColors(restaurantMarkerNormal)
        }
        val restaurantMarkerImageProviderNormal = ImageProvider.fromBitmap(result)

        addCustomPlaceMark(point, restaurantMarkerImageProviderNormal)
    }


    fun addPlaceMarkMiniPin(point: Point, isSelected: Boolean){
        val restaurantMarkerMini = createBitmapFromVector(com.example.core.R.drawable.ic_mini_pin, context = context)
        var result = restaurantMarkerMini
        if(isSelected && restaurantMarkerMini != null){
            result = invertColors(restaurantMarkerMini)
        }
        val restaurantMarkerImageProviderMini = ImageProvider.fromBitmap(result)

        addCustomPlaceMark(point, restaurantMarkerImageProviderMini)
    }

    private fun addCustomPlaceMark(point: Point, imageProvider: ImageProvider){
        val placeMarkMiniPin = mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = point
            setIcon(imageProvider)
        }
        placeMarks.add(placeMarkMiniPin)
    }*/

    fun getVisibleRegionCoordinates(listener: (CameraPosition) -> Unit) {
        onCameraPositionChangeFinishedListener = listener
    }

    fun getCurrentVisibleRegionCoordinates(): Pair<Point, Point> {
        val visibleRegion = mapWindow.map.visibleRegion
        return Pair(visibleRegion.topRight, visibleRegion.bottomLeft)
    }
}