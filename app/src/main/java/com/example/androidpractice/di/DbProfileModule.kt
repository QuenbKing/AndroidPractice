package com.example.androidpractice.di

import android.content.Context
import androidx.room.Room
import com.example.androidpractice.data.database.ProfileDatabase
import org.koin.dsl.module

val dbProfileModule = module {
    single { DatabaseProfileBuilder.getInstance(get()) }
}

object DatabaseProfileBuilder {
    private var INSTANCE: ProfileDatabase? = null

    fun getInstance(context: Context): ProfileDatabase {
        if (INSTANCE == null) {
            synchronized(ProfileDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            ProfileDatabase::class.java,
            "profiles"
        ).build()
}