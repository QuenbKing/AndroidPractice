package com.example.androidpractice.data.repository

import com.example.androidpractice.data.database.MovieDatabase
import com.example.androidpractice.data.entity.MovieDbEntity
import com.example.androidpractice.domain.model.Genre
import com.example.androidpractice.domain.model.MovieShort
import com.example.androidpractice.domain.model.MovieType
import com.example.androidpractice.domain.model.Poster
import com.example.androidpractice.domain.model.Rating
import com.example.androidpractice.domain.repository.IMoviesRepository
import com.example.androidpractice.mapper.MovieMapper
import com.example.androidpractice.web.MovieApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepository(
    private val api: MovieApi,
    private val mapper: MovieMapper,
    private val db: MovieDatabase
) : IMoviesRepository {
    override suspend fun getList(query: String, filterTypes: Set<MovieType>?) =
        withContext(Dispatchers.IO) {
            val response = api.searchMovies(query = query).search
                .orEmpty()
                .filter { movie ->
                    filterTypes.isNullOrEmpty()
                            || filterTypes.contains(MovieType.getMovieTypeByCode(movie.type))
                }
            mapper.toDomainList(response)
        }

    override suspend fun getById(id: Int) =
        withContext(Dispatchers.IO) {
            val response = api.getMovie(id)
            mapper.toDomain(response)
        }

    override suspend fun saveFavorite(movie: MovieShort) =
        withContext(Dispatchers.IO) {
            db.movieDao().insert(
                MovieDbEntity(
                    name = movie.name,
                    type = movie.type,
                    genre = movie.genres.getOrNull(0)?.name ?: "",
                    url = movie.poster.previewUrl,
                    year = movie.year,
                    rating = movie.rating.kinopoisk
                )
            )
        }

    override suspend fun getFavorites() =
        withContext(Dispatchers.IO) {
            db.movieDao().getAll().map {
                MovieShort(
                    id = it.id ?: 0,
                    name = it.name ?: "",
                    poster = Poster(it.url ?: "", it.url ?: ""),
                    year = it.year ?: "",
                    rating = Rating(it.rating ?: 1.0, 1.0, 1.0),
                    genres = listOf(Genre.valueOf(it.genre.toString())),
                    type = it.type ?: ""
                )
            }
        }
}