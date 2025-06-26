package com.example.mentalhealthjournal.data.dao

import androidx.room.*
import com.example.mentalhealthjournal.data.entity.JournalEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntry)

    @Update
    suspend fun update(entry: JournalEntry)

    @Delete
    suspend fun delete(entry: JournalEntry)

    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getEntryById(id: Int): JournalEntry?

    @Query("SELECT * FROM journal_entries WHERE sentiment = :sentiment ORDER BY date DESC")
    fun getEntriesBySentiment(sentiment: String): Flow<List<JournalEntry>>

}