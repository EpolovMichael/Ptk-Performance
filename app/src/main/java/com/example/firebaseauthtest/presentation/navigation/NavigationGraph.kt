package com.example.firebaseauthtest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.firebaseauthtest.presentation.navigation_container_screen.NavigationContainerScreen
import com.example.firebaseauthtest.presentation.login_screen.SignInScreen
import com.example.firebaseauthtest.presentation.marks_screen.MarksScreen
import com.example.firebaseauthtest.presentation.navigation_container_screen.NavigationContainerViewModel
import com.example.firebaseauthtest.presentation.registration_screen.SignUpScreen
import com.example.firebaseauthtest.presentation.splash_screen.SplashScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationGraph() {
    val viewModel = hiltViewModel<NavigationContainerViewModel>()
    val navController = rememberNavController()

    //viewModel.checkUserRole()
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route
    ){
        composable(route = Screens.SplashScreen.route){
            SplashScreen(navController)
        }
        composable(route = Screens.SignInScreen.route){
            SignInScreen(navController)
        }
        composable(route = Screens.SignUpScreen.route){
            SignUpScreen(navController)
        }
        composable(route = Screens.ContentScreen.route){
            NavigationContainerScreen(navController, viewModel)
        }
    }
}