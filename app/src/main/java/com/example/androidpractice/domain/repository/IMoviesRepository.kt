package com.example.androidpractice.domain.repository

import com.example.androidpractice.domain.model.Movie

interface IMoviesRepository {
    fun getList(): List<Movie>

    fun getById(id: Int): Movie?
}