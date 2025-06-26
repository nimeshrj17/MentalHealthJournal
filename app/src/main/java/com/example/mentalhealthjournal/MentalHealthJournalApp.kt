package com.example.mentalhealthjournal

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.mentalhealthjournal.navigation.AppNavGraph

@Composable
fun MentalHealthJournalApp() {
    val navController = rememberNavController()
    AppNavGraph(navController = navController)
}
