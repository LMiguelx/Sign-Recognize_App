package com.example.sos_seasapi.presentation.recorder

import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sos_seasapi.R
import com.example.sos_seasapi.presentation.navigation.Screen
import com.example.sos_seasapi.presentation.viewmodel.RecorderViewModel
import kotlinx.coroutines.delay
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
    var recordingSeconds by remember { mutableStateOf(0) }

    // Cronómetro: aumenta cada segundo durante la grabación
    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordingSeconds = 0
            while (isRecording) {
                delay(1000)
                recordingSeconds++
            }
        }
    }

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
            // Vista previa de la cámara
            key(cameraSelector) {
                CameraPreviewView(
                    isRecording = isRecording,
                    cameraSelector = cameraSelector,
                    onVideoSaved = { uri ->
                        Log.d("RecorderScreen", "Video guardado en: $uri")

                        val file = File(uri.path ?: return@CameraPreviewView)
                        Log.d("TAMAÑO_VIDEO", "Tamaño del archivo: ${file.length() / 1024} KB")

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

            // Botón de cambio de cámara
            Image(
                painter = painterResource(id = R.drawable.ic_flip_camera),
                contentDescription = "Cambiar cámara",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(48.dp)
                    .clickable {
                        useFrontCamera = !useFrontCamera
                        Toast.makeText(
                            context,
                            if (useFrontCamera) "Cámara frontal" else "Cámara trasera",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            )

            // Cronómetro mientras graba
            if (isRecording) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp)
                        .background(
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = String.format("%02d:%02d", recordingSeconds / 60, recordingSeconds % 60),
                        style = MaterialTheme.typography.labelLarge,
                        color = colorResource(id = R.color.black)
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { isRecording = !isRecording }) {
                Text(if (isRecording) "⏹️ Detener" else "🔴 Grabar")
            }
            Button(onClick = {
                navController.navigate(Screen.History.route)
            }) {
                Text("📜 Historial")
            }
        }
    }
}
