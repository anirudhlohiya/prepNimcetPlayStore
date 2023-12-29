package com.example.prepnimcet

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.prepnimcet.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class activity_login : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val dialog = ProgressDialog(this)
        dialog.setMessage("We're creating your account")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnArrowleft.setOnClickListener {
            finish()
        }

        binding.txtForgotPassword.setOnClickListener {
            // Start the LoginActivity when the Login button is clicked
            val intent = Intent(this, activity_forget_password::class.java)
            startActivity(intent)
        }

        binding.imageGoogle.setOnClickListener {
            showProgressDialog()
            signInGoogle()
        }

        binding.txtRegisterNow.setOnClickListener {
            val intent = Intent(this, activity_registeer::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etSignInEmail.text.toString()
            val password = binding.etSignInPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (firebaseAuth.currentUser?.isEmailVerified == true) {
                            // Update emailVerified to true in the Firestore database
                            updateEmailVerificationStatus(email)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Email is not verified", Toast.LENGTH_LONG).show()
                        }

                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Kindly fill all the data", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showProgressDialog() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Signing in with Google...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        // Save a reference to the progressDialog in the class property
        progressDialogSignIn = progressDialog
    }

    private fun dismissProgressDialog() {
        progressDialogSignIn?.dismiss()
    }

    private var progressDialogSignIn: ProgressDialog? = null

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcherGoogle.launch(signInIntent)
    }

    private val launcherGoogle =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleGoogleSignInResult(task)
            } else {
                Toast.makeText(this, "Google Sign-In canceled", Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }
        }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                // Sign in with Google credential
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Update Firestore with user info if needed
                        val email = account.email.toString()
                        val name = account.displayName.toString()
                        saveUserInfoToDatabase(email, name, true)

                        // Continue with your logic (e.g., navigate to MainActivity)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        dismissProgressDialog()
                    }
                }
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            dismissProgressDialog()
        }
    }

    private fun saveUserInfoToDatabase(
        email: String,
        name: String,
        isEmailVerified: Boolean = false
    ) {
        // Get the unique user ID generated by Firebase Authentication
        val userId = firebaseAuth.currentUser?.uid

        // Create a User object with the provided information
        val user = User(name, email, isEmailVerified)

        // Assuming you have a reference to the "users" collection
        val usersCollection = firestore.collection("users")

        // Create a document reference using the user ID
        val userDocument = usersCollection.document(userId ?: "")

        userDocument.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                // The document does not exist, so create it
                userDocument.set(user)
            } else {
                // The document already exists, so update it
                userDocument.update("name", name, "email", email, "emailVerified", isEmailVerified)
            }
        }.addOnFailureListener { exception ->
            // Handle errors here
            Toast.makeText(this, "Failed to save data: $exception", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateEmailVerificationStatus(email: String) {
        val userId = firebaseAuth.currentUser?.uid

        // Update emailVerified to true in the Firestore collection "users" for the user with the unique user ID
        userId?.let {
            firestore.collection("users").document(it).update("emailVerified", true)
                .addOnSuccessListener {
                    // Successfully updated emailVerified status
                }
                .addOnFailureListener { exception ->
                    // Handle errors here
                    Toast.makeText(
                        this,
                        "Failed to update email verification status: $exception",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}