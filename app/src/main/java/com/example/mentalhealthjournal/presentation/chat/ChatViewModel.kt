package com.example.mentalhealthjournal.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mentalhealthjournal.service.AiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val aiService: AiService
) : ViewModel() {

    fun getSupportResponse(title: String, content: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val support = aiService.generateSupport(title, content)
                val combined = support.response + "\n\nTips:\n" + support.suggestions.joinToString("\n• ", prefix = "• ")
                onResult(combined)
            } catch (e: Exception) {
                onResult("Error: ${e.localizedMessage}")
            }
        }
    }
}
