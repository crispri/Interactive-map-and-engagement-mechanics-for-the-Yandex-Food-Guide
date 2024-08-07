package network

interface INetworkRepository {
    suspend fun getList()
}