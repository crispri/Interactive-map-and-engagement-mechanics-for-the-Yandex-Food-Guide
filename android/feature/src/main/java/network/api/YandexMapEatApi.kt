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
import retrofit2.http.Path

interface YandexMapEatApi {

    @POST("guide/v1/restaurants")
    suspend fun getRestaurants(
        @Header("Authorization") token: String,
        @Header("Accept") accept: String = "application/json",
        @Header("Content-Type") contentType: String = "application/json",
        @Body requestBody: RequestBody,
    ): RestaurantListResponseForJson

    @GET("guide/v1/restaurants/{id}")
    suspend fun getRestaurantById(
        @Header("Authorization") token: String,
        @Header("Accept") accept: String = "application/json",
        @Header("Content-Type") contentType: String = "application/json",
        @Path("id") id: String,
    ): RestaurantItemForJson

    @POST("guide/v1/selections")
    suspend fun getCollections(
        @Header("Authorization") token: String,
        @Header("Accept") accept: String = "application/json",
        @Header("Content-Type") contentType: String = "application/json",
        @Body requestBody: RequestBodyCollection,
    ): CollectionListResponseForJson

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

data class FilterForJson(
    @SerializedName("property") val property: String,
    @SerializedName("value") val value: List<Any>,
    @SerializedName("operator") val operator: String
)