package com.example.sos_seasapi.presentation.navigation


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sos_seasapi.presentation.history.HistoryScreen
import com.example.sos_seasapi.presentation.permissions.PermissionScreen
import com.example.sos_seasapi.presentation.recorder.RecorderScreen
import com.example.sos_seasapi.presentation.result.ResultScreen
import com.example.sos_seasapi.presentation.splash.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Permission : Screen("permission")
    object Recorder : Screen("recorder")
    object Result : Screen("result/{resultado}") {
        fun createRoute(resultado: String) = "result/${Uri.encode(resultado)}"
    }
    object History : Screen("history")
}

@Composable
fun GestoNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Permission.route) {
            PermissionScreen(navController)
        }
        composable(Screen.Recorder.route) {
            RecorderScreen(navController)
        }
        composable(Screen.Result.route) { backStackEntry ->
            val resultado = backStackEntry.arguments?.getString("resultado") ?: "Sin resultado"
            ResultScreen(navController, resultado)
        }
        composable(Screen.History.route) {
            HistoryScreen()
        }
    }
}