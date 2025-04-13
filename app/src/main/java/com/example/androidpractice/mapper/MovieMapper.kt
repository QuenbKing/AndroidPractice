package com.example.androidpractice.mapper

import com.example.androidpractice.domain.model.Movie
import com.example.androidpractice.domain.model.MovieShort
import com.example.androidpractice.domain.model.Poster
import com.example.androidpractice.domain.model.Rating
import com.example.androidpractice.web.response.MovieFullResponse
import com.example.androidpractice.web.response.MoviesSearchResponse

class MovieMapper {
    fun toDomain(response: MovieFullResponse): Movie? {
        if (response.id == null || response.name == null) return null

        return Movie(
            id = response.id.or(0),
            name = response.name,
            rating = response.rating ?: Rating(0.0, 0.0, 0.0),
            description = response.description.orEmpty(),
            year = response.year.orEmpty(),
            poster = response.poster ?: Poster("", ""),
            genres = response.genres ?: emptyList(),
            countries = response.countries ?: emptyList(),
            persons = response.persons ?: emptyList()
        )
    }

    private fun toSmallDomain(response: MovieFullResponse): MovieShort {
        return MovieShort(
            id = response.id ?: 0,
            name = response.name.orEmpty(),
            poster = response.poster ?: Poster("", ""),
            year = response.year.orEmpty(),
            rating = response.rating ?: Rating(1.0, 1.0, 1.0)
        )
    }

    fun toDomainList(response: MoviesSearchResponse) =
        response.search?.map { movie -> toSmallDomain(movie) }.orEmpty()

}