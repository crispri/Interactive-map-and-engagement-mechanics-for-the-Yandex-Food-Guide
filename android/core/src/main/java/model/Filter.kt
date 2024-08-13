package model

data class Filter(
    val property: String,
    val value: List<Double>,
    val operator: String
)