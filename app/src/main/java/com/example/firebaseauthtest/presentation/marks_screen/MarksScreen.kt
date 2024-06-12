package com.example.firebaseauthtest.presentation.marks_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.firebaseauthtest.presentation.navigation_container_screen.NavigationContainerViewModel
import com.example.firebaseauthtest.presentation.registration_screen.Student
import com.example.firebaseauthtest.presentation.student_screen.StudentScreenViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MarksScreen(
    navController: NavController,
    navigationContainerViewModel: NavigationContainerViewModel,
    viewModel: MarksScreenViewModel = hiltViewModel(),
) {
    //val viewModel = hiltViewModel<NavigationContainerViewModel>()

    val listState = remember {
        mutableStateOf<List<Student>>(emptyList())
    }

    val studentScreenViewModel = hiltViewModel<StudentScreenViewModel>()

    LaunchedEffect(key1 = studentScreenViewModel) {
        // Заменить переменную на выбранного студента, но только если нажал студент, у препода не будет такой возможности
        val groupNumber = navigationContainerViewModel.selectedGroup.value.name
        studentScreenViewModel.getStudentsFromFireStore(groupNumber.toString()) { students ->
            listState.value = students
        }
        Log.d("MyTag", "0101010101010101 - ${listState.value}")
    }

    val context = LocalContext.current

    val userRole = viewModel.getUserRole(context).value

    //Toast.makeText(content, "User data: ${viewModel.userState.value}", Toast.LENGTH_SHORT).show()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
                Text(
                    modifier = Modifier.weight(2f),
                    text = "Дата"
                )
                Text(
                    modifier = Modifier.weight(5f),
                    text = "Дисциплина"
                )
                Text(
                    modifier = Modifier.weight(2f),
                    text = "Оценка"
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(256.dp),
            content = {
                items(listState.value) { student ->
                    MarkItem()
                }
            }
        )
    }
}