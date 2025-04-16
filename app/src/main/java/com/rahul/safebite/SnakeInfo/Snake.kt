package com.rahul.safebite.SnakeInfo

data class Snake(
    val id: Int,
    val scientificName: String,
    val commonName: String?,
    val imageUrl: String
)

data class TaxaResult(
    val id: Int,
    val name: String,
    val preferred_common_name: String?,
    val default_photo: DefaultPhoto?
)

data class DefaultPhoto(
    val medium_url: String
)

data class iNaturalistResponse(
    val results: List<TaxaResult>
)