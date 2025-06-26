package com.example.mentalhealthjournal.presentation.auth

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

@Composable
fun PhoneAuthScreen(
    onVerificationSuccess: () -> Unit,
    navController: NavController
) {
    val context = LocalActivity.current as Activity
    val auth = FirebaseAuth.getInstance()
    var phone by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var isVerifying by remember { mutableStateOf(false) }

    Column(Modifier.padding(24.dp)) {
        Text("Phone Verification", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    auth.signInWithCredential(credential).addOnSuccessListener {
                        onVerificationSuccess()
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Handle error
                }

                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    verificationId = id
                    isVerifying = true
                }
            }

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                context,
                callbacks
            )
        }) {
            Text("Send OTP")
        }

        if (isVerifying) {
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Enter OTP") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(onClick = {
                val cred = PhoneAuthProvider.getCredential(verificationId!!, code)
                auth.signInWithCredential(cred).addOnSuccessListener {
                    onVerificationSuccess()
                }
            }) {
                Text("Verify")
            }
        }
    }
}
