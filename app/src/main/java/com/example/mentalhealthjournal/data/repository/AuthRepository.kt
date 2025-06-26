package com.yourname.mentalhealthjournal.data.repository

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.*
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth


class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient
) {

    fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

    fun signOut() {
        firebaseAuth.signOut()
        googleSignInClient.signOut()
    }

    fun signUp(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) onResult(true, null)
                else onResult(false, it.exception?.message)
            }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) onResult(true, null)
                else onResult(false, it.exception?.message)
            }
    }

    fun getGoogleSignInIntent(): Intent = googleSignInClient.signInIntent

    fun firebaseAuthWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) onResult(true, null)
                else onResult(false, it.exception?.message)
            }
    }

    fun signInWithPhone(credential: PhoneAuthCredential, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) onResult(true, null)
                else onResult(false, it.exception?.message)
            }
    }
}
