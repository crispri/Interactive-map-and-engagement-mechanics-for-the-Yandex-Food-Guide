package network.dto.response

import com.google.gson.annotations.SerializedName

data class RestaurantListResponseForJson(
    //@SerializedName("status") val status: String,
    @SerializedName("items") val items: List<RestaurantItemForJson>,
    //@SerializedName("revision") val revision: Int
)
