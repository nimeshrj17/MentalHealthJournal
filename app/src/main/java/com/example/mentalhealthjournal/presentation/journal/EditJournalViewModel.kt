package com.example.mentalhealthjournal.presentation.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mentalhealthjournal.service.AiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditJournalViewModel @Inject constructor(
    private val aiService: AiService
) : ViewModel() {


}
