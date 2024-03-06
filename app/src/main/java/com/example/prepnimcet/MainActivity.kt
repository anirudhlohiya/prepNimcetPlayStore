package com.example.prepnimcet

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.prepnimcet.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        toggle =
            ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open_nav, R.string.close_nav)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.color6
                )
            )
        )

        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                val firestore = FirebaseFirestore.getInstance()
                val userRef = firestore.collection("users").document(user.uid)

                Handler(Looper.getMainLooper()).postDelayed({
                    userRef.get().addOnSuccessListener { document ->
                        val name = document.getString("name")
                        val email = document.getString("email")

                        val navigationView = binding.navigationDrawer
                        val headerView = navigationView.getHeaderView(0)
                        val userName = headerView.findViewById<TextView>(R.id.drawer_user_name)
                        val userEmail = headerView.findViewById<TextView>(R.id.drawer_user_email)

                        userName.text = name
                        userEmail.text = email
                    }
                }, 1000) // delay of 2 seconds
            }
        }


        //Code for Navigation Drawer
        binding.navigationDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.share_icon -> {
                    Toast.makeText(applicationContext, "Share", Toast.LENGTH_SHORT).show()
                }

                R.id.feedback_icon -> feedbackDialog()

                R.id.rate_us_icon -> Toast.makeText(
                    applicationContext,
                    "Rate Us",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.above_icon -> aboutDialog()

                R.id.logout_icon -> {
                    // logout the current user and send him to welcome activity
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, activity_welcome::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            //Code for Close Navigation Drawer after one item is click
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        //Code for Bottom Navigation Bar
        binding.bottomNavigationBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.article_bar_icon -> {
                    loadFragment(ArticleFragment.newInstance())
                }

                R.id.quiz_bar_icon -> {
                    loadFragment(QuizFragment())
                }

                R.id.mock_test_bar_icon -> {
                    loadFragment(MockTestFragment.newInstance())
                }

                R.id.profile_bar_icon -> {
                    loadFragment(ProfileFragment.newInstance())
                }
            }
            true
        }
        //Default Bottom bar option will be select by this
        binding.bottomNavigationBar.selectedItemId = R.id.article_bar_icon

    }

    @SuppressLint("MissingInflatedId")
    private fun feedbackDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_feedback, null)
        dialogBuilder.setView(dialogView)

        val nameField = dialogView.findViewById<EditText>(R.id.feedback_name)
        val emailField = dialogView.findViewById<EditText>(R.id.feedback_email)
        val messageField = dialogView.findViewById<EditText>(R.id.feedback_message)
        val submitButton = dialogView.findViewById<Button>(R.id.feedback_submit)

        val feedbackDialog: AlertDialog = dialogBuilder.create()
        feedbackDialog.show()

        val firestore = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val userRef = firestore.collection("users").document(userId.toString())

        userRef.get().addOnSuccessListener { document ->
            val name = document.getString("name")
            val email = document.getString("email")

            nameField.setText(name)
            emailField.setText(email)

            nameField.isEnabled = false
            emailField.isEnabled = false
        }

        submitButton.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val message = messageField.text.toString()

            if (message.isNotEmpty()) {
                userRef.get().addOnSuccessListener { document ->
                    val feedbackCount =
                        document.data?.filterKeys { it.startsWith("feedback") }?.size ?: 0
                    val newFeedbackField = "feedback${feedbackCount + 1}"

                    val feedbackData = mapOf(
                        "name" to name,
                        "email" to email,
                        newFeedbackField to message,
                        "uid" to userId
                    )

                    val userFeedbackData = mapOf(
                        newFeedbackField to message
                    )

                    val feedbackRef = firestore.collection("feedback").document(userId.toString())

                    userRef.update(userFeedbackData).addOnSuccessListener {
                        feedbackRef.update(feedbackData).addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Your feedback has been successfully submitted",
                                Toast.LENGTH_SHORT
                            ).show()
                            feedbackDialog.dismiss()
                        }.addOnFailureListener {
                            feedbackRef.set(feedbackData).addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Your feedback has been successfully submitted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                feedbackDialog.dismiss()
                            }.addOnFailureListener { e2 ->
                                Toast.makeText(this, "Error: ${e2.message}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Feedback message is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun aboutDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.about_dialog_box, null)
        dialogBuilder.setView(dialogView)
        val aboutDialog: AlertDialog = dialogBuilder.create()
        aboutDialog.show()
        val closeButton = dialogView.findViewById<Button>(R.id.aboutDialogCloseBtn)
        closeButton.setOnClickListener {
            aboutDialog.dismiss()
        }
    }

    private fun showExitConfirmationDialog() {
        val exitDialog = ExitConfirmationDialogFragment()
        exitDialog.show(supportFragmentManager, "exit_dialog")
    }

    //Function for replace fragment with other fragment
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    //Code for Navigation Drawer item click listener
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //Code for Back Pressed Button Action
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}