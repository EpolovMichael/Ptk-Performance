package com.example.firebaseauthtest.presentation.attendance_screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.firebaseauthtest.presentation.navigation.Screens
import com.example.firebaseauthtest.presentation.navigation_container_screen.NavigationContainerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AttendanceGroupsView(
    navHostController: NavHostController,
    navController: NavController,
    viewModel: AttendanceGroupsViewModel = hiltViewModel(),
    navigationViewModel: NavigationContainerViewModel,
) {
    navigationViewModel.setTopBarTitle("Профиль")
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.checkUserName()
        viewModel.getUserPermissions()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                Text(
                    text = viewModel.userName.value,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 24.sp)
                )
            }
            item {
                ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Дисциплины:",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            items(viewModel.listOfPermission.value.values.flatMap { it.keys }
                .distinct()) { discipline ->
                Text(
                    text = discipline,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            item {
                ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Доступ к группам:",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            items(viewModel.listOfPermission.value.toList()) { (group, disciplines) ->
                ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
                    Text(
                        text = group,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                disciplines.forEach { (discipline, topics) ->
                    Text(
                        text = "- $discipline",
                        modifier = Modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W800
                        )
                    )

                    topics.forEach { topic ->
                        Text(
                            text = "-- $topic",
                            modifier = Modifier.padding(start = 32.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = {
                viewModel.logoutUser(
                    onLogoutSuccess = {
                        Toast.makeText(context, "Вы успешно вышли из профиля", Toast.LENGTH_SHORT)
                            .show()
                    },
                    onLogoutFailure = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
                coroutineScope.launch {
                    withContext(Dispatchers.Main) {
                        delay(1000)
                        navHostController.navigate(Screens.SignInScreen.route)
                    }
                }
            }
        ) {
            Text(text = "Выйти из профиля", color = Color.White)
        }
    }
}