package com.example.sos_seasapi.presentation.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.sos_seasapi.presentation.navigation.Screen

@Composable
fun PermissionScreen(navController: NavController) {
    val context = LocalContext.current
    val permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    var allGranted by remember {
        mutableStateOf(permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        })
    }

    LaunchedEffect(allGranted) {
        if (allGranted) {
            navController.navigate(Screen.Recorder.route) {
                popUpTo(Screen.Permission.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (!allGranted) {
            Text("Esta app necesita permisos de cámara y micrófono.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    permissions.toTypedArray(),
                    101
                )
                allGranted = permissions.all {
                    ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                }
            }) {
                Text("Conceder permisos")
            }
        }
    }
}
