package network.api

import com.google.gson.annotations.SerializedName
import network.dto.request.RestaurantItemRequestForJson
import network.dto.response.RestaurantListResponseForJson
import network.dto.response.RestaurantResponseForJson
import presintation.model.Coordinates
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface YandexMapEatApi {
    @POST("guide/v1/restaurants")
    suspend fun getRestaurants(
        @Header("Authorization") token: String,
        @Query("lower_left_lat") lowerLeftLat: Int,
        @Query("lower_left_lon") lowerLeftLon: Int,
        @Query("top_right_lat") topRightLat: Int,
        @Query("top_right_lon") topRightLon: Int,
        //@Query("max_count") maxCount: Int
    ): RestaurantListResponseForJson

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
    @SerializedName("max_count") val maxCount: Int
)