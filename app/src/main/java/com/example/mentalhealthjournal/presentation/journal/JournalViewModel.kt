package com.example.mentalhealthjournal.presentation.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mentalhealthjournal.data.entity.JournalEntry
import com.example.mentalhealthjournal.data.repository.JournalRepository
import com.example.mentalhealthjournal.service.AiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val repository: JournalRepository,
    private val aiService: AiService
) : ViewModel() {

    val entries = repository.getAllEntries()
        .map { it.sortedByDescending { entry -> entry.date } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEntry(title: String, content: String) {
        viewModelScope.launch {
            val sentiment = try {
                aiService.analyzeSentiment(content)
            } catch (e: Exception) {
                "Neutral" // fallback sentiment
            }

            val entry = JournalEntry(
                title = title,
                content = content,
                date = Date(),
                sentiment = sentiment
            )
            repository.insert(entry)
        }
    }

    fun updateEntry(entry: JournalEntry) {
        viewModelScope.launch {
            repository.update(entry)
        }
    }

    fun deleteEntry(entry: JournalEntry) {
        viewModelScope.launch {
            repository.delete(entry)
        }
    }

    fun getSupportResponse(
        title: String,
        content: String,
        onResult: (String, List<String>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val support = aiService.generateSupport(title, content)
                onResult(support.response, support.suggestions)
            } catch (e: Exception) {
                onResult("AI Support Unavailable", emptyList())
            }
        }
    }
}
