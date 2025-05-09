package com.example.androidpractice.domain.repository

import com.example.androidpractice.domain.model.Movie
import com.example.androidpractice.domain.model.MovieShort
import com.example.androidpractice.domain.model.MovieType

interface IMoviesRepository {
    suspend fun getList(
        query: String,
        filterTypes: Set<MovieType>? = null
    ): List<MovieShort>

    suspend fun getById(id: Int): Movie?

    suspend fun saveFavorite(movie: MovieShort)
    suspend fun getFavorites(): List<MovieShort>
}