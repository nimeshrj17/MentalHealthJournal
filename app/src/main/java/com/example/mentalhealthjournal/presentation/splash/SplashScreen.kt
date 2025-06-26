package com.example.mentalhealthjournal.presentation.splash

import android.os.Handler
import android.os.Looper
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(
    onFinished: (Boolean) -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            onFinished(auth.currentUser != null)
        }, 2000)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Mental Health Journal", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))
            CircularProgressIndicator()
        }
    }
}
