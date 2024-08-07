package network.dto.response

import com.google.gson.annotations.SerializedName

data class RestaurantResponseForJson(
    @SerializedName("status") val status: String,
    @SerializedName("element") val element: RestaurantItemForJson,
    @SerializedName("revision") val revision: Int
)
