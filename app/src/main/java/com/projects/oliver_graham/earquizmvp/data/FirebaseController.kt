package com.projects.oliver_graham.earquizmvp.data

import androidx.navigation.NavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.projects.oliver_graham.earquizmvp.navigation.NavigationController
import com.projects.oliver_graham.earquizmvp.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class FirebaseController(
    private val navController: NavigationController
) {

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)

    // flow of Users from firestore
    private val _usersList = MutableStateFlow(listOf<User>())
    private val _currentUser = MutableStateFlow(User())
    private var currentUserDocument: User? = User()

    init {
        initializeUsersList()
        initializeUser()
    }

    fun isUserLoggedIn() = auth.currentUser != null
    fun logOutUser() = auth.signOut()

    fun getUsers(): MutableStateFlow<List<User>> = _usersList

    fun getUserDocument(): User? = currentUserDocument

    fun saveUserDocument(user: User?) {
        firestore.collection(USERS_COLLECTION).document(user!!.uid)
            .set(user, SetOptions.merge())
    }

    // TODO: can use coroutine to reduce nesting?
    fun createUserWithEmailAndPassword(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // auth has a currentUser, it just can't believe it
                    firestore.collection(USERS_COLLECTION).document(auth.currentUser!!.uid)
                        .set(User(
                            uid = auth.currentUser!!.uid,
                            userName = userName,
                            email = email
                            )
                        )
                } else {
                    // do something?
                }
            }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {  task ->
                if (task.isSuccessful)
                    navController.navHomeScreenPopBackstack()
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

}