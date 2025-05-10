package com.example.androidpractice.data.repository

import com.example.androidpractice.data.database.ProfileDatabase
import com.example.androidpractice.data.entity.ProfileDbEntity
import com.example.androidpractice.domain.model.Profile
import com.example.androidpractice.domain.repository.IProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileRepository(
    private val db: ProfileDatabase
) : IProfileRepository {
    override suspend fun getProfile(): Profile? {
        return db.profileDao().getUser()?.toDomain()
    }

    override suspend fun setProfile(profile: Profile): Profile {
        val existingProfile = db.profileDao().getUser()
        if (existingProfile == null) {
            val profileEntity = ProfileDbEntity(
                fio = profile.fio,
                avatarUri = profile.avatarUri,
                resumeUrl = profile.resumeUrl,
                position = profile.position,
                email = profile.email,
                favoriteClassTime = profile.favoriteClassTime
            )
            db.profileDao().insert(profileEntity)
        } else {
            db.profileDao().update(
                fio = profile.fio,
                avatarUri = profile.avatarUri,
                resumeUrl = profile.resumeUrl,
                position = profile.position,
                email = profile.email,
                favoriteClassTime = profile.favoriteClassTime
            )
        }
        return profile
    }

    override suspend fun observeProfile(): Flow<Profile> {
        return db.profileDao().getUser()?.toDomain()?.let { profile ->
            kotlinx.coroutines.flow.flowOf(profile)
        } ?: kotlinx.coroutines.flow.flowOf(Profile())
    }

    private fun ProfileDbEntity.toDomain(): Profile {
        return Profile(
            fio = fio ?: "",
            avatarUri = avatarUri ?: "",
            resumeUrl = resumeUrl ?: "",
            position = position ?: "",
            email = email ?: "",
            favoriteClassTime = favoriteClassTime ?: ""
        )
    }
}