package com.example.firebaseauthtest.presentation.login_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.firebaseauthtest.R
import com.example.firebaseauthtest.presentation.navigation.Screens
import com.example.firebaseauthtest.ui.theme.Blue40
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signInState.collectAsState(initial = null)

    val passwordVisibility by viewModel.passwordVisibility.collectAsState()

    val userRole = viewModel.getUserRole(context).value
    val status = remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.titleLarge) {
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = "Вход",
            )
        }

        ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
            Text(
                text = stringResource(R.string.enter_your_credential_s_to_login),
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.tertiary,
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.enter_email))
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.enter_password))
                },
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.changPasswordVisibility()
                    }) {
                        Icon(
                            painter = if (passwordVisibility) painterResource(id = R.drawable.hide_password_img)
                            else painterResource(id = R.drawable.show_password_img),
                            contentDescription = "visibility",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None
            )

            Button(
                onClick = {
                    if (viewModel.checkUserReg()) {
                        viewModel.checkUserRole()
                        navController.navigate(Screens.ContentScreen.route)
                    } else {
                        scope.launch {
                            viewModel.loginUser(email, password, context)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue40,
                    contentColor = MaterialTheme.colorScheme.inversePrimary // Что это?
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.bodyMedium) {
                    Text(
                        text = stringResource(R.string.sign_in),
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            /*Text(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable {
                        navController.navigate(Screens.SignUpScreen.route)
                    },
                text = stringResource(R.string.not_registered_yet_create_an_account)
            )*/
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            if (state.value?.isLoading == true) {
                CircularProgressIndicator()
            }
        }

        LaunchedEffect(key1 = state.value?.isSuccess) {
            scope.launch {
                if (state.value?.isSuccess?.isNotEmpty() == true) {
                    val success = state.value?.isSuccess
                    Toast.makeText(context, "${success}", Toast.LENGTH_LONG).show()
                    navController.navigate(Screens.ContentScreen.route)
                }
            }
        }

        LaunchedEffect(key1 = state.value?.isError) {
            scope.launch {
                if (state.value?.isError?.isNotEmpty() == true) {
                    val error = state.value?.isError
                    Toast.makeText(context, "${error}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}