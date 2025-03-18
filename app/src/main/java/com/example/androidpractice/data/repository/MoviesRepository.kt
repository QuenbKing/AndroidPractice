package com.example.androidpractice.data.repository

import com.example.androidpractice.data.mock.MoviesData
import com.example.androidpractice.domain.repository.IMoviesRepository

class MoviesRepository : IMoviesRepository {
    override fun getList() = MoviesData.movies

    override fun getById(id: Int) = MoviesData.movies.find { it.id == id }
}