package com.example.mentalhealthjournal.presentation.journal

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun EditJournalScreen(
    viewModel: JournalViewModel = hiltViewModel(),
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var aiResponse by remember { mutableStateOf("") }
    var aiTips by remember { mutableStateOf(listOf<String>()) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Journal Entry") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.getSupportResponse(title, content) { resultText, suggestions ->
                    aiResponse = resultText
                    aiTips = suggestions
                }
            }
        ) {
            Text("Get AI Support")
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.addEntry(title, content)
                navController.popBackStack() // optional: navigate back
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

        if (aiResponse.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text("AI Response:", style = MaterialTheme.typography.titleMedium)
            Text(aiResponse)

            Spacer(Modifier.height(8.dp))
            Text("Suggestions:", style = MaterialTheme.typography.titleMedium)
            aiTips.forEach { tip ->
                Text("• $tip")
            }
        }
    }
}
