package com.example.sos_seasapi.di


import android.content.Context
import androidx.room.Room
import com.example.sos_seasapi.data.local.GestoDao
import com.example.sos_seasapi.data.local.GestoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @dagger.hilt.android.qualifiers.ApplicationContext context: Context
    ): GestoDatabase {
        return Room.databaseBuilder(
            context,
            GestoDatabase::class.java,
            "gestos_db"
        ).build()
    }

    @Provides
    fun provideGestoDao(db: GestoDatabase): GestoDao = db.gestoDao()
}