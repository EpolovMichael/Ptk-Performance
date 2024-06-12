package com.example.firebaseauthtest.presentation.date_picker_screen

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.firebaseauthtest.R
import com.example.firebaseauthtest.presentation.navigation.Screens
import com.example.firebaseauthtest.presentation.navigation_container_screen.NavigationContainerViewModel
import com.example.firebaseauthtest.ui.theme.Blue40
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@Composable
fun DatePickerScreen(
    navController: NavHostController,
    viewModel: NavigationContainerViewModel
) {
    LaunchedEffect(true){
        viewModel.checkUserRole()
        viewModel.getUserPermissions()
    }
    val context = LocalContext.current
    viewModel.setTopBarTitle(stringResource(R.string.date_selection))

    var selectedDate by remember { mutableStateOf("") }
    val errorToast = stringResource(R.string.select_a_date)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MarkDatePicker { date ->
            selectedDate = date
        }
        Spacer(modifier = Modifier.size(50.dp))
        CurrentDayPicker { date ->
            selectedDate = date
        }
        Spacer(modifier = Modifier.size(50.dp))
        Text(text = "Выбранная дата: $selectedDate", fontSize = 20.sp)
        Spacer(modifier = Modifier.size(20.dp))
        Button(onClick = {
            viewModel.selectDate(selectedDate)
            if (viewModel.selectedDate.value == "") {
                Toast.makeText(context, errorToast, Toast.LENGTH_SHORT).show()
            } else {
                Log.d("MyTag", "---------------------------- ${viewModel.selectedDate.value}")
                navController.navigate(Screens.Groups.route)
            }
        }) {
            Text(text = "Применить", color = Color.White)
        }
    }
}

@Composable
fun CurrentDayPicker(
    onDateSelected: (String) -> Unit
) {
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val currentDate = LocalDate.now().format(dateFormat)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                onDateSelected(currentDate)
            },
            colors = ButtonDefaults
                .buttonColors(containerColor = Blue40)
        ) {
            Text(text = "Сегодня", color = Color.White)
        }
        Spacer(modifier = Modifier.size(50.dp))
    }
}

@Composable
fun MarkDatePicker(
    onDateSelected: (String) -> Unit
) {
    val localContext = LocalContext.current

    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()

    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        localContext,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            val selectedDate = "$day/${month + 1}/$year"
            onDateSelected(selectedDate)
        }, year, month, day
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                datePickerDialog.show()
            },
            colors = ButtonDefaults
                .buttonColors(containerColor = Blue40)
        ) {
            Text(text = "Задать свою дату", color = Color.White)
        }
        Spacer(modifier = Modifier.size(100.dp))
    }
}