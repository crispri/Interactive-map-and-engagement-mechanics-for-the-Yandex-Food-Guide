package repository

import kotlinx.coroutines.flow.Flow
import model.CollectionOfPlace
import model.Filter
import model.Recommendation
import model.Restaurant
import network.util.NetworkState

interface RestaurantRepository {

    fun getRestaurants(
        token: String,
        lowerLeftLat: Double,
        lowerLeftLon: Double,
        topRightLat: Double,
        topRightLon: Double,
        filterList: List<Filter>,
    ): Flow<NetworkState<List<Restaurant>>>

    fun getRestaurantById(
        token: String,
        id: String,
    ): Flow<NetworkState<Restaurant>>

    fun getCollections(
        token: String,
    ): Flow<NetworkState<List<CollectionOfPlace>>>


}

