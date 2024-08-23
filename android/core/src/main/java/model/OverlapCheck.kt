package model

import com.yandex.mapkit.geometry.Point

/**
 * Represents the icon used for a map pin.
 */
data class PinIcon(
    val name: Pins,
    val width: Int,
    val height: Int,
    val point: Point? = null
)

/**
 * Enum class that defines the different types of pin icons available.
 */
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