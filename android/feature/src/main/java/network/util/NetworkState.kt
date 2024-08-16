package network.util

sealed class NetworkState <out T> {
    data object Loading : NetworkState<Nothing>()
    data class Success<T>(val data: T) : NetworkState<T>()
    data class Failure(val cause: Throwable): NetworkState<Nothing>()

}
