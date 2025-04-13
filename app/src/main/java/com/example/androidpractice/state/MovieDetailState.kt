package com.example.androidpractice.state

import com.example.androidpractice.domain.model.Movie
import com.example.androidpractice.domain.model.MovieShort

interface MovieDetailState {
    val movie: Movie?
    val rating: Float
    val isRatingVisible: Boolean
    val isLoading: Boolean
    val error: String?
    val related: List<MovieShort>
}