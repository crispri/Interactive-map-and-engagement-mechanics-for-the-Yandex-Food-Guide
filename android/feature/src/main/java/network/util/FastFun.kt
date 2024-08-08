package network.util

import com.yandex.mapkit.geometry.Point
import model.Restaurant
import network.dto.response.RestaurantItemForJson
import model.Coordinates

fun Restaurant.forJson(): RestaurantItemForJson = RestaurantItemForJson(
    id,
    Coordinates(lat = coordinates.latitude,  lon = coordinates.longitude),
    name,
    description,
    address,
    isApproved,
    rating,
    priceLowerBound,
    priceUpperBound,
    openTime,
    closeTime,
    isFavorite,
    tags,
)

fun RestaurantItemForJson.toModel(): Restaurant = Restaurant(
    id,
    Point(coordinates.lat, coordinates.lon),
    name,
    description,
    address,
    isApproved,
    rating,
    priceLowerBound,
    priceUpperBound,
    openTime,
    closeTime,
    isFavorite,
    tags,
)

fun String.toToken(): String = "Bearer $this"
