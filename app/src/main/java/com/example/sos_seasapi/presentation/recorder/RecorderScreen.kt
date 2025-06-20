package com.example.sos_seasapi.presentation.recorder

import android.content.pm.PackageManager
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sos_seasapi.presentation.navigation.Screen
import com.example.sos_seasapi.presentation.viewmodel.RecorderViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Composable
fun RecorderScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: RecorderViewModel = hiltViewModel()

    var isRecording by remember { mutableStateOf(false) }
    var useFrontCamera by remember { mutableStateOf(false) }

    val cameraSelector = if (useFrontCamera) {
        CameraSelector.DEFAULT_FRONT_CAMERA
    } else {
        CameraSelector.DEFAULT_BACK_CAMERA
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CameraPreviewView(
                isRecording = isRecording,
                cameraSelector = cameraSelector,
                onVideoSaved = { uri ->
                    Log.d("RecorderScreen", "Video guardado en: $uri")

                    val file = File(uri.path ?: return@CameraPreviewView)

                    Log.d("TAMAÑO_VIDEO", "Tamaño del archivo: ${file.length() / 1024} KB") // ✅ tamaño real

                    // ✅ Crear Multipart con el nombre exacto "video" y tipo correcto
                    val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())
                    val body = MultipartBody.Part.createFormData("video", file.name, requestFile)

                    viewModel.enviarVideo(body) { response ->

                        Log.d("RESPUESTA_GESTO", "Gesto: ${response.label}")
                        Log.d("RESPUESTA_GESTO", "Confianza: ${response.confianza}")
                        Log.d("RESPUESTA_GESTO", "Tiempo procesamiento: ${response.tiempo_procesamiento} segundos")
                        navController.navigate(Screen.Result.createRoute(response.label))
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { isRecording = !isRecording }) {
                Text(if (isRecording) "⏹️ Detener" else "🔴 Grabar")
            }
            Button(onClick = { useFrontCamera = !useFrontCamera }) {
                Text("🔄 Cambiar")
            }
            Button(onClick = {
                navController.navigate(Screen.History.route)
            }) {
                Text("📜 Historial")
            }
        }
    }
}

fun checkPermissions(context: android.content.Context, permissions: List<String>): Boolean {
    return permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}
