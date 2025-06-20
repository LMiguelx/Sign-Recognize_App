package com.example.sos_seasapi.presentation.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sos_seasapi.presentation.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val historial = viewModel.historial.collectAsState().value

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Historial de Gestos", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(historial) { gesto ->
                Text("${gesto.nombre} — ${java.text.SimpleDateFormat("HH:mm:ss dd/MM").format(gesto.fecha)}")
                Divider()
            }
        }
    }
}