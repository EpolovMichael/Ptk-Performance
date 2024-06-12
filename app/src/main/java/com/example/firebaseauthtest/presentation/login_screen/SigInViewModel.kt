package com.example.firebaseauthtest.presentation.login_screen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthtest.data.AuthRepository
import com.example.firebaseauthtest.data.Resource
import com.example.firebaseauthtest.data.datastore.StoreUserData
import com.example.firebaseauthtest.data.repository.RealtimeFirebaseRepository
import com.example.firebaseauthtest.presentation.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: RealtimeFirebaseRepository,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _passwordVisibility = MutableStateFlow(true)
    val passwordVisibility = _passwordVisibility.asStateFlow()

    fun changPasswordVisibility() {
        _passwordVisibility.value = !_passwordVisibility.value
    }

    private val _userRole = MutableStateFlow("")
    val userRole = _userRole.asStateFlow()

    fun changeUserRole(role: String){
        _userRole.value = role
    }

    @Composable
    fun getUserRole(context: Context) =
        StoreUserData(context).getUserRole().collectAsState(initial = "")

    fun checkUserReg(): Boolean {
        Log.d("MyTag", "userRole ===== ${userRole.value}")
        return authRepository.checkUser()
    }

    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    fun loginUser(email: String, password: String, context: Context) = viewModelScope.launch {
        authRepository.loginUser(email, password, context).collect { result ->
            when (result) {
                is Resource.Success -> {
                    checkUserRoleAndSetState()
                }
                is Resource.Loading -> {
                    _signInState.send(SignInState(isLoading = true))
                }
                is Resource.Error -> {
                    _signInState.send(SignInState(isError = result.message))
                }
            }
        }
    }

    private suspend fun checkUserRole() {
        authRepository.getUsersRole { resource ->
            when (resource) {
                is Resource.Success -> {
                    val userRole = resource.data
                    userRole?.let { changeUserRole(it) }
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

    private fun checkUserRoleAndSetState() = viewModelScope.launch {
        checkUserRole()
        _signInState.send(SignInState(isSuccess = "Sign In Success"))
    }

    fun invokeCheckUserRole() = viewModelScope.launch {
        checkUserRole()
    }
}
