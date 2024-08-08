package repository

import Utils
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import model.Recommendation
import model.Restaurant
import network.api.RequestBody
import network.api.YandexMapEatApi
import network.dto.request.RestaurantItemRequestForJson
import network.dto.response.RestaurantItemForJson
import network.util.NetworkState
import network.util.forJson
import network.util.toModel
import network.util.toToken
import model.Coordinates
import javax.inject.Inject


class RestaurantRepositoryImpl @Inject constructor(
    private val api: YandexMapEatApi
) : RestaurantRepository {

    override val recommendations: Flow<List<Recommendation>>
        get() = flowOf(Utils.recommendations)

    override val restaurants: Flow<List<Restaurant>>
        get() = flowOf(Utils.restaurants)

    override fun getRestaurants(
        token: String,
        lowerLeftLon: Double,
        topRightLat: Double,
        topRightLon: Double,
        lowerLeftLat: Double,
        maxCount: Int
    ): Flow<NetworkState<List<Restaurant>>> = flow {
        Log.d("SourceGetLoading", "start")
        emit(NetworkState.Loading)
        Log.d("SourceGetLoading", "end")

        try {
            val bearToken = token.toToken()
            Log.d("Token", bearToken)

            val response = api.getRestaurants(
                bearToken,
                requestBody =  RequestBody(
                    Coordinates(
                        lon = lowerLeftLon,
                        lat = lowerLeftLat,
                    ),
                    Coordinates(
                        lat = topRightLat,
                        lon = topRightLon,
                    ),
                    // maxCount,
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
                    0
                )
            )
        } catch (e: Exception) {
            Log.d("SourceGetException", "${e.message}")
            emit(NetworkState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)


    override fun updateTask(
        item: Restaurant,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<Restaurant>> = flow {
        emit(NetworkState.Loading)
        try {
            val response = api.putTask(
                token.toToken(),
                revision,
                item.id,
                RestaurantItemRequestForJson(item.forJson())
            )
            emit(NetworkState.OK(response.revision))
        } catch (e: Exception) {
            emit(NetworkState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}