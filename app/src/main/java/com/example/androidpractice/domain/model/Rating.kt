package com.example.androidpractice.domain.model

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("kp")
    val kinopoisk: Double,
    val imdb: Double,
    val rottenTomatoes: Double,
) {
}