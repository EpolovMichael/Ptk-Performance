package com.example.firebaseauthtest.presentation.marks_screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun MarkTextItem(
    data: String,
    discipline: String,
    mark: String
) {
    ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1.5f),
                textAlign = TextAlign.Center,
                text = data
            )
            Text(
                modifier = Modifier
                    .weight(5f),
                textAlign = TextAlign.Center,
                text = discipline
            )
            Text(
                modifier = Modifier
                    .weight(1.5f),
                textAlign = TextAlign.Center,
                text = mark
            )
        }
    }
}