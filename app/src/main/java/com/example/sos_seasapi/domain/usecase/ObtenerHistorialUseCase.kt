package com.example.sos_seasapi.domain.usecase

import com.example.sos_seasapi.data.local.GestoDao
import com.example.sos_seasapi.data.local.GestoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObtenerHistorialUseCase @Inject constructor(
    private val dao: GestoDao
) {
    operator fun invoke(): Flow<List<GestoEntity>> = dao.obtenerHistorial()
}