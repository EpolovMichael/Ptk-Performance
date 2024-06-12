package com.example.firebaseauthtest.data.model

import com.google.firebase.database.Exclude

data class RealtimeFirebaseUser(
    val id: String? = null,
    val name: String? = null,
    val role: String? = null,
    val permission: Map<String, Map<String, List<String>>>? = null,
){
    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "name" to name,
            "role" to role,
            "permission" to permission
        )
    }
}

