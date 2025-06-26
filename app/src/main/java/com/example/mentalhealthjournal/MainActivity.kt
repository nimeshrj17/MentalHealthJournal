package com.example.mentalhealthjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mentalhealthjournal.ui.theme.MentalHealthJournalTheme
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.FirebaseApp


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        setContent {
            MentalHealthJournalTheme {
                MentalHealthJournalApp()
            }
        }
    }
}
