package com.example.sos_seasapi.data.repository

import com.example.sos_seasapi.data.model.GestoResponse
import com.example.sos_seasapi.data.service.GestoApiService
import com.example.sos_seasapi.domain.repository.GestoRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class GestoRepositoryImpl @Inject constructor(
    private val apiService: GestoApiService
) : GestoRepository {
    override suspend fun enviarVideo(video: MultipartBody.Part): GestoResponse {
        return apiService.enviarVideo(video)
    }
}