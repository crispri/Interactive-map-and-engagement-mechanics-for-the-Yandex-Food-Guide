package network.util

import network.dto.response.RestaurantItemForJson
import presintation.model.RestaurantModel

fun RestaurantModel.forJson(): RestaurantItemForJson = RestaurantItemForJson(
    id,
    coordinates,
    name,
    description,
    address,
    isApproved,
    rating,
    priceLowerBound,
    priceUpperBound,
    isFavorite,
    tags,
)

fun RestaurantItemForJson.toModel(): RestaurantModel = RestaurantModel(
    id,
    coordinates,
    name,
    description,
    address,
    isApproved,
    rating,
    priceLowerBound,
    priceUpperBound,
    isFavorite,
    tags,
)

fun String.toToken(): String = "Bearer $this"
