package repository

import kotlinx.coroutines.flow.Flow
import model.CollectionOfPlace
import model.Filter
import model.Restaurant
import network.util.NetworkState

/**
 * Interface for repository operations related to restaurants and collections.
 *
 * This interface defines the methods for interacting with the data source to fetch and manage restaurant and
 * collection data. Implementations of this interface handle network requests and provide the results as [Flow]s
 * * of [NetworkState] to the ViewModel or other consumers.
 */
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

