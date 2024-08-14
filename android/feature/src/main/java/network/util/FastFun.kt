package network.util

import com.yandex.mapkit.geometry.Point
import model.Restaurant
import network.dto.response.RestaurantItemForJson
import model.Coordinates
import model.Filter
import network.api.FilterForJson

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

fun Filter.toJson() : FilterForJson = FilterForJson(
    property, value, operator
)

fun FilterForJson.toModel() : Filter = Filter(
    property, value, operator
)

fun String.toToken(): String = "Bearer $this"
