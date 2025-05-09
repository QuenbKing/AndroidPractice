package com.example.androidpractice.domain.model

data class Movie(
    val id: Int,
    val name: String,
    val type: MovieType,
    val rating: Rating,
    val description: String,
    val year: String = "",
    val poster: Poster,
    val genres: List<Genre>,
    val countries: List<String>,
    val persons: List<Person>
)