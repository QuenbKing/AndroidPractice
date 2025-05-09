package com.example.androidpractice.state

import com.example.androidpractice.domain.model.MovieShort
import com.example.androidpractice.domain.model.MovieType

interface MoviesListState {
    val items: List<MovieShort>
    val query: String
    val isEmpty: Boolean
    var hasBadge: Boolean
    var showTypesDialog: Boolean
    val typesVariants: Set<MovieType>
    var selectedTypes: Set<MovieType>
    val isLoading: Boolean
    val error: String?
}