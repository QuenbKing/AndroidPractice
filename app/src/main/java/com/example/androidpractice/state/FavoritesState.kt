package com.example.androidpractice.state

import com.example.androidpractice.domain.model.MovieShort

data class FavoritesState(
    val items: List<MovieShort> = emptyList()
)