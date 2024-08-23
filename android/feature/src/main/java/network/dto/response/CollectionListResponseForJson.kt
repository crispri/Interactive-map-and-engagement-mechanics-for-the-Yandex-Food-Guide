package network.dto.response

import com.google.gson.annotations.SerializedName

data class CollectionListResponseForJson(
    @SerializedName("items") val items: List<CollectionItemForJson>,
)
