package com.projects.oliver_graham.earquizmvp.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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

    // flow of Users from firestore and LiveData to listen for callback when user logs in
    private val _usersList = MutableStateFlow(listOf<User>())
    private val currentUserDocument: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    init {
        initializeUsersList()
        initializeUser()
    }

    fun isUserLoggedIn() = auth.currentUser != null

    // logout functionality will be added soon
    fun logOutUserFromFirebase() = auth.signOut()
    fun logOutUserFromGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ID_TOKEN_GOOGLE)
            .build()
        GoogleSignIn.getClient(context, gso).signOut()
    }

    fun getUsers(): MutableStateFlow<List<User>> = _usersList
    fun getUserDocument(): User? = currentUserDocument.value

    fun createUserWithEmailAndPassword(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    initializeUser()
                    auth.currentUser?.let { saveAuthenticatedUserAsNewDocument(it, userName, email) }
                } else
                    toastMessage(context, message = "Unable to create user")
            }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {  task ->
                if (task.isSuccessful) {
                    initializeUser()
                    navController.navHomeScreenPopBackstack()
                } else
                    toastMessage(context = context, message = "Invalid username/password")
            }

    }

    fun updateUserDocument(user: User?) {
        firestore.collection(USERS_COLLECTION).document(user!!.uid)
            .set(user, SetOptions.merge())
    }

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
                    val authUser = auth.currentUser
                    if (authUser != null) {
                        // TODO: Refactor this into initializeUser()
                        firestore.collection(USERS_COLLECTION).document(authUser.uid).get()
                            .addOnSuccessListener { user ->
                                if (!user.exists()) {
                                    val email = authUser.email.toString()
                                    saveAuthenticatedUserAsNewDocument(
                                        authUser, trimEmail(email), email)
                                }
                            }

                        initializeUser()
                        navController.navHomeScreenPopBackstack()
                    }
                } else {
                    toastMessage(context, message = "Unable to sign in")
                }
            }
    }


    private fun trimEmail(email: String): String {
        val atIndex = email.indexOf(string = "@")
        return email.substring(startIndex = 0, endIndex = atIndex)
    }

    private fun saveAuthenticatedUserAsNewDocument(user: FirebaseUser, userName: String, email: String) {
        firestore.collection(USERS_COLLECTION).document(user.uid)
            .set(User(
                uid = user.uid,
                userName = if (userName != "") userName else email,
                email = email,
                )
            )
    }

    // gets every document from firestore as a flow
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
                    if (user != null) {
                        // set the live data for later observation
                        currentUserDocument.value = user.toObject(User::class.java)
                    }
                }
        }
    }

    private fun toastMessage(context: Context, message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}