package com.example.prepnimcet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prepnimcet.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth

class activity_welcome : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            if (firebaseAuth.currentUser?.isEmailVerified == true) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else { //give user a toast to first verify email
                Toast.makeText(
                    this, "Kindly Verify Your Email", Toast.LENGTH_LONG
                ).show()
            }
        }


        binding.btnLogin.setOnClickListener {
            // Start the LoginActivity when the Login button is clicked
            val intent = Intent(this, activity_login::class.java)
            startActivity(intent)
//            finish()
        }

        binding.btnRegister.setOnClickListener {
            // Start the RegisterActivity when the Register button is clicked
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
//            finish()
        }
    }
}