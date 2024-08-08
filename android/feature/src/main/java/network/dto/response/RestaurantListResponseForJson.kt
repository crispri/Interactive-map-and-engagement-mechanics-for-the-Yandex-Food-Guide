package network.dto.response

import com.google.gson.annotations.SerializedName

data class RestaurantListResponseForJson(
    @SerializedName("items") val items: List<RestaurantItemForJson>,
)
