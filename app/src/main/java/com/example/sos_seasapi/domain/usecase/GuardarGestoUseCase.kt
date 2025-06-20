package com.example.sos_seasapi.domain.usecase

import com.example.sos_seasapi.data.local.GestoDao
import com.example.sos_seasapi.data.local.GestoEntity
import javax.inject.Inject

class GuardarGestoUseCase @Inject constructor(
    private val dao: GestoDao
) {
    suspend operator fun invoke(nombre: String) {
        dao.insertarGesto(GestoEntity(nombre = nombre))
    }
}