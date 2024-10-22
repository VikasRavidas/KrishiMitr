package com.example.krishimitr

sealed class Screen(val route: String) {
    data object Login : Screen("login_screen")
    data object Main : Screen("main_screen")
    data object Profile : Screen("profile_screen")
    data object History : Screen("history_screen")
    data object Contact : Screen("contact_screen")
    data object Home : Screen("home_screen")

}