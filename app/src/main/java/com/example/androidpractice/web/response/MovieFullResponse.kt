package com.example.androidpractice.web.response

import androidx.annotation.Keep
import com.example.androidpractice.domain.model.Person
import com.example.androidpractice.domain.model.Poster
import com.example.androidpractice.domain.model.Rating

@Keep
class MovieFullResponse(
    val id: Int?,
    val name: String?,
    val rating: Rating?,
    val description: String?,
    val type: String,
    val year: String?,
    val poster: Poster?,
    val genres: List<GenreDto>?,
    val countries: List<CountryDto>?,
    val persons: List<Person>?
) {

    class GenreDto(
        val name: String
    )

    class CountryDto(
        val name: String
    )
}