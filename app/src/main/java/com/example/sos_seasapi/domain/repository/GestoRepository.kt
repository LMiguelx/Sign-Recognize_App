package com.example.sos_seasapi.domain.repository

import com.example.sos_seasapi.data.model.GestoResponse
import okhttp3.MultipartBody

interface GestoRepository {
    suspend fun enviarVideo(video: MultipartBody.Part): GestoResponse
}