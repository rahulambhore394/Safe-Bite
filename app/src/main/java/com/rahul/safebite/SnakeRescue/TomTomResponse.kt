package com.rahul.safebite.SnakeRescue

data class TomTomResponse(
    val results: List<Result>
)

data class Result(
    val poi: Poi?,
    val address: Address?
)

data class Poi(
    val name: String?,
    val phone: List<String>?
)

data class Address(
    val municipality: String?
)
