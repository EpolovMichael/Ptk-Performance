package com.example.firebaseauthtest.presentation.marks_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.firebaseauthtest.presentation.registration_screen.Student

@Composable
fun MarkItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 4.dp)
            .clip(shape = RectangleShape)
            .clickable { } // Прописать логику раскрытия или перехода на экран с подробным описанием предмета
    ) {
        MarkTextItem(
            data = "20.09",
            discipline = "Название дисциплины",
            mark = "4.73"
        )
    }
}