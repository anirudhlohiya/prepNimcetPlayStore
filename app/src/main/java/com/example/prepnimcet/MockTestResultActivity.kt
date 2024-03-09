package com.example.prepnimcet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.prepnimcet.databinding.ActivityMockTestResultBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class MockTestResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMockTestResultBinding
    private lateinit var mockTestQuestionDataList: ArrayList<MockTestQuestionDataForResult>
    private var correctAnswer = 0
    private var incorrectAnswer = 0
    private var unAttemptAnswer = 0
    private var marks: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMockTestResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Retrieve the serialized JSON string from the intent extra
        val mockTestDataForResult: String? = intent.getStringExtra("MockTestDataForResult")
        setResult(mockTestDataForResult)
        binding.mockTestDate?.text = getCurrentDate()

        binding.viewAnswer?.setOnClickListener {
            val intent = Intent(this, MockTestViewAnswerActivity::class.java)
            intent.putExtra("MockTestData",mockTestDataForResult)
            startActivity(intent)
        }
    }

    private fun setResult(mockTestDataForResult: String?) {
        // Deserialize the JSON string back into a list of MockTestQuestionData objects using Gson
        mockTestQuestionDataList = Gson().fromJson(
            mockTestDataForResult,
            object : TypeToken<ArrayList<MockTestQuestionDataForResult>>() {}.type
        )
        Log.d("Mock Test", mockTestDataForResult.toString())
        setQuestionNumber()
        //Set the how much time is taken for mock test
        setTotalTime()
        binding.close?.setOnClickListener {
            val mocktestIntent = Intent(this, MainActivity::class.java)
            startActivity(mocktestIntent)

        }
    }

    private fun setQuestionNumber() {
        // Calculate marks based on provided rules
        calculateMarks()

        binding.totalCorrectAnswer?.text = "$correctAnswer"
        binding.totalIncorrectAnswer?.text = "$incorrectAnswer"
        binding.totalUnattemptedQuestion?.text = "$unAttemptAnswer"
        binding.totalScore?.text = "$marks"
    }

    private fun calculateMarks() {
        for ((index, mockTestQuestionData) in mockTestQuestionDataList.withIndex()) {
            val userAnswer = mockTestQuestionData.userAnswer
            val answer = mockTestQuestionData.answer

            // Check if the user's answer is null or not
            if (userAnswer == null) {
                unAttemptAnswer++
                continue
            }
            // Check if the user's answer matches the correct answer for the current set
            if (userAnswer == answer) {
                when {
                    index < 50 -> marks += 12
                    index in 50..109 -> marks += 6
                    index in 110..119 -> marks += 4
                }
                correctAnswer++
            } else {
                // Apply negative marking based on question index range
                val negativeMarks = when {
                    index < 50 -> 3
                    index in 50..109 -> 1.5
                    else -> 1
                }
                marks -= negativeMarks.toDouble() // Deduct negative marks for incorrect answer
                incorrectAnswer++
            }
        }
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val backIntent = Intent(this, MainActivity::class.java)
        startActivity(backIntent)
        finish() // Call finish to close the current activity
        super.onBackPressed()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val cal = Calendar.getInstance()
        return dateFormat.format(cal.time)
    }

}