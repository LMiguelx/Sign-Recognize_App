package com.example.sos_seasapi.data.service

import com.example.sos_seasapi.data.model.GestoResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface GestoApiService {

    @Multipart
    @POST("predict") // Cambia esto a tu endpoint real
    suspend fun enviarVideo(
        @Part video: MultipartBody.Part
    ): GestoResponse
}