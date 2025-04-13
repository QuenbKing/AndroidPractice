package com.example.androidpractice.web.response

import com.google.gson.annotations.SerializedName

class MoviesSearchResponse(
    @SerializedName("docs")
    val search: List<MovieFullResponse>?
)