package com.example.firebaseauthtest.data

import android.content.Context
import android.util.Log
import com.example.firebaseauthtest.data.datastore.StoreUserData
import com.example.firebaseauthtest.presentation.registration_screen.Discipline
import com.example.firebaseauthtest.presentation.registration_screen.Group
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dataBase: DatabaseReference
) : AuthRepository {

    override fun loginUser(
        email: String,
        password: String,
        context: Context
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { StoreUserData(context).saveUserRole(it.uid) }
            emit(Resource.Success(result))
        }.catch { exception ->
            Log.e("AuthRepository", "Login error: ${exception.message}")
            emit(Resource.Error(exception.message.toString()))
        }
    }

    override fun registerUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch { exception ->
            Log.e("AuthRepository", "Registration error: ${exception.message}")
            emit(Resource.Error(exception.message.toString()))
        }
    }

    override fun getUsersData(callback: (Resource<String>) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userRoleRef = dataBase.child(currentUser.uid).child("name")
            userRoleRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val role = snapshot.getValue(String::class.java)
                    if (role != null) {
                        Log.d("MyLog", "onDataChange12312: $role")
                        callback(Resource.Success(role))
                    } else {
                        callback(Resource.Error("User role not found"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(Resource.Error(error.message))
                }
            })
        } else {
            callback(Resource.Error("User not authenticated"))
        }
    }

    override fun getUsersRole(callback: (Resource<String>) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userRoleRef = dataBase.child(currentUser.uid).child("role")
            userRoleRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val role = snapshot.getValue(String::class.java)
                    if (role != null) {
                        Log.d("MyLog", "onDataChange12312: $role")
                        callback(Resource.Success(role))
                    } else {
                        callback(Resource.Error("User role not found"))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(Resource.Error(error.message))
                }
            })
        } else {
            callback(Resource.Error("User not authenticated"))
        }
    }

    override suspend fun getUserPermissions(): Flow<UserPermissionsResult> {
        return flow {
            emit(UserPermissionsResult.Loading())
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                try {
                    val permissionValue = suspendCoroutine<Map<String, List<String>>?> { continuation ->
                        dataBase.child(currentUser.uid).child("permission").get()
                            .addOnSuccessListener { dataSnapshot ->
                                val permissionValue = dataSnapshot.value as? Map<String, List<String>>?
                                continuation.resume(permissionValue)
                            }
                            .addOnFailureListener { exception ->
                                continuation.resumeWithException(exception)
                            }
                    }
                    Log.d("MyTag", "Serialized value $permissionValue")
                    emit(UserPermissionsResult.Success(permissionValue))
                } catch (exception: Exception) {
                    Log.e("AuthRepository", "Permission error: ${exception.message}")
                    emit(UserPermissionsResult.Error(exception.message))
                }
            } else {
                emit(UserPermissionsResult.Error("User not authenticated"))
            }
        }.catch { exception ->
            Log.e("AuthRepository", "Flow error: ${exception.message}")
            emit(UserPermissionsResult.Error(exception.message))
        }
    }

    override suspend fun logoutUser(): Resource<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            firebaseAuth.signOut()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Logout error: ${e.message}")
            Resource.Error(e.message ?: "Failed to logout user")
        }
    }

    override suspend fun setUserRole(userId: String, role: String): Resource<Unit> {
        return try {
            val userRoleRef = dataBase.child("users").child(userId)
            userRoleRef.setValue(role).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Set user role error: ${e.message}")
            Resource.Error(e.message ?: "Failed to set user role")
        }
    }

    override fun getUser(): String {
        return firebaseAuth.currentUser?.uid ?: ""
    }

    override fun checkUser(): Boolean {
        return firebaseAuth.currentUser != null
    }
}
