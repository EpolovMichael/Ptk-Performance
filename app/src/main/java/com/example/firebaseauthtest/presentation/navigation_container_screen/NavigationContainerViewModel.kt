package com.example.firebaseauthtest.presentation.navigation_container_screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthtest.data.AuthRepository
import com.example.firebaseauthtest.data.Resource
import com.example.firebaseauthtest.data.UserPermissionsResult
import com.example.firebaseauthtest.data.datastore.StoreUserData
import com.example.firebaseauthtest.data.model.RealtimeFirebaseUser
import com.example.firebaseauthtest.data.repository.RealtimeFirebaseRepository
import com.example.firebaseauthtest.presentation.registration_screen.Discipline
import com.example.firebaseauthtest.presentation.registration_screen.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationContainerViewModel @Inject constructor(
    private val repository: RealtimeFirebaseRepository,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _topBarTitle = MutableStateFlow("")

    val topBarTitle = _topBarTitle.asStateFlow()

    fun setTopBarTitle(title: String){
        _topBarTitle.value = title
    }

    private val _selectedTheme = MutableStateFlow("")
    val selectedTheme = _selectedTheme.asStateFlow()

    fun selectTheme(theme: String) {
        _selectedTheme.value = theme
    }

    private val _selectedGroup = MutableStateFlow(Group(name = null, disciplines = emptyMap()))
    val selectedGroup = _selectedGroup.asStateFlow()

    fun selectGroup(group: Group) {
        _selectedGroup.value = group
    }

    private val _selectedDiscipline = MutableStateFlow(Discipline(name = ""))
    val selectedDiscipline = _selectedDiscipline.asStateFlow()

    fun selectDiscipline(discipline: Discipline) {
        _selectedDiscipline.value = discipline
    }

    private val _selectedDate = MutableStateFlow("")
    val selectedDate = _selectedDate.asStateFlow()

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    private val _userState = MutableStateFlow(RealtimeDBUserState())
    val userState: StateFlow<RealtimeDBUserState> = _userState.asStateFlow()

    init {
        checkUserRole()
        getUserPermissions()
    }

    fun checkUserRole() = viewModelScope.launch {
        authRepository.getUsersRole { resource ->
            when (resource) {
                is Resource.Success -> {
                    val userRole = resource.data
                    Log.d("MyTag", " user role 2 = $userRole")
                    userRole?.let { changeUserRole(it) }
                }

                is Resource.Error -> {
                    val errorMessage = resource.message
                    Log.d("MyTag", " user role 2 error = $errorMessage ")
                }

                is Resource.Loading -> {

                }
            }
        }
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

    fun convertMapToGroups(map: Map<String, Map<String, List<String>>>): List<Group> {
        val groups = mutableListOf<Group>()

        map.forEach { (groupName, disciplinesMap) ->
            val disciplines = disciplinesMap.mapKeys { Discipline(it.key) }
            val group = Group(groupName, disciplines)
            groups.add(group)
        }

        return groups
    }





    @Composable
    fun getUserRole(context: Context) =
        StoreUserData(context).getUserRole().collectAsState(initial = "")

    private val _userRole = MutableStateFlow("")
    val userRole = _userRole.asStateFlow()

    private val _listOfPermission =
        MutableStateFlow<Map<String, Map<String, List<String>>>>(mutableMapOf())
    val listOfPermission: StateFlow<Map<String, Map<String, List<String>>>> = _listOfPermission

    fun changeListOfPermission(permissions: Map<String, Map<String, List<String>>>) {
        _listOfPermission.value = permissions
    }

    fun changeUserRole(role: String) {
        _userRole.value = role
    }
}

data class RealtimeDBUserState(
    val data: List<RealtimeFirebaseUser?>? = null,
    val isLoading: Boolean = false,
    val errorMsg: String? = null
)