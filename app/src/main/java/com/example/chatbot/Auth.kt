package com.example.chatbot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth

// ALL the Firebase code in the app lives in this one file.
//
// Firebase Authentication keeps the list of users for us (see the "Users" tab
// under Authentication in the Firebase console). We never store passwords ourselves.

/** Our connection to Firebase Authentication. */
private val auth = FirebaseAuth.getInstance()

// The two variables below are "state": whenever they change, the screen redraws by itself.

/** The email of whoever is logged in, or null if nobody is. Firebase remembers it even after the app is closed. */
var loggedInEmail by mutableStateOf(auth.currentUser?.email)

/** What went wrong last time (wrong password, email taken, ...), or null if nothing did. */
var authError by mutableStateOf<String?>(null)

/** SIGN UP: creates a brand-new account (and logs it in). */
fun signUp(email: String, password: String) {
    authError = null
    auth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener { loggedInEmail = auth.currentUser?.email }
        .addOnFailureListener { authError = it.message }
}

/** LOG IN: checks the email and password against an existing account. */
fun logIn(email: String, password: String) {
    authError = null
    auth.signInWithEmailAndPassword(email, password)
        .addOnSuccessListener { loggedInEmail = auth.currentUser?.email }
        .addOnFailureListener { authError = it.message }
}

/** LOG OUT: forgets the logged-in user on this phone. */
fun logOut() {
    auth.signOut()
    loggedInEmail = null
}
