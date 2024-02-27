package com.example.prepnimcet

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var splashImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashImage = findViewById(R.id.splash_image_view)
        animateZoomIn()
    }

    private fun animateZoomOut() {
        splashImage.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1000)
            .withEndAction {
                // start a new activity
                startNewActivity()
            }
            .start()
    }

    private fun animateZoomIn() {
        splashImage.animate()
            .scaleX(3.0f)
            .scaleY(3.0f)
            .setDuration(700)
            .withEndAction {
                animateZoomOut()
            }
            .start()
    }

    private fun startNewActivity() {
        startActivity(Intent(this, activity_welcome::class.java))
        finish()
    }
}