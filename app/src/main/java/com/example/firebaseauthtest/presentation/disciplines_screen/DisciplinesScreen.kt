package com.example.firebaseauthtest.presentation.disciplines_screen

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
import androidx.compose.runtime.getValue
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@SuppressLint("StateFlowValueCalledInComposition")
fun DisciplinesScreen(
    navController: NavHostController,
    viewModel: NavigationContainerViewModel,
) {
    viewModel.setTopBarTitle("Выбор дисциплины")

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
        val permissions = viewModel.listOfPermission.collectAsState()
        val selectedGroup = viewModel.selectedGroup

        Log.d("MyTag", "11111111111111111 ---------- 0 --- ${permissions.value}")

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isLoading.value) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    selectedGroup.value.disciplines?.let {
                        ListOfDisciplines(
                            disciplines = it.keys.toList(),
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ListOfDisciplines(
    disciplines: List<Discipline>,
    viewModel: NavigationContainerViewModel,
    navController: NavController
) {
    LazyColumn {
        items(disciplines) { discipline ->
            discipline.name?.let {
                SelectOptionCard(value = it) {
                    viewModel.selectDiscipline(discipline)
                    navController.navigate(Screens.Themes.route)
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ThemeScreen(
    navController: NavHostController,
    viewModel: NavigationContainerViewModel,
) {
    viewModel.setTopBarTitle("Выбор темы")

    val context = LocalContext.current
    val isConnected = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(true) }

    val connectivityReceiver = rememberUpdatedState(
        ConnectivityReceiver { isNetworkAvailable ->
            isConnected.value = isNetworkAvailable
            if (isNetworkAvailable) {
                isLoading.value = true
                isLoading.value = true
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

    LaunchedEffect(isConnected.value) {
        if (isConnected.value) {
            isLoading.value = true
            delay(1000)
            isLoading.value = false
        }
    }

    if (!isConnected.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Нет подключения к интернету", modifier = Modifier.padding(16.dp))
        }
    } else {
        val permissions = viewModel.listOfPermission.collectAsState()
        val selectedGroup = viewModel.selectedGroup

        Log.d("MyTag", "11111111111111111 ---------- 0 --- ${permissions.value}")

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isLoading.value) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    selectedGroup.value.disciplines?.let {
                        ListOfThemes(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ListOfThemes(
    viewModel: NavigationContainerViewModel,
    navController: NavController
) {
    val selectedGroup by viewModel.selectedGroup.collectAsState()
    val disciplines = selectedGroup.disciplines
    val dis = viewModel.selectedDiscipline
    val selectedDisciplines = selectedGroup.disciplines.filter { it.key.name == dis.value.name }

    LazyColumn {
        selectedDisciplines.forEach { (discipline, themeList) ->
            items(themeList) { theme ->
                SelectOptionCard(value = theme) {
                    viewModel.selectTheme(theme)
                    viewModel.selectDiscipline(discipline)
                    navController.navigate(Screens.Students.route)
                }
            }
        }
    }
}
