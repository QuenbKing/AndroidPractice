package com.example.androidpractice.domain.repository

import com.example.androidpractice.domain.model.Movie
import com.example.androidpractice.domain.model.MovieShort

interface IMoviesRepository {
    suspend fun getList(query: String): List<MovieShort>

    suspend fun getById(id: Int): Movie?
}