package com.example.androidpractice.web.response

import androidx.annotation.Keep
import com.example.androidpractice.domain.model.Country
import com.example.androidpractice.domain.model.Genre
import com.example.androidpractice.domain.model.Person
import com.example.androidpractice.domain.model.Poster
import com.example.androidpractice.domain.model.Rating

@Keep
class MovieFullResponse(
    val id: Int?,
    val name: String?,
    val rating: Rating?,
    val description: String?,
    val year: String?,
    val poster: Poster?,
    val genres: List<Genre>?,
    val countries: List<Country>?,
    val persons: List<Person>?
)