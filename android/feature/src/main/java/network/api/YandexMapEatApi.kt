package network.api

import com.google.gson.annotations.SerializedName
import model.Coordinates
import network.dto.request.RestaurantItemRequestForJson
import network.dto.response.RestaurantItemForJson
import network.dto.response.RestaurantListResponseForJson
import network.dto.response.RestaurantResponseForJson
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @PUT("guide/v1/restaurants/{id}")
    suspend fun putTask(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") header: Int,
        @Path("id") id: String,
        @Body body: RestaurantItemRequestForJson
    ): RestaurantResponseForJson
}

data class RequestBody(
    @SerializedName("lower_left_corner") val lowerLeftCorner: Coordinates,
    @SerializedName("top_right_corner") val topRightCorner: Coordinates,
    // @SerializedName("max_count") val maxCount: Int
)