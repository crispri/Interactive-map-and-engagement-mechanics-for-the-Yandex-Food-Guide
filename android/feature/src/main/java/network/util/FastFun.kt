package network.util

import com.yandex.mapkit.geometry.Point
import model.CollectionOfPlace
import model.Restaurant
import network.dto.response.RestaurantItemForJson
import model.Coordinates
import model.Filter
import model.Pins
import network.api.FilterForJson
import network.dto.response.CollectionItemForJson

fun Restaurant.forJson(): RestaurantItemForJson = RestaurantItemForJson(
    id,
    Coordinates(lat = coordinates.latitude, lon = coordinates.longitude),
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
    inCollection,
    pin, pictures, score, additionalInfo,
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
    inCollection,
    food, interior, score, Pins.NONE, additionalInfo
)

fun CollectionItemForJson.toModel(): CollectionOfPlace = CollectionOfPlace(
    id,
    name,
    description,
    isPublic,
    picture, link,
)

fun Filter.toJson(): FilterForJson = FilterForJson(
    property, value, operator
)

fun FilterForJson.toModel(): Filter = Filter(
    property, value, operator, true
)

fun String.toToken(): String = "$this"