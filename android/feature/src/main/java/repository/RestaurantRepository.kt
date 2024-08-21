package repository

import kotlinx.coroutines.flow.Flow
import model.CollectionOfPlace
import model.Filter
import model.Restaurant
import network.util.NetworkState

interface RestaurantRepository {

    fun getRestaurants(
        token: String,
        lowerLeftLat: Double,
        lowerLeftLon: Double,
        topRightLat: Double,
        topRightLon: Double,
        onCollection: Boolean,
        filterList: List<Filter>,
    ): Flow<NetworkState<List<Restaurant>>>

    fun getRestaurantById(
        token: String,
        id: String,
    ): Flow<NetworkState<Restaurant>>

    fun getCollections(
        token: String,
        isUserCollection: Boolean,
    ): Flow<NetworkState<List<CollectionOfPlace>>>

    fun addItemInCollection(
        token: String,
        idUserCollection: String,
        restaurantId: String,
    ): Flow<NetworkState<String>>
}

