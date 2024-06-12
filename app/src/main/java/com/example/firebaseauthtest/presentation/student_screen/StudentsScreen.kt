package com.example.firebaseauthtest.presentation.student_screen

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.firebaseauthtest.data.local.Mark
import com.example.firebaseauthtest.data.util.ConnectivityReceiver
import com.example.firebaseauthtest.presentation.custom_components.CustomDialog
import com.example.firebaseauthtest.presentation.navigation_container_screen.NavigationContainerViewModel
import com.example.firebaseauthtest.presentation.registration_screen.Student
import com.example.firebaseauthtest.ui.theme.Blue40
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition", "RememberReturnType")
@Composable
fun SwipeToDismissStudentItem(
    student: Student,
    score: Double,
    onAbsent: () -> Unit,
    onAttend: () -> Unit,
    onClick: () -> Unit,
    showConfirmationDialog: MutableState<Boolean>,
    confirmationText: MutableState<String>,
    confirmationAction: MutableState<() -> Unit>,
    modifier: Modifier = Modifier
) {
    val background = MaterialTheme.colorScheme.background
    val coroutineScope = rememberCoroutineScope()
    val backgroundColor = remember { mutableStateOf(background) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            when (state) {
                SwipeToDismissBoxValue.EndToStart -> {
                    confirmationText.value = "Отметить отсутствие студента ${student.firstName}?"
                    confirmationAction.value = {
                        coroutineScope.launch {
                            onAbsent()
                            backgroundColor.value = Color.Red
                        }
                    }
                    showConfirmationDialog.value = true
                    false
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    confirmationText.value = "Отметить присутствие студента ${student.firstName}?"
                    confirmationAction.value = {
                        coroutineScope.launch {
                            onAttend()
                            backgroundColor.value = Color.Green
                        }
                    }
                    showConfirmationDialog.value = true
                    false
                }
                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                targetValue = backgroundColor.value,
                label = "Changing color"
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
            )
        },
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(56.dp)
                .clickable {
                    onClick()
                },
            colors = CardDefaults.cardColors(
                containerColor = Blue40,
                contentColor = Color.White
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(4f)
                                .padding(4.dp),
                            text = student.firstName ?: "",
                            textAlign = TextAlign.Left
                        )
                        Text(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .weight(1f),
                            text = score.toString(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudentsScreen(
    navController: NavHostController,
    navigationViewModel: NavigationContainerViewModel,
    viewModel: StudentScreenViewModel,
) {
    navigationViewModel.setTopBarTitle("Студенты группы ${navigationViewModel.selectedGroup.value.name}")
    val listOfStudents = remember { mutableStateOf<List<Student>>(emptyList()) }
    val scores = remember { mutableStateMapOf<String, Double>() }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isConnected = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(true) }

    val connectivityReceiver = rememberUpdatedState(
        ConnectivityReceiver { isNetworkAvailable ->
            isConnected.value = isNetworkAvailable
            if (isNetworkAvailable) {
                isLoading.value = true
                coroutineScope.launch {
                    val groupNumber = navigationViewModel.selectedGroup.value.name
                    viewModel.getStudentsFromFireStore(groupNumber.toString()) { students ->
                        listOfStudents.value = students
                        students.forEach { student ->
                            scores[student.id ?: ""] = 0.0 // Default score
                        }
                    }
                    delay(1000)
                    isLoading.value = false
                }
            }
        }
    )

    DisposableEffect(Unit) {
        val receiver = connectivityReceiver.value
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(receiver, intentFilter)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    if (!isConnected.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Нет подключения к интернету", modifier = Modifier.padding(16.dp))
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isLoading.value) {
                CircularProgressIndicator()
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    val showCustomDialog = remember { mutableStateOf(false) }
                    val dialogText = remember { mutableStateOf("") }
                    val selectedStudent = remember { mutableStateOf(Student()) }

                    if (showCustomDialog.value) {
                        CustomDialog(
                            selectedObject = selectedStudent.value,
                            value = dialogText.value,
                            setShowDialog = { showCustomDialog.value = it },
                            setValue = { newValue ->
                                dialogText.value = newValue
                                val mark = Mark(
                                    data = navigationViewModel.selectedDate.value,
                                    mark = dialogText.value,
                                    disciplineName = navigationViewModel.selectedDiscipline.value.name,
                                    groupNumber = navigationViewModel.selectedGroup.value.name,
                                    studentNumber = selectedStudent.value.id?.toInt(),
                                    theme = navigationViewModel.selectedTheme.value
                                )
                                viewModel.addMarksToFireStore(mark, selectedStudent.value)
                                selectedStudent.value.id?.let { id ->
                                    scores[id] = newValue.toDoubleOrNull() ?: 0.0
                                }
                            }
                        )
                    }

                    val showConfirmationDialog = remember { mutableStateOf(false) }
                    val confirmationAction = remember { mutableStateOf<() -> Unit>({}) }
                    val confirmationText = remember { mutableStateOf("") }

                    if (showConfirmationDialog.value) {
                        ConfirmationDialog(
                            text = confirmationText.value,
                            onConfirm = {
                                confirmationAction.value()
                                showConfirmationDialog.value = false
                            },
                            onDismiss = { showConfirmationDialog.value = false }
                        )
                    }

                    LazyColumn {
                        items(
                            items = listOfStudents.value,
                            key = { it.id ?: "" }
                        ) { student ->
                            SwipeToDismissStudentItem(
                                student = student,
                                score = scores[student.id ?: ""] ?: 0.0,
                                onAbsent = {
                                    viewModel.markStudentAsAbsent(
                                        student = student,
                                        date = navigationViewModel.selectedDate.value,
                                        disciplineName = navigationViewModel.selectedDiscipline.value.name,
                                        groupNumber = navigationViewModel.selectedGroup.value.name,
                                        theme = navigationViewModel.selectedTheme.value
                                    )
                                },
                                onAttend = {
                                    viewModel.markStudentAsPresent(
                                        student = student,
                                        date = navigationViewModel.selectedDate.value,
                                        disciplineName = navigationViewModel.selectedDiscipline.value.name,
                                        groupNumber = navigationViewModel.selectedGroup.value.name,
                                        theme = navigationViewModel.selectedTheme.value
                                    )
                                },
                                onClick = {
                                    dialogText.value = scores[student.id ?: ""].toString()
                                    selectedStudent.value = student
                                    showCustomDialog.value = true
                                },
                                showConfirmationDialog = showConfirmationDialog,
                                confirmationText = confirmationText,
                                confirmationAction = confirmationAction,
                                modifier = Modifier.animateItemPlacement(tween(200))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Подтверждение действия") },
        text = { Text(text) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Подтвердить")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отменить")
            }
        }
    )
}
