package com.example.androidpractice.state

import com.example.androidpractice.domain.model.MovieShort

interface MoviesListState {
    val items: List<MovieShort>
    val query: String
    val isEmpty: Boolean
    val isLoading: Boolean
    val error: String?
}