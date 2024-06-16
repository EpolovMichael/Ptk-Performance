package com.example.firebaseauthtest.presentation.attendance_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthtest.data.AuthRepository
import com.example.firebaseauthtest.data.Resource
import com.example.firebaseauthtest.data.UserPermissionsResult
import com.example.firebaseauthtest.data.repository.RealtimeFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceGroupsViewModel @Inject constructor (
    private val repository: RealtimeFirebaseRepository,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
): ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private fun changeUserName(role: String) {
        _userName.value = role
    }

    private val _listOfPermission =
        MutableStateFlow<Map<String, Map<String, List<String>>>>(mutableMapOf())
    val listOfPermission: StateFlow<Map<String, Map<String, List<String>>>> = _listOfPermission

    fun changeListOfPermission(permissions: Map<String, Map<String, List<String>>>) {
        _listOfPermission.value = permissions
    }

    fun getUserPermissions() = viewModelScope.launch {
        authRepository.getUserPermissions().collect { result ->
            when (result) {
                is UserPermissionsResult.Success -> {
                    val userPermissions = result.userPermissions
                    changeListOfPermission(userPermissions as Map<String, Map<String, List<String>>>)
                    Log.d("MyTag", "getUserPermissions: $userPermissions")
                }

                is UserPermissionsResult.Error -> {
                    val errorMessage = result.errorMessage
                    Log.d("MyTag", "getUserPermissions error: $errorMessage")
                }

                is UserPermissionsResult.Loading -> {
                    val loadingMessage = result.loadingMessage
                    Log.d("MyTag", "getUserPermissions loading: $loadingMessage")
                }
            }
        }
    }
    suspend fun checkUserName() {
        authRepository.getUsersData { resource ->
            when (resource) {
                is Resource.Success -> {
                    val userName = resource.data
                    userName?.let { changeUserName(it) }
                }
                is Resource.Error -> {
                    val errorMessage = resource.message
                    Log.d("MyTag", "user role error = $errorMessage")
                }
                is Resource.Loading -> {

                }
            }
        }
    }
    fun logoutUser(onLogoutSuccess: () -> Unit, onLogoutFailure: (String) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.logoutUser()
            if (result is Resource.Success) {
                onLogoutSuccess()
            } else if (result is Resource.Error) {
                onLogoutFailure(result.message ?: "Ошибка выхода")
            }
        }
    }
}