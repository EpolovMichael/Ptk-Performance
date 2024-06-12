package com.example.firebaseauthtest.presentation.splash_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.firebaseauthtest.presentation.login_screen.SignInViewModel
import com.example.firebaseauthtest.presentation.navigation.Screens
import com.example.firebaseauthtest.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        if (viewModel.checkUserReg()) {
            viewModel.invokeCheckUserRole()
            delay(2000)
            navController.navigate(Screens.ContentScreen.route)
        } else {
            delay(2000)
            navController.navigate(Screens.SignInScreen.route)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimationView()
    }
}

@Composable
fun LottieAnimationView() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.fillMaxSize()
    )
}
