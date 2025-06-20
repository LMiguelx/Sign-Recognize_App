package com.example.sos_seasapi.presentation.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.sos_seasapi.R
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (!allGranted) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Imagen decorativa (puedes reemplazar con la tuya)
                Image(
                    painter = painterResource(id = R.drawable.permission_image), // Cambia a tu recurso
                    contentDescription = "Permisos necesarios",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 24.dp)
                )

                Text(
                    text = "Permisos requeridos",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Necesitamos acceso a la cámara y al micrófono para detectar tus gestos correctamente.",
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            permissions.toTypedArray(),
                            101
                        )
                        allGranted = permissions.all {
                            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Conceder permisos")
                }
            }
        }
    }
}
