package com.example.firebaseauthtest.data

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

sealed class UserPermissionsResult {
    data class Loading(val loadingMessage: String = "Loading...") : UserPermissionsResult()
    data class Success(val userPermissions: Map<String, List<String>>?) : UserPermissionsResult()
    data class Error(val errorMessage: String?) : UserPermissionsResult()
}
