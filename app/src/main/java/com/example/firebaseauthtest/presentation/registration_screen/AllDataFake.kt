package com.example.firebaseauthtest.presentation.registration_screen

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Group(
    val name: String? = null,
    val disciplines: Map<Discipline, List<String>> = emptyMap()
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "disciplines" to disciplines.mapKeys { it.key.name }
        )
    }
}

@IgnoreExtraProperties
data class Discipline(
    val name: String = ""
)


data class Teacher(
    val name: String? = null,
    val login: String? = null,
    val password: String? = null,
    val groups: List<Group>? = null
)

data class Student(
    val id: String? = null,
    val firstName: String? = null,
    val lastname: String? = null,
    val patronymic: String? = null,
    val groupNumber: String? = null,
)