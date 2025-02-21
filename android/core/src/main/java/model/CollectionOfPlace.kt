package model

/**
 ** A data class representing the user's personal collection.
 */
data class CollectionOfPlace(
    val id: String,
    val name: String,
    val description: String,
    val isPublic: Int? = 0,
    val picture: String? = "",
    val link: String = "",
    val preCreatedCollectionName: String?,
)

