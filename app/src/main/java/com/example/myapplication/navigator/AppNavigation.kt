package com.example.myapplication.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.screens.DashboardScreen
import com.example.myapplication.screens.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // "startDestination" define qué pantalla carga al abrir la app
    NavHost(navController = navController, startDestination = "login") {

        // Pantalla 1: Login
        composable("login") {
            LoginScreen(navController)
        }

        // Pantalla 2: Dashboard
        composable("dashboard") {
            DashboardScreen()
        }
    }
}