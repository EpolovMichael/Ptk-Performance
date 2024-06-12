package com.example.firebaseauthtest.data.firebase

import com.example.firebaseauthtest.data.AuthRepository
import com.example.firebaseauthtest.data.model.RealtimeFirebaseUser
import com.example.firebaseauthtest.data.util.RealtimeFirebaseResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealtimeFirebaseDB @Inject constructor(
    private val userReference: DatabaseReference,
    private val repository: AuthRepository
) {
    suspend fun getUsers(): Flow<RealtimeFirebaseResult<List<RealtimeFirebaseUser?>>> =
        callbackFlow {
            trySend(RealtimeFirebaseResult.Loading)
            userReference.keepSynced(true)
            val event = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue<RealtimeFirebaseUser>()
                    }
                    trySend(RealtimeFirebaseResult.Success(users))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(RealtimeFirebaseResult.Error(Throwable(error.message)))
                }
            }
            userReference.addValueEventListener(event)
            awaitClose { close() }
        }

    fun createUser(firebaseUser: RealtimeFirebaseUser): Flow<RealtimeFirebaseResult<String>> =
        callbackFlow {
            val userId = repository.getUser()
            val newUser = RealtimeFirebaseUser(
                id = userId,
                name = firebaseUser.name,
                role = firebaseUser.role,
                permission = firebaseUser.permission
            )
            userReference.child(userId).setValue(newUser)
                .addOnSuccessListener { trySend(RealtimeFirebaseResult.Success("User Added")) }
                .addOnFailureListener { trySend(RealtimeFirebaseResult.Error(Throwable(it.message))) }
            awaitClose { close() }
        }

    suspend fun updateUser(firebaseUser: RealtimeFirebaseUser): Flow<RealtimeFirebaseResult<String>> =
        callbackFlow {
            userReference.child(firebaseUser.id!!).updateChildren(firebaseUser.toMap())
                .addOnSuccessListener { trySend(RealtimeFirebaseResult.Success("User updated")) }
                .addOnFailureListener { trySend(RealtimeFirebaseResult.Error(Throwable(it.message))) }
            awaitClose { close() }
        }

    suspend fun deleteUser(firebaseUser: RealtimeFirebaseUser): Flow<RealtimeFirebaseResult<String>> =
        callbackFlow {
            userReference.child(firebaseUser.id!!).removeValue()
                .addOnSuccessListener { trySend(RealtimeFirebaseResult.Success("User deleted")) }
                .addOnFailureListener { trySend(RealtimeFirebaseResult.Error(Throwable(it.message))) }
            awaitClose { close() }
        }
}