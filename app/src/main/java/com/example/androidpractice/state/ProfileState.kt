package com.example.androidpractice.state

interface ProfileState {
    val fio: String
    val avatarUri: String
    val resumeUrl: String
    val position: String
    val email: String
    val isLoading: Boolean
    val error: String?
}