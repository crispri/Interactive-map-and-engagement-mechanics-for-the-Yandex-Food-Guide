package model

import com.yandex.mapkit.geometry.Point

data class Restaurant(
    val id: String,
    val coordinates: Point,
    val name: String,
    val description: String,
    val address: String,
    val isApproved: Boolean,
    val rating: Double,
    val priceLowerBound: Int,
    val priceUpperBound: Int,
    val openTime: String,
    val closeTime: String,
    val isFavorite: Boolean,
    val tags: List<String>,
    val inCollection: Boolean,
    val pin: String,
    val pictures: List<String>,
    val score: Long,
    val additionalInfo: String,
)