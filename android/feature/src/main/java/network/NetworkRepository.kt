package network

import android.util.Log
import network.datasource.INetworkDatasource
import network.util.NetworkState
import java.io.IOException
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val networkSource: INetworkDatasource,
) : INetworkRepository {

    override suspend fun getList() {
        networkSource.getTasks(
            "Asd", 55, 37, 56, 38, 0

        ).collect { state ->
            when (state) {
                is NetworkState.Failure ->
                    Log.d("NetworkException", "NetworkFailure")

                is NetworkState.Success -> Log.d("NetworkSuccess", "")
                else -> {}
            }
        }
    }
}