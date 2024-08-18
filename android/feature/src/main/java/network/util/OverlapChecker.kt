package network.util

import android.graphics.RectF
import android.graphics.RectF.intersects
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.PlacemarkMapObject
import model.MyPin
import model.PinIcon
import model.Restaurant


fun checkPinsOverlap(pin1: MyPin, pin2: MyPin): Boolean {
    val rect1 = getPinRect(pin1.point, pin1.icon)
    val rect2 = getPinRect(pin2.point, pin2.icon)
    return intersects(rect1, rect2)
}

fun getPinRect(point: Point, icon : PinIcon): RectF {
    val width = icon.width
    val height = icon.height

    val left = point.longitude - (width / 2).toDouble()
    val top = point.latitude - (height / 2).toDouble()
    val right = point.longitude + (width / 2).toDouble()
    val bottom = point.latitude + (height / 2).toDouble()

    return RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
}

