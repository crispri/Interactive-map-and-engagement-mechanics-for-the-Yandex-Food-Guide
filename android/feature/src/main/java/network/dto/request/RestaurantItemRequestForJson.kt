package network.dto.request

import com.google.gson.annotations.SerializedName
import network.dto.response.RestaurantItemForJson

data class RestaurantItemRequestForJson(
    @SerializedName("element") val element: RestaurantItemForJson
)