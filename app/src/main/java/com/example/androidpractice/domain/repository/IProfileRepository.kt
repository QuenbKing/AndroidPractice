package com.example.androidpractice.domain.repository

import com.example.androidpractice.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface IProfileRepository {
    suspend fun getProfile(): Profile?
    suspend fun setProfile(profile: Profile): Profile
    suspend fun observeProfile(): Flow<Profile>
}