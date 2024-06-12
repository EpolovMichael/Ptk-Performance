package com.example.firebaseauthtest.presentation.navigation

import com.example.firebaseauthtest.R

sealed class NavigationItem(val route: String, var icon: Int, var title: String) {
    // Для теста перый - путь препода, а второй - путь студента
    object AttendanceCourses : NavigationItem(Screens.StartPrepodRoute.route, R.drawable.attendance_img, "Посещаемость")
    object Marks : NavigationItem(Screens.StartStudentRoute.route, R.drawable.marks_img, "Оценки")
}