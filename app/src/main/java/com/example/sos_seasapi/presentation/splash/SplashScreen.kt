package com.example.sos_seasapi.presentation.splash

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sos_seasapi.presentation.navigation.Screen


@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            navController.navigate(Screen.Permission.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }, 2000)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Gesto Detector", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}