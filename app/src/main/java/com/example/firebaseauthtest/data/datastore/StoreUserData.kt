package com.example.firebaseauthtest.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class StoreUserData(private val context: Context) {

    companion object {
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore("UserRole")
        val USER_DATA_KEY = stringPreferencesKey("user_role")
    }

    fun getUserRole() = context.datastore.data
        .map { preferences ->
            preferences[USER_DATA_KEY] ?: ""
        }

    suspend fun saveUserRole(userRole: String) {
        context.datastore.edit { preferences ->
            preferences[USER_DATA_KEY] = userRole
        }
    }
}