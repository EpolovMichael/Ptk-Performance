package com.example.firebaseauthtest.presentation.registration_screen

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signUpState.collectAsState(initial = null)

    val passwordVisibility by viewModel.passwordVisibility.collectAsState()


    val listOfTeachers = listOf(
        Teacher(
            name = "Воробьев М.А.",
            login = "vorobev76@gmail.com",
            password = "qweqwssdf",
            groups = listOf(
                Group(
                    name = "0901",
                    disciplines = mapOf(
                        Discipline(name = "Основы философии") to listOf(
                            "Введение в философию",
                            "Античная философия",
                            "Средневековая философия",
                            "Философия Нового времени",
                            "Немецкая классическая философия"
                        )
                    )
                ),
                Group(
                    name = "0902",
                    disciplines = mapOf(
                        Discipline(name = "Основы философии") to listOf(
                            "Введение в философию",
                            "Античная философия",
                            "Средневековая философия",
                            "Философия Нового времени",
                            "Немецкая классическая философия"
                        )
                    )
                )
            )
        ),
        Teacher(
            name = "Лесков Р.А.",
            login = "liskov@gmail.com",
            password = "sdfsdfsdf",
            groups = listOf(
                Group(
                    name = "0901",
                    disciplines = mapOf(
                        Discipline(name = "Физ культ") to listOf(
                            "Основы здорового образа жизни",
                            "Общая физическая подготовка",
                            "Гимнастика",
                            "Лёгкая атлетика",
                            "Игровые виды спорта",
                        )
                    )
                ),
                Group(
                    name = "0902",
                    disciplines = mapOf(
                        Discipline(name = "Физ культ") to listOf(
                            "Основы здорового образа жизни",
                            "Общая физическая подготовка",
                            "Гимнастика",
                            "Лёгкая атлетика",
                            "Игровые виды спорта",
                        )
                    )
                )
            )
        ),
        Teacher(
            name = "Цымболюк Л.Н.",
            login = "cimboluk@gmail.com",
            password = "gfhherreasd",
            groups = listOf(
                Group(
                    name = "0901",
                    disciplines = mapOf(
                        Discipline(name = "Операционные системы") to listOf(
                            "Введение в операционные системы",
                            "Архитектура операционных систем",
                            "Управление процессами",
                            "Управление памятью",
                            "Файловые системы",
                        ),
                        Discipline(name = "Основы баз данных") to listOf(
                            "Введение в базы данных",
                            "Модели данных",
                            "Реляционные базы данных",
                            "Язык SQL",
                            "Запросы и манипуляции с данными",
                        ),
                    )
                ),
                Group(
                    name = "0902",
                    disciplines = mapOf(
                        Discipline(name = "Операционные системы") to listOf(
                            "Введение в операционные системы",
                            "Архитектура операционных систем",
                            "Управление процессами",
                            "Управление памятью",
                            "Файловые системы",
                        ),
                        Discipline(name = "Основы баз данных") to listOf(
                            "Введение в базы данных",
                            "Модели данных",
                            "Реляционные базы данных",
                            "Язык SQL",
                            "Запросы и манипуляции с данными",
                        ),
                    )
                )
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.titleLarge) {
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = stringResource(R.string.create_account),
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp,
            )
        }

        ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
            Text(
                text = stringResource(R.string.enter_your_credential_s_to_register),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.tertiary
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
                    scope.launch {
                        //viewModel.getAll()
                        //viewModel.registerUser(email, password)
                        viewModel.registerListOfTeachers(listOfTeachers)
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
                        text = stringResource(R.string.sign_up),
                        color = MaterialTheme.colorScheme.inversePrimary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                if (state.value?.isLoading == true) {
                    CircularProgressIndicator()
                }
            }
            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable {
                        navController.navigate(Screens.SignInScreen.route)
                    },
                text = stringResource(R.string.already_have_an_account_sign_in),
            )
        }
    }

    LaunchedEffect(key1 = state.value?.isSuccess) {
        scope.launch {
            if (state.value?.isSuccess?.isNotEmpty() == true) {
                val success = state.value?.isSuccess
                Toast.makeText(context, "${success}", Toast.LENGTH_LONG).show()
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