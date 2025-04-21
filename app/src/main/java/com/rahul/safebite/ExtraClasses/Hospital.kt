package com.rahul.safebite.ExtraClasses

data class Hospital(
    val name: String,
    var address: String,  // Marked `var` so we can update it dynamically
    val latitude: Double,
    val longitude: Double,
    val distance: Float
)
