package com.example.firebaseauthtest.presentation.registration_screen

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthtest.data.AuthRepository
import com.example.firebaseauthtest.data.Resource
import com.example.firebaseauthtest.data.model.RealtimeFirebaseUser
import com.example.firebaseauthtest.data.repository.RealtimeFirebaseRepository
import com.example.firebaseauthtest.data.util.RealtimeFirebaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: RealtimeFirebaseRepository,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _passwordVisibility = MutableStateFlow(true)
    val passwordVisibility = _passwordVisibility.asStateFlow()

    fun changPasswordVisibility() {
        _passwordVisibility.value = !_passwordVisibility.value
    }

    val _signUpState = Channel<SignUpState>()
    val signUpState = _signUpState.receiveAsFlow()

    fun registerUser(email: String, password: String) = viewModelScope.launch {
        authRepository.registerUser(email, password).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _signUpState.send(SignUpState(isSuccess = "Sign In Success"))
                }

                is Resource.Loading -> {
                    _signUpState.send(SignUpState(isLoading = true))
                }

                is Resource.Error -> {
                    _signUpState.send(SignUpState(isError = result.message))
                }
            }
        }
    }

    fun createUser(user: RealtimeFirebaseUser) = viewModelScope.launch {
        repository.createUser(user).collect { result ->
            when (result) {
                is RealtimeFirebaseResult.Success -> {
                    Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                }

                is RealtimeFirebaseResult.Error -> {
                    Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                }

                is RealtimeFirebaseResult.Loading -> {
                }
            }
        }
    }

    fun groupToMap(groups: List<Group>): Map<String, Map<String, List<String>>> {
        val resultMap = mutableMapOf<String, Map<String, List<String>>>()
        for (group in groups) {
            val groupName = group.name
            if (groupName != null) {
                val disciplinesMap = group.disciplines.mapKeys { it.key.name }.mapValues { it.value }
                resultMap[groupName] = disciplinesMap
            }
        }
        return resultMap
    }


    suspend fun registerListOfTeachers(teachers: List<Teacher>) {
        for (teacher in teachers) {
            teacher.login?.let {
                teacher.password?.let { it1 ->
                    authRepository.registerUser(it, it1).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                val userId = authRepository.getUser()
                                val permissionMap = teacher.groups?.let { groupToMap(it) }
                                val firebaseUser = RealtimeFirebaseUser(
                                    id = userId,
                                    name = teacher.name,
                                    role = "prepod",
                                    permission = permissionMap
                                )
                                createUser(firebaseUser)
                            }

                            is Resource.Loading -> {
                                // Handle loading state if needed
                            }

                            is Resource.Error -> {
                                // Handle error state if needed
                            }
                        }
                    }
                }
            }
        }
    }

}