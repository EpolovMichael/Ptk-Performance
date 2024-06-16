package com.example.firebaseauthtest.presentation.groups_screen

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.firebaseauthtest.data.util.ConnectivityReceiver
import com.example.firebaseauthtest.data.util.NetworkUtils
import com.example.firebaseauthtest.presentation.custom_components.SelectOptionCard
import com.example.firebaseauthtest.presentation.navigation.Screens
import com.example.firebaseauthtest.presentation.navigation_container_screen.NavigationContainerViewModel
import com.example.firebaseauthtest.presentation.registration_screen.Discipline
import com.example.firebaseauthtest.presentation.registration_screen.Group
import com.example.firebaseauthtest.presentation.registration_screen.Student
import com.example.firebaseauthtest.presentation.student_screen.StudentScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GroupsScreen(
    navController: NavHostController,
    viewModel: NavigationContainerViewModel,
    studentScreenViewModel: StudentScreenViewModel,
) {
    viewModel.setTopBarTitle("Выбор группы")

    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val isConnected = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(true) }

    // Ensure the receiver instance remains stable across recompositions
    val receiver = rememberUpdatedState(
        ConnectivityReceiver { isNetworkAvailable ->
            isConnected.value = isNetworkAvailable
            if (isNetworkAvailable) {
                isLoading.value = true
                coroutine.launch {
                    delay(1000)
                    isLoading.value = false
                }
            }
        }
    )

    DisposableEffect(Unit) {
        val connectivityReceiver = receiver.value
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(connectivityReceiver, intentFilter)

        onDispose {
            context.unregisterReceiver(connectivityReceiver)
        }
    }

    if (!isConnected.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Нет подключения к интернету", modifier = Modifier.padding(16.dp))
        }
    } else {
        /*val listOfStudents2 = listOf(
            Student(
                "1",
                "Абакумова Анжелика Александровна",
                lastname = null,
                patronymic = null,
                groupNumber = "0901"
            ),
            Student(
                "2",
                "Благодыр Светислав Вячеславович",
                lastname = null,
                patronymic = null,
                groupNumber = "0902"
            ),
            Student(
                "3",
                "Вавиленко Георгий Григорьевич",
                lastname = null,
                patronymic = null,
                groupNumber = "0901"
            ),
            Student(
                "4",
                "Ваганов Егор Денисович",
                lastname = null,
                patronymic = null,
                groupNumber = "0902"
            ),
            Student(
                "5",
                "Варламов Валерий Валентинович",
                lastname = null,
                patronymic = null,
                groupNumber = "0902"
            ),
            Student(
                "6",
                "Галанцев Никита Александрович",
                lastname = null,
                patronymic = null,
                groupNumber = "0901"
            ),
            Student(
                "7",
                "Героев Владислав Николаевич",
                lastname = null,
                patronymic = null,
                groupNumber = "0902"
            ),
            Student(
                "8",
                "Журавель Михаил Александрович",
                lastname = null,
                patronymic = null,
                groupNumber = "0901"
            ),
            Student(
                "9",
                "Иванов Алексей Сергеевич",
                lastname = null,
                patronymic = null,
                groupNumber = "0902"
            ),
            Student(
                "10",
                "Корнев Мирослав Сергеевич",
                lastname = null,
                patronymic = null,
                groupNumber = "0901"
            ),
            Student(
                "12",
                "Орлов Григорий Вячеславович",
                lastname = null,
                patronymic = null,
                groupNumber = "0902"
            ),
            Student(
                "13",
                "Остроумов Николай Владиславович",
                lastname = null,
                patronymic = null,
                groupNumber = "0901"
            ),
            Student(
                "14",
                "Раунио Никита Дмитриевич",
                lastname = null,
                patronymic = null,
                groupNumber = "0902"
            ),
            Student(
                "15",
                "Румянцев Андрей Олегович",
                lastname = null,
                patronymic = null,
                groupNumber = "0901"
            ),
            Student(
                "16",
                "Свольский Елисей Сергеевич",
                lastname = null,
                patronymic = null,
                groupNumber = "0902"
            ),
            Student(
                "17",
                "Сизов Андрей Анатольевич",
                lastname = null,
                patronymic = null,
                groupNumber = "0901"
            ),
            Student(
                "18",
                "Федоров Семен Сергеевич",
                lastname = null,
                patronymic = null,
                groupNumber = "0902"
            ),
            Student(
                "19",
                "Фомин Глеб Антонович",
                lastname = null,
                patronymic = null,
                groupNumber = "0901"
            ),
            Student(
                "20",
                "Фрейвальд Злата Павловна",
                lastname = null,
                patronymic = null,
                groupNumber = "0902"
            ),
            Student(
                "22",
                "Яковлев Максим Андреевич",
                lastname = null,
                patronymic = null,
                groupNumber = "0901"
            ),
            Student(
                "23",
                "Красин Олег Игоревич",
                lastname = null,
                patronymic = null,
                groupNumber = "0902"
            ),
            Student(
                "24",
                "Тюряков Александр Евгеньевич",
                lastname = null,
                patronymic = null,
                groupNumber = "0901"
            ),
        )*/

        //val mapOfStudentsByGroupNumber = listOfStudents2.groupBy { it.groupNumber }

        val listOfStudents = remember {
            mutableStateOf<List<Student>>(emptyList())
        }

        val permissions = viewModel.listOfPermission.collectAsState()

        val groupsList = viewModel.convertMapToGroups(permissions.value)
        val discipline = viewModel.selectedDiscipline

        Log.d("MyTag", "11111111111111111 ---------- 0 --- ${groupsList}")

        LaunchedEffect(key1 = studentScreenViewModel) {
            delay(2000)
            isLoading.value = false
            /*val groupNumber = viewModel.selectedGroup.value.name
            studentScreenViewModel.getStudentsFromFireStore(groupNumber.toString()) { students ->
                listOfStudents.value = students
            }*/
            //studentScreenViewModel.addStudentsToFireStore(mapOfStudentsByGroupNumber)
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isLoading.value) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    ListOfGroups(
                        dis = discipline.value,
                        groups = groupsList,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun ListOfGroups(
    dis: Discipline,
    groups: List<Group>,
    viewModel: NavigationContainerViewModel,
    navController: NavController
) {
    LazyColumn {
        items(groups) { group ->
            group.name?.let {
                Log.d(
                    "MyTag",
                    "77766665555 ---------- 0 --- ${group.disciplines.filter { it.key.name == dis.name }}"
                )
                SelectOptionCard(value = it, onClickListener = {
                    viewModel.selectGroup(group)
                    navController.navigate(Screens.Disciplines.route)
                })
            }
        }
    }
}
