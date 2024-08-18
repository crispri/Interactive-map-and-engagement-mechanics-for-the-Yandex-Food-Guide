package network.util

import android.graphics.RectF
import android.graphics.RectF.intersects
import com.yandex.mapkit.map.PlacemarkMapObject


data class PinIcon(
    val name: Pins,
    val width: Int,
    val height: Int
)

enum class Pins(val text: String){
    MINI(text = "mini"),
    NORMAL(text = "normal"),
    MAXI(text = "maxi")
}

fun hideIntersections(list: MutableList<PlacemarkMapObject>, miniPin : PinIcon, normalPin: PinIcon, maxiPin : PinIcon) : MutableSet<MyPin>{
    val resSet = mutableSetOf<MyPin>()

    var miniCount = 0
    var normalCount = 0
    var maxiCount = 0

    for(pin1 in list){
        for(pin2 in resSet){
            if(maxiCount < 3){
                if(!checkPinsOverlap(MyPin(pin1, maxiPin), pin2)){
                    resSet.add(MyPin(pin1, maxiPin))
                    maxiCount++
                } else{
                    if(!checkPinsOverlap(MyPin(pin1, normalPin), pin2)){
                        resSet.add(MyPin(pin1, normalPin))
                        normalCount++
                    } else if(!checkPinsOverlap(MyPin(pin1, miniPin), pin2)){
                        resSet.add(MyPin(pin1, miniPin))
                        miniCount++
                    }
                }
            } else if(normalCount < 3){
                if(!checkPinsOverlap(MyPin(pin1, normalPin), pin2)){
                    resSet.add(MyPin(pin1, normalPin))
                    normalCount++
                } else{
                    if(!checkPinsOverlap(MyPin(pin1, miniPin), pin2)){
                        resSet.add(MyPin(pin1, miniPin))
                        miniCount++
                    }
                }
            } else {
                resSet.add(MyPin(pin1, miniPin))
                miniCount++
            }
        }
    }
    return resSet
}

data class MyPin(
    val placeMark: PlacemarkMapObject,
    val icon: PinIcon
)

fun checkPinsOverlap(pin1: MyPin, pin2: MyPin): Boolean {
    val rect1 = getPinRect(pin1.placeMark, pin1.icon)
    val rect2 = getPinRect(pin2.placeMark, pin2.icon)
    return intersects(rect1, rect2)
}

fun getPinRect(pin: PlacemarkMapObject, icon : PinIcon): RectF {
    val point = pin.geometry

    val width = icon.width
    val height = icon.height

    val left = point.longitude - (width / 2).toDouble()
    val top = point.latitude - (height / 2).toDouble()
    val right = point.longitude + (width / 2).toDouble()
    val bottom = point.latitude + (height / 2).toDouble()

    return RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
}

