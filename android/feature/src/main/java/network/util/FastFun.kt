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
    picture,
    preCreatedCollectionName =  preCreatedCollectionName,
)

fun Filter.toJson(): FilterForJson = FilterForJson(
    property, value, operator
)

fun String.toToken(): String = "$this"