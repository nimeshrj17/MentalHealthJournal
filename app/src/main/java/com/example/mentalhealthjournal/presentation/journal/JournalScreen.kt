package com.example.mentalhealthjournal.presentation.journal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mentalhealthjournal.data.entity.JournalEntry
import com.example.mentalhealthjournal.domain.model.AiSupport
import com.example.mentalhealthjournal.service.AiService
import kotlinx.coroutines.launch

@Composable
fun JournalScreen(
    viewModel: JournalViewModel = hiltViewModel(),
    onEditEntry: (JournalEntry) -> Unit,
    aiService: AiService // ✅ Pass from DI (not hardcoded)
) {
    val entries by viewModel.entries.collectAsState()
    val scope = rememberCoroutineScope()

    // 🔄 Cache to avoid refetching on recomposition
    val aiCache = rememberSaveable(saver = mapSaver(
        save = { map -> map.map { it.key.toString() to it.value.response }.toMap() },
        restore = { restoredMap ->
            restoredMap.mapKeys { it.key.toLong() }.mapValues {
                AiSupport(response = it.value as String, suggestions = emptyList())
            }.toMutableMap()
        }
    )) { mutableStateMapOf<Long, AiSupport>() }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEditEntry(
                    JournalEntry(title = "", content = "", date = java.util.Date(), sentiment = "")
                )
            }) {
                Text("+")
            }
        }
    ) {
        LazyColumn(modifier = Modifier
            .padding(it)
            .padding(16.dp)
        ) {
            items(entries) { entry ->
                var aiSupport by remember { mutableStateOf<AiSupport?>(aiCache[entry.id ?: entry.hashCode().toLong()]) }
                var isLoading by remember { mutableStateOf(aiSupport == null) }

                LaunchedEffect(entry.id) {
                    if (aiSupport == null) {
                        isLoading = true
                        try {
                            val result = aiService.generateSupport(entry.title, entry.content)
                            aiSupport = result
                            val key = (entry.id as? Long) ?: entry.hashCode().toLong()
                            aiCache[key] = result
                        } catch (_: Exception) {
                            aiSupport = null
                        } finally {
                            isLoading = false
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onEditEntry(entry) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = entry.title, style = MaterialTheme.typography.titleMedium)
                        Text(text = entry.content, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Sentiment: ${entry.sentiment}", style = MaterialTheme.typography.labelSmall)

                        when {
                            isLoading -> {
                                Spacer(Modifier.height(8.dp))
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                                Text("AI is analyzing...", style = MaterialTheme.typography.bodySmall)
                            }
                            aiSupport != null -> {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("🧠 Support: ${aiSupport!!.response}", style = MaterialTheme.typography.bodySmall)
                                Text("💡 Tips: ${aiSupport!!.suggestions.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                            }
                            else -> {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("❌ AI support unavailable.", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
