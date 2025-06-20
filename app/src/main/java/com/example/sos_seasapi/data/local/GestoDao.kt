package com.example.sos_seasapi.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GestoDao {
    @Insert
    suspend fun insertarGesto(gesto: GestoEntity)

    @Query("SELECT * FROM gestos ORDER BY fecha DESC")
    fun obtenerHistorial(): Flow<List<GestoEntity>>
}