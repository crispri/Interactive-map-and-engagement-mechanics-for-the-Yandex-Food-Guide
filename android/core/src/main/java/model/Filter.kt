package model


/**
 * Data class representing a filter with specific criteria.
 */
data class Filter(
    val property: String,
    val value: List<Any>,
    val operator: String,
    val isSelected: Boolean
)