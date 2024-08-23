package network.api

import com.google.gson.annotations.SerializedName
import model.Coordinates
import network.dto.response.CollectionListResponseForJson
import network.dto.response.RestaurantItemForJson
import network.dto.response.RestaurantListResponseForJson
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Defines the API endpoints for interacting with the Yandex Map Eat service.
 *
 * This interface provides methods for performing various network operations related to restaurants and collections,
 * using Retrofit for network requests.
 */
interface YandexMapEatApi {

    @POST("guide/v1/restaurants")
    suspend fun getRestaurants(
        @Header("Cookie") token: String,
        @Header("Accept") accept: String = "application/json",
        @Header("Content-Type") contentType: String = "application/json",
        @Body requestBody: RequestBody,
    ): RestaurantListResponseForJson


    @GET("guide/v1/restaurants/{id}")
    suspend fun getRestaurantById(
        @Header("Cookie") token: String,
        @Header("Accept") accept: String = "application/json",
        @Header("Content-Type") contentType: String = "application/json",
        @Path("id") id: String,
    ): RestaurantItemForJson


    @POST("guide/v1/selections")
    suspend fun getCollections(
        @Header("Cookie") token: String,
        @Header("Accept") accept: String = "application/json",
        @Header("Content-Type") contentType: String = "application/json",
        @Body requestBody: RequestBodyCollection,
    ): CollectionListResponseForJson


    @PUT("guide/v1/collection/{id}")
    suspend fun addItemToCollection(
        @Header("Cookie") token: String,
        @Header("Accept") accept: String = "application/json",
        @Header("Content-Type") contentType: String = "application/json",
        @Path("id") id: String,
        @Body requestBody: RequestBodyAddItemCollection,
    )

}


data class RequestBody(
    @SerializedName("lower_left_corner") val lowerLeftCorner: Coordinates,
    @SerializedName("top_right_corner") val topRightCorner: Coordinates,
    @SerializedName("only_collections") val onlyCollections: Boolean,
    @SerializedName("filters") val filters: List<FilterForJson> = listOf()
)


data class RequestBodyCollection(
    @SerializedName( "return_collections" ) val  returnCollections : Boolean,
)


data class RequestBodyAddItemCollection(
    @SerializedName( "restaurant_id" ) val  restaurantId : String,
)


data class FilterForJson(
    @SerializedName("property") val property: String,
    @SerializedName("value") val value: List<Any>,
    @SerializedName("operator") val operator: String
)