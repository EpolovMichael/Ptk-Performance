package com.example.firebaseauthtest.presentation.navigation_container_screen

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationContainerScreen(
    navController: NavController,
    viewModel: NavigationContainerViewModel,
) {

    ContainerNavGraph(viewModel = viewModel)

    // TODO: Перенести scaffold и передавать в контейнер экланы с нижней навикацией
    // TODO: Начать с переноса toolBar, взять только title и пустые экраны, чтобы проверить навигацию в пределах контейнера
    // цель - устранить перерисовки элемента и перерисовывать только title
}