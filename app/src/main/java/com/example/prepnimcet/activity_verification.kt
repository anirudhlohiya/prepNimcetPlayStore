package com.example.prepnimcet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prepnimcet.databinding.ActivityVerificationBinding // Import the correct binding class

class activity_verification : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationBinding // Use the correct binding class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityVerificationBinding.inflate(layoutInflater) // Use the correct binding class
        setContentView(binding.root)

        // Now you can access the views from the binding object
        binding.btnBackToLogin.setOnClickListener {
            // Add your button click logic here
            // For example, you can navigate back to the login activity
            val intent = Intent(this, activity_login::class.java)
            startActivity(intent)
            finish()
        }
    }
}