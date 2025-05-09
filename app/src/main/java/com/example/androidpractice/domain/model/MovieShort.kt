package com.example.androidpractice.domain.model

data class MovieShort(
    val id: Int,
    val name: String,
    val poster: Poster,
    val year: String,
    val rating: Rating,
    val type: String,
    val genres: List<Genre>
)
