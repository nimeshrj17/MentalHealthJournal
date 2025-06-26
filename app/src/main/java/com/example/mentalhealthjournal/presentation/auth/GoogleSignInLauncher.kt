package com.example.mentalhealthjournal.presentation.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

fun handleGoogleSignInResult(
    resultCode: Int,
    data: Intent?,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    if (resultCode == Activity.RESULT_OK && data != null) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                onSuccess(idToken)
            } else {
                onError("No ID token found")
            }
        } catch (e: ApiException) {
            onError("Google Sign-In failed: ${e.message}")
        }
    } else {
        onError("Sign-in canceled or failed")
    }
}
