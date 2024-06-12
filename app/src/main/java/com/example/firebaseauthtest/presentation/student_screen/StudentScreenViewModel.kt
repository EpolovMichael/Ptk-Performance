package com.example.firebaseauthtest.presentation.student_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauthtest.data.local.Mark
import com.example.firebaseauthtest.data.local.MarksRepository
import com.example.firebaseauthtest.presentation.registration_screen.Student
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentScreenViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val roomRepository: MarksRepository,
) : ViewModel() {

    private val _marks = MutableStateFlow<List<Mark>>(emptyList())
    val marks: StateFlow<List<Mark>> = _marks.asStateFlow()

    init {
        viewModelScope.launch {
            roomRepository.allMarks.collect { listOfMarks ->
                _marks.value = listOfMarks
            }
        }
        Log.d("MyTag", "7777777777777777777777 ${marks.value}")
    }

    fun upsertStudentMark(mark: Mark) = viewModelScope.launch {
        roomRepository.insert(mark)
    }
    /*
    fun deleteStudentMark(mark: Mark) = viewModelScope.launch {
        roomRepository.delete(mark)
    }*/

    fun markStudentAsAbsent(
        student: Student,
        date: String,
        disciplineName: String,
        groupNumber: String?,
        theme: String
    ) = CoroutineScope(Dispatchers.IO).launch {
            val markData = mapOf(
                "date" to date,
                "attendance" to "-",
                "disciplineName" to disciplineName,
                "groupNumber" to groupNumber,
                "studentNumber" to student.firstName.toString(),
                "theme" to theme
            )

            fireStore.collection("attendance")
                .add(markData)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        "MyTag",
                        "addMarksToFireStore: Mark added with ID ${documentReference.id}"
                    )
                }
                .addOnFailureListener { exception ->
                    Log.d(
                        "MyTag",
                        "addMarksToFireStore: Failed to add mark. Error: ${exception.message}"
                    )
                }
        }

    fun markStudentAsPresent(
        student: Student,
        date: String,
        disciplineName: String,
        groupNumber: String?,
        theme: String
    ) = CoroutineScope(Dispatchers.IO).launch {
            val markData = mapOf(
                "date" to date,
                "attendance" to "+",
                "disciplineName" to disciplineName,
                "groupNumber" to groupNumber,
                "studentNumber" to student.firstName.toString(),
                "theme" to theme
            )

            fireStore.collection("attendance")
                .add(markData)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        "MyTag",
                        "addMarksToFireStore: Mark added with ID ${documentReference.id}"
                    )
                }
                .addOnFailureListener { exception ->
                    Log.d(
                        "MyTag",
                        "addMarksToFireStore: Failed to add mark. Error: ${exception.message}"
                    )
                }
        }

    fun addMarksToFireStore(mark: Mark, student: Student) = CoroutineScope(Dispatchers.IO).launch {
        val markData = mapOf(
            "date" to mark.data.toString(),
            "mark" to mark.mark.toString(),
            "disciplineName" to mark.disciplineName.toString(),
            "groupNumber" to mark.groupNumber.toString(),
            "studentNumber" to student.firstName.toString(),
            "theme" to mark.theme.toString()
        )

        fireStore.collection("marks")
            .add(markData)
            .addOnSuccessListener { documentReference ->
                Log.d("MyTag", "addMarksToFireStore: Mark added with ID ${documentReference.id}")
            }
            .addOnFailureListener { exception ->
                Log.d(
                    "MyTag",
                    "addMarksToFireStore: Failed to add mark. Error: ${exception.message}"
                )
            }
    }


    fun getStudentsFromFireStore(
        groupNumber: String,
        data: (List<Student>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        fireStore.collection("students")
            .document(groupNumber)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val students = documentSnapshot.data?.values
                    val studentList = mutableListOf<Student>()
                    students?.forEach { studentData ->
                        if (studentData is List<*>) {
                            studentData.forEach { student ->
                                try {
                                    if (student is HashMap<*, *>) {
                                        val id = student["id"] as String?
                                        val firstName = student["firstName"] as String?
                                        val lastname = student["lastname"] as String?
                                        val patronymic = student["patronymic"] as String?
                                        val groupNumber = student["groupNumber"] as String?
                                        val student = Student(
                                            id,
                                            firstName,
                                            lastname,
                                            patronymic,
                                            groupNumber
                                        )
                                        studentList.add(student)
                                    }
                                } catch (e: Exception) {
                                    Log.d("MyTag", "getStudentsFromFireStoreError: ${e.message}")
                                }
                            }
                        }
                    }
                    data(studentList)
                } else {
                    Log.d("MyTag", "getStudentsFromFireStore: Document doesn't exist")
                    data(emptyList())
                }
            }
            .addOnFailureListener { exception ->
                Log.d(
                    "MyTag",
                    "getStudentsFromFireStore: Failed to retrieve students. Error: ${exception.message}"
                )
                data(emptyList())
            }
    }

    fun addStudentsToFireStore(
        students: Map<String?, List<Student>>
        //context: Context,
    ) = CoroutineScope(Dispatchers.IO).launch {
        students.forEach { groupNumber, listOfStudents ->
            val mapOfStudents = listOfStudents.groupBy { it.id }
            groupNumber?.let {
                fireStore.collection("students")
                    .document(it)
                    .set(mapOfStudents)
                    .addOnSuccessListener { dataReference ->
                        Log.d("MyTag", "addStudentsToFireStore: ${dataReference.toString()}")
                    }
                    .addOnFailureListener { exception ->
                        Log.d("MyTag", "addStudentsToFireStore: ${exception.message}")
                    }
            }
        }
    }

    fun deleteStudentFromFireStore(
        uid: String,
        context: Context,
    ) {
        fireStore.collection("students")
            .document(uid)
            .delete()
            .addOnSuccessListener {
                Log.d("MyTag", "deleteStudentFromFireStore: student $uid deleted")
            }
            .addOnFailureListener { e ->
                Log.d("MyTag", "deleteStudentFromFireStore: error: ${e.message}")
            }
    }
}