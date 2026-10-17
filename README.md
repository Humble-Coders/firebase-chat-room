# Chat Room — learning Firebase step by step (Jetpack Compose)

## Milestone 1: Email + password login, then a welcome screen

No chat yet. Today the app does three things:

1. **Create account** — a new email + password is saved in Firebase Authentication.
2. **Log in** — an existing email + password is checked by Firebase.
3. **Welcome screen** — shows who is logged in, with a **Log out** button.

Close the app and open it again: you're still logged in. Firebase remembers.

## The code — four small files

```
com/example/chatbot/
├── Auth.kt           ALL the Firebase code + who is logged in
├── MainActivity.kt   shows LoginScreen or HomeScreen
├── LoginScreen.kt    email + password fields, "Log in" and "Create account" buttons
└── HomeScreen.kt     "Welcome!", your email, and "Log out"
```

Read them in this order: **Auth.kt → MainActivity.kt → LoginScreen.kt → HomeScreen.kt**.

The Firebase calls that matter, all in `Auth.kt`:

```kotlin
auth.createUserWithEmailAndPassword(email, password)   // sign up
auth.signInWithEmailAndPassword(email, password)       // log in
auth.signOut()                                         // log out
auth.currentUser                                       // who is logged in? (null = nobody)
```

`Auth.kt` also keeps two variables that the screens read:

```kotlin
var loggedInEmail by mutableStateOf(auth.currentUser?.email)   // null = nobody logged in
var authError by mutableStateOf<String?>(null)                 // Firebase's last error message
```

Firebase answers over the internet, so the answer comes back a moment later.
When it does, we just update one of those variables:

```kotlin
auth.signInWithEmailAndPassword(email, password)
    .addOnSuccessListener { loggedInEmail = auth.currentUser?.email }   // worked
    .addOnFailureListener { authError = it.message }                    // didn't
```

And the app picks a screen with one `if` (`MainActivity.kt`):

```kotlin
if (loggedInEmail == null) LoginScreen() else HomeScreen()
```

Because `loggedInEmail` is Compose state, changing it redraws the app, and the
right screen appears. That's our whole "navigation".

## Firebase setup

1. Create a Firebase project and add an **Android app** with package name `com.example.chatbot`.
2. Download `google-services.json` and put it in the `app/` folder
   (next to `app/build.gradle.kts`).

   This file is **not** in the repo. Everyone uses their own Firebase project.
   Until you add it, the build stops with *"File google-services.json is missing"*.
3. In the Firebase console go to **Build → Authentication → Get started**,
   then **Sign-in method → Email/Password → Enable → Save**.
4. Sync Gradle and run.

After someone creates an account, open **Authentication → Users** in the console.
Their email is listed there. The password is not visible to anyone, not even you.

## Errors students will hit (and what they mean)

These messages appear in red under the buttons. They come straight from Firebase:

| Message says…                                  | Why                                                  |
|------------------------------------------------|------------------------------------------------------|
| "The given password is invalid" / 6 characters | Firebase needs a password of at least 6 characters   |
| "The email address is badly formatted"         | Not a real email shape, e.g. missing `@`             |
| "already in use by another account"            | Use **Log in** instead of **Create account**         |
| "The supplied auth credential is incorrect"    | Wrong email or password                              |
| "This operation is not allowed" / CONFIGURATION_NOT_FOUND | Email/Password isn't enabled in the console (setup step 3) |
