package com.example.prepnimcet

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.prepnimcet.databinding.ActivityRegisteerBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class activity_register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisteerBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisteerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val dialog = ProgressDialog(this)
        dialog.setMessage("We're creating your account")

        binding.btnArrowleft.setOnClickListener {
            finish()
        }

        binding.txtLoginNow.setOnClickListener {
            val intent = Intent(this, activity_login::class.java)
            startActivity(intent)
            finish()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.imageGoogle.setOnClickListener {
            showProgressDialog()
            signInGoogle()
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etSignUpEmail.text.toString()
            val pass = binding.etSignUpPassword.text.toString()
            val cnfPass = binding.etSignUpCnfPassword.text.toString()
            val name = binding.etSignUpName.text.toString() // Assuming you have a name field

            if (email.isNotEmpty() && pass.isNotEmpty() && cnfPass.isNotEmpty() && name.isNotEmpty()) {
                if (pass == cnfPass) {
                    dialog.show()
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                dialog.dismiss()
                                // Save user info to the Firestore database
                                saveUserInfoToDatabase(email, name)

                                // Send email verification
                                firebaseAuth.currentUser?.sendEmailVerification()
                                    ?.addOnCompleteListener { emailTask ->
                                        if (emailTask.isSuccessful) {
                                            val intent =
                                                Intent(this, activity_verification::class.java)
                                            startActivity(intent)
                                            finish()
//                                            Toast.makeText(this, "Link Sent Successfully", Toast.LENGTH_LONG).show()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                emailTask.exception.toString(),
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                            } else {
                                dialog.dismiss()
                                Toast.makeText(
                                    this, authTask.exception.toString(), Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this, "Password and Confirm password are not the same", Toast.LENGTH_LONG
                    ).show()
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

    private val launcherGoogle =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            dismissProgressDialog() // Dismiss the progress dialog when the activity result is received

            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
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


    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            } else {
                Toast.makeText(this, "Sign in with google canceled", Toast.LENGTH_LONG).show()
                dismissProgressDialog()
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            dismissProgressDialog()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                // Save user info to the Firestore database
                saveUserInfoToDatabase(
                    account.email.toString(),
                    account.displayName.toString(),
                    true
                )
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                // finish all the activities in the background
                finishAffinity()
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }
        }
    }
}