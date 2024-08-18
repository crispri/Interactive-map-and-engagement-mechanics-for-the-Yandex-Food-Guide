package repository

import Utils
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import model.CollectionOfPlace
import model.Coordinates
import model.Filter
import model.Recommendation
import model.Restaurant
import network.api.FilterForJson
import network.api.RequestBody
import network.api.RequestBodyCollection
import network.api.YandexMapEatApi
import network.dto.request.RestaurantItemRequestForJson
import network.dto.response.CollectionItemForJson
import network.dto.response.RestaurantItemForJson
import network.util.NetworkState
import network.util.forJson
import network.util.toJson
import network.util.toModel
import network.util.toToken
import javax.inject.Inject


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
        Log.d("SourceGetLoading", "start")
        emit(NetworkState.Loading)
        Log.d("SourceGetLoading", "end")

        try {
            val bearToken = token.toToken()
            Log.d("Token", bearToken)

            val response = api.getRestaurants(
                bearToken,
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
            Log.d(
                "SourceGet", response.items.toString()
            )

            Log.d("Response", response.toString())
            Log.d("ResponseItems", response.items.toString())
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
        Log.d("SourceGetItemLoading", "start")
        emit(NetworkState.Loading)
        Log.d("SourceGetItemLoading", "end")

        try {
            val bearToken = token.toToken()
            Log.d("Token", bearToken)

            val response = api.getRestaurantById(
                bearToken,
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
        Log.d("SourceGetLoading", "start")
        emit(NetworkState.Loading)
        Log.d("SourceGetLoading", "end")

        try {
            val bearToken = token.toToken()

            val response = api.getCollections(
                bearToken,
                requestBody = RequestBodyCollection(isUserCollection)
            )

            Log.d(
                "SourceGet", response.items.toString()
            )

            Log.d("Response", response.toString())
            Log.d("ResponseItems", response.items.toString())
            emit(
                NetworkState.Success(
                    response.items.map(CollectionItemForJson::toModel)
                )
            )
        } catch (e: Exception) {
            Log.d("SourceGetException", "${e.message}")
            emit(NetworkState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}