package com.example.firebaseauthtest.presentation.navigation

sealed class Screens(val route: String) {
    object SplashScreen : Screens(route = "Splash_Screen")
    object SignInScreen : Screens(route = "SignIn_Screen")
    object SignUpScreen : Screens(route = "SignUp_ Screen")
    object ContentGraphScreen : Screens(route = "Content_Graph_Screen")
    object ContentScreen : Screens(route = "Content_Screen")

    object StartPrepodRoute : Screens(route = "Start_Prepo_Route")
    object Groups : Screens(route = "Groups_Screen")
    object Disciplines : Screens(route = "Disciplines_Screen")
    object Themes : Screens(route = "Themes_Screen")
    object Students : Screens(route = "Students_Screen")
    object Marks : Screens(route = "Marks_Screen")
    object DateTimePicker : Screens(route = "Date_Time_Picker_Screen")

    object StartStudentRoute : Screens(route = "Start_Student_Route")
    object AttendanceCourses : Screens(route = "AttendanceCourses_Screen")
}