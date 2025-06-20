package com.example.sos_seasapi.data.local


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GestoEntity::class], version = 1)
abstract class GestoDatabase : RoomDatabase() {
    abstract fun gestoDao(): GestoDao
}