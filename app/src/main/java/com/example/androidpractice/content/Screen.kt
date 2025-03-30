package com.example.androidpractice.content

sealed class Screen(val route: String) {
    object List : Screen("list")
    object Details : Screen("details/{movieId}")
}