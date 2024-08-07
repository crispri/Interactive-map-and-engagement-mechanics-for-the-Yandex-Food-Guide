package network.datasource

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import network.api.YandexMapEatApi
import network.dto.request.RestaurantItemRequestForJson
import network.dto.response.RestaurantItemForJson
import network.util.NetworkState
import network.util.forJson
import network.util.toModel
import network.util.toToken
import presintation.model.RestaurantModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkDatasource @Inject constructor(
    private val api: YandexMapEatApi
) : INetworkDatasource {


    override fun getTasks(
        token: String,
        lowerLeftLon: Int,
        topRightLat: Int,
        topRightLon: Int,
        lowerLeftLat: Int,
        maxCount: Int
    ): Flow<NetworkState<List<RestaurantModel>>> = flow {
        Log.d("SourceGet", "start")
        emit(NetworkState.Loading)
        Log.d("SourceGet", "end")
        try {
            val bearToken = token.toToken()
            Log.d("Token", bearToken)
            val response = api.getRestaurants(
                bearToken,
                lowerLeftLon = lowerLeftLon,
                lowerLeftLat = lowerLeftLat,
                topRightLat = topRightLat,
                topRightLon = topRightLon,
                // 55, 37, 56, 38, //0
            )
            Log.d("SourceGet", response.items.toString())
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
        item: RestaurantModel,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<RestaurantModel>> = flow {
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

