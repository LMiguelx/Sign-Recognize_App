package com.example.sos_seasapi.domain.usecase

import com.example.sos_seasapi.data.model.GestoResponse
import com.example.sos_seasapi.domain.repository.GestoRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class EnviarVideoUseCase @Inject constructor(
    private val repository: GestoRepository
) {
    suspend operator fun invoke(video: MultipartBody.Part): GestoResponse {
        return repository.enviarVideo(video)
    }
}