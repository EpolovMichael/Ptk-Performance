package com.example.firebaseauthtest.presentation.custom_components

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.firebaseauthtest.presentation.registration_screen.Student
import com.example.firebaseauthtest.ui.theme.Blue40

@Composable
fun SelectOptionCard(
    value: String,
    onClickListener: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                onClickListener()
            },
        colors = CardDefaults.cardColors(
            containerColor = Blue40,
            contentColor = Color.White
        )
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                text = value,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewCustomDialog() {
    CustomDialog(selectedObject = Student(), value = "", setShowDialog = {}, setValue = {})
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(
    selectedObject: Student,
    value: String,
    setShowDialog: (Boolean) -> Unit,
    setValue: (String) -> Unit
) {

    val txtFieldError = remember { mutableStateOf("") }
    val text = remember { mutableStateOf(value) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedObject.firstName ?: "",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold,
                                color = Blue40
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            tint = Blue40,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    width = 2.dp,
                                    color = colorResource(id = if (txtFieldError.value.isEmpty()) R.color.holo_green_light else R.color.holo_red_dark)
                                ),
                                shape = RoundedCornerShape(50)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "",
                                tint = colorResource(android.R.color.holo_green_light),
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                        },
                        placeholder = { Text(text = stringResource(com.example.firebaseauthtest.R.string.student_s_mark)) },
                        value = text.value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { newMark ->
                            val filteredValue = newMark.filter {
                                it.isDigit() || it == '.'
                            }
                            var formattedValue = if (filteredValue.count { it == '.' } > 1) {
                                filteredValue.substring(0, filteredValue.lastIndexOf('.')) // убираем повторяющиеся точки
                            } else {
                                filteredValue
                            }

                            if (formattedValue.length > 3) {
                                formattedValue = formattedValue.substring(0, 3)
                            }

                            val valueAsDouble = formattedValue.toDoubleOrNull()
                            if (valueAsDouble != null && valueAsDouble <= 5.0) {
                                text.value = formattedValue
                                txtFieldError.value = ""
                            } else if (formattedValue.isNotEmpty()) {
                                text.value = formattedValue
                                txtFieldError.value = "Оценка не должна быть больше 5.0"
                            } else {
                                text.value = ""
                                txtFieldError.value = ""
                            }
                        })

                    if (txtFieldError.value.isNotEmpty()) {
                        Text(
                            text = txtFieldError.value,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {
                                if (text.value.isEmpty()) {
                                    txtFieldError.value = "Поле для оценки не должно быть пустым!"
                                    return@Button
                                }
                                setValue(text.value)
                                setShowDialog(false)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Blue40
                            ),
                            shape = RoundedCornerShape(size = 50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
                                Text(
                                    text = stringResource(com.example.firebaseauthtest.R.string.done),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}