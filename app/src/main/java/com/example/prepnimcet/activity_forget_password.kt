package com.example.prepnimcet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.prepnimcet.databinding.ActivityForgetPasswordBinding
import com.example.prepnimcet.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class activity_forget_password : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        val title = "Reset Link Send"
        val detail =
            "We have sent a password reset link to your email address. Please check your email and click on the link to reset your password. If you don't see the email, check other places it might be, like your junk, spam, social, or other folders."

        binding.btnArrowleft.setOnClickListener {
            finish()
        }

        binding.btnSendLink.setOnClickListener {
            val email = binding.etForgotEmail.text.toString()
            if (email.isNotEmpty()) {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, activity_password_reset::class.java)
                        intent.putExtra("title", title)
                        intent.putExtra("detail", detail)
                        startActivity(intent)
                        finish()
                    } else {
                        when (it.exception) {
                            is FirebaseAuthInvalidUserException -> {
                                Toast.makeText(
                                    this, "The email address is not registered.", Toast.LENGTH_SHORT
                                ).show()
                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG)
                                    .show()
                            }

                        }
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Kindly enter the email address", Toast.LENGTH_SHORT).show()
            }
        }
    }
}