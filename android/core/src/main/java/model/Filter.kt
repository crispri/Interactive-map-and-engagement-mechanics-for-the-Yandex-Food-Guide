package model

data class Filter(
    val property: String,
    val value: List<Any>,
    val operator: String,
    val isSelected: Boolean
)