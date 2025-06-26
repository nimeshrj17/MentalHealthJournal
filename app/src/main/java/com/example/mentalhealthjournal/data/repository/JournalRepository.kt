package com.example.mentalhealthjournal.data.repository

import com.example.mentalhealthjournal.data.dao.JournalDao
import com.example.mentalhealthjournal.data.entity.JournalEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JournalRepository @Inject constructor(
    private val dao: JournalDao
) {
    fun getAllEntries(): Flow<List<JournalEntry>> = dao.getAllEntries()

    suspend fun getEntryById(id: Int): JournalEntry? = dao.getEntryById(id)

    suspend fun insert(entry: JournalEntry): Unit = dao.insert(entry)

    suspend fun update(entry: JournalEntry) = dao.update(entry)

    suspend fun delete(entry: JournalEntry) = dao.delete(entry)


}
