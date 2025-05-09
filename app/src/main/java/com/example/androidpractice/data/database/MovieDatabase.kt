package com.example.androidpractice.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidpractice.data.dao.MovieDao
import com.example.androidpractice.data.entity.MovieDbEntity

@Database(entities = [MovieDbEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}