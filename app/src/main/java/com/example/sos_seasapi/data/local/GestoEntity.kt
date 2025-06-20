package com.example.sos_seasapi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gestos")
data class GestoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val fecha: Long = System.currentTimeMillis()
)