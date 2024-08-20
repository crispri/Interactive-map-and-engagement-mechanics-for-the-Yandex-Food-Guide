package model

import com.yandex.mapkit.geometry.Point

data class PinIcon(
    val name: Pins,
    val width: Int,
    val height: Int,
    val point: Point? = null
)

enum class Pins(val text: String) {
    MINI(text = "mini"),
    NORMAL(text = "normal"),
    MAXI(text = "maxi"),
    NONE(text = "none")
}

data class MyPin(
    val point: Point,
    val icon: PinIcon,
    val restaurantId: String = ""
)