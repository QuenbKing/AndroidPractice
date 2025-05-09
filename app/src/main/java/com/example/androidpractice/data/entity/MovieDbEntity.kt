package com.example.androidpractice.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MovieDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "movieName") val name: String?,
    @ColumnInfo(name = "movieType") val type: String?,
    @ColumnInfo(name = "movieGenres") val genre: String?,
    @ColumnInfo(name = "movieUrl") val url: String?,
    @ColumnInfo(name = "movieYear") val year: String?,
    @ColumnInfo(name = "movieRating") val rating: Double?
)