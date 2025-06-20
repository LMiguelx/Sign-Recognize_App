package com.example.sos_seasapi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sos_seasapi.domain.usecase.ObtenerHistorialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    obtenerHistorialUseCase: ObtenerHistorialUseCase
) : ViewModel() {
    val historial = obtenerHistorialUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}