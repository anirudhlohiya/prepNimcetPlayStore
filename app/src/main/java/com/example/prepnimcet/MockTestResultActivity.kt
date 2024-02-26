package com.example.prepnimcet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.prepnimcet.databinding.ActivityMockTestResultBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.TimeUnit

class MockTestResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMockTestResultBinding
    private lateinit var mockTestQuestionData: ArrayList<MockTestQuestionData>
    private var correctAnswer = 0
    private var incorrectAnswer = 0
    private var unAttemptAnswer = 0
    private var marks = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMockTestResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setResult()
    }

    private fun setResult() {
        // Retrieve the serialized JSON string from the intent extra
        val mocktestData: String? = intent.getStringExtra("MockTestData")
        // Deserialize the JSON string back into a list of MockTestQuestionData objects using Gson
        mockTestQuestionData = Gson().fromJson(
            mocktestData,
            object : TypeToken<ArrayList<MockTestQuestionData>>() {}.type
        )

        setQuestionNumber()
        //Set the how much time is taken for mock test
        setTotalTime()
        binding.close?.setOnClickListener {
            val mocktestIntent = Intent(this, MainActivity::class.java)
            startActivity(mocktestIntent)

        }
    }

    private fun setQuestionNumber() {
        //Iterate over the list and compare userAnswer with answer for each set of answers
        for ((index, mockTestQuestionData) in mockTestQuestionData.withIndex()) {
            val userAnswer = mockTestQuestionData.userAnswer
            val answer = mockTestQuestionData.answer
            // Check if the user's answer matches the correct answer for the current set
            when (userAnswer) {
                answer -> {
                    // If they match, increment the correct count
                    correctAnswer++
                    marks += 8
                }

                null -> {
                    //If they have null,increment the un-attempt count
                    unAttemptAnswer++
                }

                else -> {
                    // If they don't match, increment the incorrect count
                    incorrectAnswer++
                }
            }
        }
        binding.totalCorrectAnswer?.text = "$correctAnswer"
        binding.totalIncorrectAnswer?.text = "$incorrectAnswer"
        binding.totalUnattemptedQuestion?.text = "$unAttemptAnswer"
        binding.totalScore?.text = "$marks"
    }


    private fun setTotalTime() {
        // Retrieve the elapsed time from intent extras
        val elapsedTimeMillis: Long = intent.getLongExtra("ElapsedTime", 0)

        // Convert milliseconds to hours, minutes, and seconds
        val hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis) % 60

        // Format the elapsed time as "hours:minutes:seconds"
        val formattedTime = String.format("%02d hours %02d min %02d sec", hours, minutes, seconds)

        // Display the countdown time on the screen
        binding.totalTime?.text = formattedTime
    }

    override fun onBackPressed() {
        val backIntent = Intent(this, MainActivity::class.java)
        startActivity(backIntent)
        finish() // Call finish to close the current activity
        super.onBackPressed()
    }

}