package com.example.prepnimcet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.prepnimcet.databinding.ActivityForgetPasswordBinding
import com.example.prepnimcet.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class activity_forget_password : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnArrowleft.setOnClickListener {
            finish()
        }

        binding.btnSendLink.setOnClickListener {
            val email = binding.etForgotEmail.text.toString()
            if (email.isNotEmpty()) {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Email sent", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, activity_password_reset::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Kindly enter the email address", Toast.LENGTH_LONG).show()
            }
        }
    }
}