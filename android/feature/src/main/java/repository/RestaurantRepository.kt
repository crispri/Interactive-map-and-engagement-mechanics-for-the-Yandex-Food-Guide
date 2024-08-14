package repository

import kotlinx.coroutines.flow.Flow
import model.Filter
import model.Recommendation
import model.Restaurant
import network.util.NetworkState

interface RestaurantRepository {

    val recommendations: Flow<List<Recommendation>>

    val restaurants: Flow<List<Restaurant>>

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

    fun updateTask(
        task: Restaurant,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<Restaurant>>


}

