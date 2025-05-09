package com.example.androidpractice.mapper

import com.example.androidpractice.domain.model.Genre
import com.example.androidpractice.domain.model.Movie
import com.example.androidpractice.domain.model.MovieShort
import com.example.androidpractice.domain.model.MovieType
import com.example.androidpractice.domain.model.Poster
import com.example.androidpractice.domain.model.Rating
import com.example.androidpractice.web.response.MovieFullResponse

class MovieMapper {
    fun toDomain(response: MovieFullResponse): Movie? {
        if (response.id == null
            || response.name.isNullOrBlank()
        ) return null

        return Movie(
            id = response.id.or(0),
            name = response.name,
            type = MovieType.getMovieTypeByCode(response.type),
            rating = response.rating ?: Rating(0.0, 0.0, 0.0),
            description = response.description.orEmpty(),
            year = response.year.orEmpty(),
            poster = response.poster ?: Poster("", ""),
            genres = response.genres?.map { genre -> Genre.getGenreByName(genre.name) }.orEmpty(),
            countries = response.countries?.map { country -> country.name }.orEmpty(),
            persons = response.persons ?: emptyList()
        )
    }

    private fun toSmallDomain(response: MovieFullResponse): MovieShort {
        return MovieShort(
            id = response.id ?: 0,
            name = response.name.orEmpty(),
            poster = response.poster ?: Poster("", ""),
            year = response.year.orEmpty(),
            rating = response.rating ?: Rating(1.0, 1.0, 1.0),
            type = response.type,
            genres = response.genres?.map { genre -> Genre.getGenreByName(genre.name) }.orEmpty(),
        )
    }

    fun toDomainList(response: List<MovieFullResponse>) =
        response.map { movie -> toSmallDomain(movie) }

}