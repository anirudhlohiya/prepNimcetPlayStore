package com.example.prepnimcet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.prepnimcet.databinding.ActivityQuizResultBinding

class QuizResult : AppCompatActivity() {
    private lateinit var binding: ActivityQuizResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = intent.getIntExtra("score", 0)
        binding.score.text = "$score/5"
        binding.textView8.text = getMessageForScore(score)

        binding.sharBtn.setOnClickListener {
            // send the user to the quiz fragment
            val intent = Intent(this, QuizFragment::class.java)
            startActivity(intent)
            finish()
        }

        binding.restaratBtn.setOnClickListener {
            // finish the current activity
            finish()
        }
    }

    private fun getMessageForScore(score: Int): String {
        return when (score) {
            1 -> "Try Harder!"
            2 -> "Not Bad!"
            3 -> "Good Job!"
            4 -> "Great!"
            5 -> "Excellent!"
            else -> "Better Luck Next Time!"
        }
    }

}