package model

import android.graphics.Picture
import com.yandex.mapkit.geometry.Point

data class CollectionOfPlace(
    val id: String,
    val name: String,
    val description: String,
    val isPublic: Int,
    val picture: String,
    val link: String,
)

