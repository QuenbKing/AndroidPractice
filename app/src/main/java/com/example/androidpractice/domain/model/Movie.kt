package com.example.androidpractice.domain.model

data class Movie(
    val id: Int,
    val name: String,
    val rating: Rating,
    val description: String,
    val year: String,
    val poster: Poster,
    val genres: List<Genre>,
    val countries: List<Country>,
    val persons: List<Person>
)