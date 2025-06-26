package com.example.mentalhealthjournal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mentalhealthjournal.data.dao.JournalDao
import com.example.mentalhealthjournal.data.entity.JournalEntry
import com.example.mentalhealthjournal.util.DateConverter

@Database(entities = [JournalEntry::class], version = 2, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao
}