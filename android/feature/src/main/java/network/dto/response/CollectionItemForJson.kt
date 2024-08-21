package network.dto.response

import com.google.gson.annotations.SerializedName
import model.Coordinates

data class CollectionItemForJson(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("is_public") val isPublic: Int? = 0,
    @SerializedName("picture") val picture: String?,
    @SerializedName("pre_created_collection_name") val preCreatedCollectionName: String?,
)
