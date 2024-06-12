package com.example.firebaseauthtest.presentation.navigation_container_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.firebaseauthtest.presentation.attendance_screen.AttendanceGroupsView
import com.example.firebaseauthtest.presentation.date_picker_screen.DatePickerScreen
import com.example.firebaseauthtest.presentation.disciplines_screen.DisciplinesScreen
import com.example.firebaseauthtest.presentation.disciplines_screen.ThemeScreen
import com.example.firebaseauthtest.presentation.groups_screen.GroupsScreen
import com.example.firebaseauthtest.presentation.marks_screen.MarksScreen
import com.example.firebaseauthtest.presentation.navigation.BottomNavigationBar
import com.example.firebaseauthtest.presentation.navigation.Screens
import com.example.firebaseauthtest.presentation.student_screen.StudentScreenViewModel
import com.example.firebaseauthtest.presentation.student_screen.StudentsScreen
import com.example.firebaseauthtest.ui.theme.Blue40

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContainerNavGraph(
    viewModel: NavigationContainerViewModel
) {
    val navController = rememberNavController()
    var topBarTitle by remember { mutableStateOf("") }
    val viewModell = hiltViewModel<StudentScreenViewModel>()

    LaunchedEffect(viewModel.topBarTitle) {
        viewModel.topBarTitle.collect { newTitle ->
            topBarTitle = newTitle
        }
    }

    val startDestination = if (viewModel.userRole.value == "prepod") {
        Log.d("MyTag", "ContainerNavGraph user role --------- ${viewModel.userRole.value} ")
        Screens.StartPrepodRoute.route
    } else {
        Screens.StartPrepodRoute.route
        //Screens.StartStudentRoute.route
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Blue40
                ),
                title = {
                    ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
                        Text(text = topBarTitle, color = Color.White)
                    }
                })
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        content = { innerPadding ->
            //viewModel.setPaddingValues(innerPadding = innerPadding)
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    navigation(
                        startDestination = Screens.DateTimePicker.route,
                        route = Screens.StartPrepodRoute.route
                    ) {
                        composable(route = Screens.DateTimePicker.route) {
                            DatePickerScreen(navController, viewModel)
                        }
                        composable(route = Screens.Groups.route) {
                            GroupsScreen(navController, viewModel, viewModell)
                        }
                        composable(route = Screens.Disciplines.route) {
                            DisciplinesScreen(navController, viewModel)
                        }
                        composable(route = Screens.Themes.route){
                            ThemeScreen(navController, viewModel)
                        }
                        composable(route = Screens.Students.route) {
                            StudentsScreen(navController, viewModel = viewModell, navigationViewModel = viewModel)
                        }
                        composable(route = Screens.Marks.route) {
                            MarksScreen(navController, viewModel)
                        }
                    }
                    navigation(
                        startDestination = Screens.AttendanceCourses.route,
                        route = Screens.StartStudentRoute.route
                    ) {
                        composable(route = Screens.AttendanceCourses.route) {
                            AttendanceGroupsView(navController)
                        }
                    }
                }
            }
        }
    )
}
