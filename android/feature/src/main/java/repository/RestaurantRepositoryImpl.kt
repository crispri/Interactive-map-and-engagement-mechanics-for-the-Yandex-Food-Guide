package repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import model.CollectionOfPlace
import model.Coordinates
import model.Filter
import model.Restaurant
import network.api.RequestBody
import network.api.RequestBodyAddItemCollection
import network.api.RequestBodyCollection
import network.api.YandexMapEatApi
import network.dto.response.CollectionItemForJson
import network.dto.response.RestaurantItemForJson
import network.util.NetworkState
import network.util.toJson
import network.util.toModel
import network.util.toToken
import javax.inject.Inject


/**
 * Implementation of [RestaurantRepository] for handling restaurant-related data operations using YandexMapEatApi.
 *
 * This class provides concrete implementations of the repository methods defined in the [RestaurantRepository] interface,
 * using the YandexMapEatApi for network requests. The methods are responsible for fetching restaurant data, collections,
 * and managing items in collections. The results are returned as [Flow]s of [NetworkState] to handle loading, success,
 * and failure states.
 */
class RestaurantRepositoryImpl @Inject constructor(
    private val api: YandexMapEatApi
) : RestaurantRepository {

    override fun getRestaurants(
        token: String,
        lowerLeftLon: Double,
        topRightLat: Double,
        topRightLon: Double,
        lowerLeftLat: Double,
        onCollection: Boolean,
        filterList: List<Filter>,
    ): Flow<NetworkState<List<Restaurant>>> = flow {
        emit(NetworkState.Loading)
        try {
            val bearToken = token.toToken()
            Log.d("Token", bearToken)

            val response = api.getRestaurants(
                token,
                requestBody = RequestBody(
                    Coordinates(
                        lon = lowerLeftLon,
                        lat = lowerLeftLat,
                    ),
                    Coordinates(
                        lat = topRightLat,
                        lon = topRightLon,
                    ),
                    onlyCollections = onCollection,
                    filters = filterList.map(Filter::toJson),
                )
            )
            emit(
                NetworkState.Success(
                    response.items.map(RestaurantItemForJson::toModel),
                )
            )
        } catch (e: Exception) {
            Log.d("SourceGetException", "${e.message}")
            emit(NetworkState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)


    override fun getRestaurantById(
        token: String,
        id: String,
    ): Flow<NetworkState<Restaurant>> = flow {
        emit(NetworkState.Loading)

        try {
            val bearToken = token.toToken()
            Log.d("Token", bearToken)

            val response = api.getRestaurantById(
                token,
                id = id,
            )
            Log.d(
                "SourceGet", response.name
            )

            emit(
                NetworkState.Success(
                    response.toModel(),
                )
            )
        } catch (e: Exception) {
            Log.d("SourceGetException", "${e.message}")
            emit(NetworkState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getCollections(
        token: String,
        isUserCollection: Boolean,
    ): Flow<NetworkState<List<CollectionOfPlace>>> = flow {
        emit(NetworkState.Loading)

        try {
            val response = api.getCollections(
                token,
                requestBody = RequestBodyCollection(isUserCollection)
            )

            emit(
                NetworkState.Success(
                    response.items.map(CollectionItemForJson::toModel)
                )
            )
        } catch (e: Exception) {
            emit(NetworkState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun addItemInCollection(
        token: String,
        idUserCollection: String,
        restaurantId: String,
    ): Flow<NetworkState<String>> = flow {
        emit(NetworkState.Loading)
        try {
            val bearToken = token.toToken()
            Log.d("Token", bearToken)

            api.addItemToCollection(
                token,
                id = idUserCollection,
                requestBody = RequestBodyAddItemCollection(restaurantId)
            )
            emit(NetworkState.Success("OK"))

        } catch (e: Exception) {
            Log.d("SourceGetException", "${e.message}")
            emit(NetworkState.Failure(e))
        }
    }
}
