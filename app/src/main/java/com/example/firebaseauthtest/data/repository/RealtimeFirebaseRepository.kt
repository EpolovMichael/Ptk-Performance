package com.example.firebaseauthtest.data.repository

import com.example.firebaseauthtest.data.model.RealtimeFirebaseUser
import com.example.firebaseauthtest.data.util.RealtimeFirebaseResult
import kotlinx.coroutines.flow.Flow

interface RealtimeFirebaseRepository {
    suspend fun createUser(user: RealtimeFirebaseUser): Flow<RealtimeFirebaseResult<String>>
    suspend fun getUser(): Flow<RealtimeFirebaseResult<List<RealtimeFirebaseUser?>>>
    suspend fun updateUser(user: RealtimeFirebaseUser): Flow<RealtimeFirebaseResult<String>>
    suspend fun deleteUser(user: RealtimeFirebaseUser): Flow<RealtimeFirebaseResult<String>>
}