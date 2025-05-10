package com.example.androidpractice.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidpractice.data.dao.ProfileDao
import com.example.androidpractice.data.entity.ProfileDbEntity

@Database(entities = [ProfileDbEntity::class], version = 1)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}