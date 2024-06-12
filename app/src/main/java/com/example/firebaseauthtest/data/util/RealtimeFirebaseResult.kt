package com.example.firebaseauthtest.data.util

sealed class RealtimeFirebaseResult<out R> {
    data class Success<out T>(val data: T) : RealtimeFirebaseResult<T>()
    data class Error(val exception: Throwable) : RealtimeFirebaseResult<Nothing>()
    data object Loading : RealtimeFirebaseResult<Nothing>()
}