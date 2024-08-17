package model

data class Filter(
    val property: String,
    val value: List<String>,
    val operator: String
)