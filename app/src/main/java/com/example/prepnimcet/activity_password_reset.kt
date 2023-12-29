package com.example.prepnimcet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prepnimcet.databinding.ActivityLoginBinding
import com.example.prepnimcet.databinding.ActivityVerificationBinding

class activity_password_reset : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackToLogin.setOnClickListener {
            // Add your button click logic here
            val intent = Intent(this, activity_login::class.java)
            startActivity(intent)
            finish()
        }
    }
}