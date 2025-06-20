package com.example.sos_seasapi.presentation.recorder

import android.net.Uri
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CameraPreviewView(
    isRecording: Boolean,
    cameraSelector: CameraSelector,
    onVideoSaved: (Uri) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner

    var videoCapture by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }

    AndroidView(factory = { ctx ->
        val previewView = androidx.camera.view.PreviewView(ctx).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.SD))
                .build()

            val newVideoCapture = VideoCapture.withOutput(recorder)
            videoCapture = newVideoCapture

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    newVideoCapture
                )
            } catch (exc: Exception) {
                Log.e("CameraPreview", "Error binding use cases", exc)
            }

        }, ContextCompat.getMainExecutor(ctx))

        previewView
    }, modifier = Modifier.fillMaxSize())


    LaunchedEffect(isRecording) {
        val vc = videoCapture ?: return@LaunchedEffect

        if (isRecording) {
            val hasAudioPermission = ActivityCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

            val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                .format(System.currentTimeMillis()) + ".mp4"
            val outputFile = File(context.cacheDir, name)

            val mediaOutput = FileOutputOptions.Builder(outputFile).build()

            val mediaPreparer = vc.output.prepareRecording(context, mediaOutput)

            val finalRecording = if (hasAudioPermission) {
                mediaPreparer.withAudioEnabled()
            } else {
                mediaPreparer
            }

            val recording = finalRecording.start(ContextCompat.getMainExecutor(context)) { event ->
                if (event is VideoRecordEvent.Finalize) {
                    if (event.hasError()) {
                        Log.e("Recorder", "Error al grabar: ${event.error}")
                    } else {
                        onVideoSaved(Uri.fromFile(outputFile))
                    }
                }
            }

            currentRecording = recording
        } else {
            currentRecording?.stop()
            currentRecording = null
        }
    }
}

private var currentRecording: Recording? = null