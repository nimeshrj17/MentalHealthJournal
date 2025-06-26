package com.example.mentalhealthjournal.di

import android.app.Application
import androidx.room.Room
import com.example.mentalhealthjournal.data.dao.JournalDao
import com.example.mentalhealthjournal.data.database.AppDatabase
import com.example.mentalhealthjournal.data.repository.JournalRepository
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
    fun provideAppDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "journal_db")
            .fallbackToDestructiveMigration()
            .build()





}
