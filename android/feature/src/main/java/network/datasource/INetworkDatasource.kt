package network.datasource

import kotlinx.coroutines.flow.Flow
import network.util.NetworkState
import presintation.model.RestaurantModel

interface INetworkDatasource {
    //    fun getTasks(token: String) : List<RestaurantModel>
    fun getTasks(
//        accept: String,
        token: String,
//        contentType: String,
        lowerLeftLat: Int,
        lowerLeftLon: Int,
        topRightLat: Int,
        topRightLon: Int,
        maxCount: Int
    ): Flow<NetworkState<List<RestaurantModel>>>

    fun updateTask(
        task: RestaurantModel,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<RestaurantModel>>

}