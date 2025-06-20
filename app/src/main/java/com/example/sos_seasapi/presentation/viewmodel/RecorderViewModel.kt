package com.example.sos_seasapi.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sos_seasapi.data.model.GestoResponse
import com.example.sos_seasapi.domain.usecase.EnviarVideoUseCase
import com.example.sos_seasapi.domain.usecase.GuardarGestoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class RecorderViewModel @Inject constructor(
    private val enviarVideoUseCase: EnviarVideoUseCase,
    private val guardarGestoUseCase: GuardarGestoUseCase // ✅ inyectado correctamente
) : ViewModel() {

    fun enviarVideo(video: MultipartBody.Part, onResult: (GestoResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = enviarVideoUseCase(video)
                guardarGestoUseCase(response.label)
                onResult(response)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error al enviar video: ${e.message}")
                onResult(GestoResponse(0f, "Error", 0f))
            }
        }
    }
}
