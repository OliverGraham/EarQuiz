package com.projects.oliver_graham.earquizmvp.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

// TODO: Refactor signin methods to update the current user; make nicer
class FirebaseController(
    private val navController: NavigationController,
    private val context: Context
) {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val ID_TOKEN_GOOGLE =
            "130628022625-kv7ppbklmd4alb23ru8td7488ff23cb1.apps.googleusercontent.com"
    }

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)

    // flow of Users from firestore
    private val _usersList = MutableStateFlow(listOf<User>())
    private var currentUserDocument: User? = User()

    init {
        initializeUsersList()
        initializeUser()
    }

    fun isUserLoggedIn() = auth.currentUser != null
    fun logOutUserFromFirebase() = auth.signOut()
    fun logOutUserFromGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ID_TOKEN_GOOGLE)
            .build()
        GoogleSignIn.getClient(context, gso).signOut()

    }

    fun getUsers(): MutableStateFlow<List<User>> = _usersList
    fun getUserDocument(): User? = currentUserDocument

    fun getGoogleSignInIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ID_TOKEN_GOOGLE)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso).signInIntent
    }

    fun completeGoogleSignIn(intent: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)

        } catch (e: ApiException) {
            toastMessage(context, message = "Google sign-in failed")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        initializeUser()
                        user.email?.let { saveUserToFirestore(user, "", it) }
                        navController.navHomeScreenPopBackstack()
                    }
                } else {
                    toastMessage(context, message = "Unable to sign in")
                }
            }
    }

    fun updateUserDocument(user: User?) {
        firestore.collection(USERS_COLLECTION).document(user!!.uid)
            .set(user, SetOptions.merge())
    }

    private fun saveUserToFirestore(currentUser: FirebaseUser, userName: String, email: String) {
        firestore.collection(USERS_COLLECTION).document(currentUser.uid)
            .set(User(
                    uid = currentUser.uid,
                    userName = if (userName != "") userName else email,
                    email = email
                )
            )
    }

    // TODO: can use coroutine to reduce nesting?
    fun createUserWithEmailAndPassword(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    // auth has a currentUser, it just can't believe it
                    auth.currentUser?.let { saveUserToFirestore(it, userName, email) }
                else
                    toastMessage(context, message = "Unable to create user")
            }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {  task ->
                if (task.isSuccessful) {
                    initializeUser()
                    navController.navHomeScreenPopBackstack()
                }
                else
                    toastMessage(context = context, "Invalid username/password")
            }

    }

    private fun initializeUsersList() {
        firestore.collection(USERS_COLLECTION)
            .addSnapshotListener { users, e ->
                if (e != null)
                    return@addSnapshotListener  // do something different?

                if (users != null)
                    scope.launch { ->
                        _usersList.emit(users.toObjects(User::class.java))
                    }
            }
    }

    private fun initializeUser() {
        val authenticatedUser = auth.currentUser
        if (authenticatedUser != null) {
            firestore.collection(USERS_COLLECTION).document(authenticatedUser.uid).get()
                .addOnSuccessListener { user ->
                    if (user != null)
                        currentUserDocument = user.toObject(User::class.java)
                }
        }
    }

    private fun toastMessage(context: Context, message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}