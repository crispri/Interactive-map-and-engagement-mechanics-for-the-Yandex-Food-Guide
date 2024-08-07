package network.dto.request

import com.google.gson.annotations.SerializedName
import network.dto.response.RestaurantItemForJson

data class RestaurantListRequestForJson(
    @SerializedName("list") val list: List<RestaurantItemForJson>
)