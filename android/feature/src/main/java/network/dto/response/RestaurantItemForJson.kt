package network.dto.response

import com.google.gson.annotations.SerializedName
import com.yandex.mapkit.geometry.Point
import okhttp3.Address
import java.util.UUID

data class RestaurantItemForJson(
    @SerializedName("id") val id: String,
    @SerializedName("coordinates") val coordinates: Point,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("address") val address: String,
    @SerializedName("is_approved") val isApproved: Boolean,
    @SerializedName("rating") val rating: Double,
    @SerializedName("price_lower_bound") val priceLowerBound: Int,
    @SerializedName("price_upper_bound") val priceUpperBound: Int,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("tags") val tags: List<String>,

)
