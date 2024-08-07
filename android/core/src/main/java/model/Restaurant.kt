package model

import com.yandex.mapkit.geometry.Point

data class Restaurant(
    val id: String,
    val coorinates: Point,
    val name: String,
    val description: String,
    val address: String,
    val isApproved: Boolean,
    val rating: Double,
    val priceLowerBound: Int,
    val priceUpperBound: Int,
    val tags: List<String>,
    val isFavorite: Boolean
)