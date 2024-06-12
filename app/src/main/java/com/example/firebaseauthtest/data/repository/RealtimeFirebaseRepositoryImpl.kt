package com.example.firebaseauthtest.data.repository

import com.example.firebaseauthtest.data.firebase.RealtimeFirebaseDB
import com.example.firebaseauthtest.data.model.RealtimeFirebaseUser
import com.example.firebaseauthtest.data.util.RealtimeFirebaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RealtimeFirebaseRepositoryImpl @Inject constructor(
    private val firebase: RealtimeFirebaseDB
) : RealtimeFirebaseRepository {
    override suspend fun createUser(user: RealtimeFirebaseUser): Flow<RealtimeFirebaseResult<String>> {
        return firebase.createUser(user)
    }

    override suspend fun getUser(): Flow<RealtimeFirebaseResult<List<RealtimeFirebaseUser?>>> {
        return firebase.getUsers()
    }

    override suspend fun updateUser(user: RealtimeFirebaseUser): Flow<RealtimeFirebaseResult<String>> {
        return firebase.updateUser(user)
    }

    override suspend fun deleteUser(user: RealtimeFirebaseUser): Flow<RealtimeFirebaseResult<String>> {
        return firebase.deleteUser(user)
    }
}