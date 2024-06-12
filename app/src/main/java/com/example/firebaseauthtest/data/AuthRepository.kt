package com.example.firebaseauthtest.data

import android.content.Context
import com.example.firebaseauthtest.presentation.registration_screen.Group
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(email: String, password: String, context: Context): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun checkUser(): Boolean
    fun getUsersRole(callback: (Resource<String>) -> Unit)
    suspend fun getUserPermissions(): Flow<UserPermissionsResult>
    suspend fun setUserRole(userId: String, role: String): Resource<Unit>
    fun getUser(): String
}