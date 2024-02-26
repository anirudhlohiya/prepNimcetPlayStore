package com.example.prepnimcet

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepnimcet.databinding.ActivityDetailMockTestBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class DetailMockTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMockTestBinding
    private lateinit var mocktestQuestionList: ArrayList<MockTestQuestionData>
    private lateinit var database: FirebaseFirestore
    private lateinit var mocktestOptionAdapter: MocktestOptionAdapter
    private var currentQuestionIndex: Int = 0

    // Define a variable to store the start time of the countdown timer
    private var startTimeMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMockTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mocktestQuestionList = ArrayList()
        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        fetchQuestionAndOption(id, title)

        countdownTimer()
        binding.btnPrevious.setOnClickListener {
            showPreviousQuestion()
        }

        binding.btnNext.setOnClickListener {
            showNextQuestion()
        }

        binding.btnSubmit.setOnClickListener {
            // Calculate the elapsed time when the submit button is clicked
            val elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis
            submitMockTest(elapsedTimeMillis)
        }
    }

    private fun fetchQuestionAndOption(id: String?, title: String?) {
        for (qNum in 1..120) {
            database = FirebaseFirestore.getInstance()
            database.collection("mocktest").document(id.toString()).collection(title.toString())
                .document("questions").collection("q$qNum").get().addOnSuccessListener { result ->
                    for (document in result) {
                        val questionData = document.toObject(MockTestQuestionData::class.java)
                        mocktestQuestionList.add(questionData)
                        if (mocktestQuestionList.size == 1) {
                            showQuestion(mocktestQuestionList[currentQuestionIndex])
                        }
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
    }

    private fun showQuestion(questionData: MockTestQuestionData) {
        //Set the question counter on View by TextView
        questionCounter()
        //Set the question on View by TextView
        binding.mockTestQuestion.text = questionData.question

        //Set the Options on View by Recycler View
        mocktestOptionAdapter = MocktestOptionAdapter(this, questionData)
        binding.mockTestOptionRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.mockTestOptionRecyclerview.adapter = mocktestOptionAdapter
        binding.mockTestOptionRecyclerview.setHasFixedSize(true)

        //navigate between question and option
        navigationBetweenQuestion()
    }

    private fun navigationBetweenQuestion() {
        // Show/hide buttons based on the current question index
        if (currentQuestionIndex == 0) {
            binding.btnNext.visibility = View.VISIBLE
            binding.btnPrevious.visibility = View.INVISIBLE
            binding.btnSubmit.visibility = View.INVISIBLE
        } else if (currentQuestionIndex == mocktestQuestionList.size - 1) {
            binding.btnPrevious.visibility = View.VISIBLE
            binding.btnNext.visibility = View.INVISIBLE
            binding.btnSubmit.visibility = View.VISIBLE
        } else if (currentQuestionIndex > 0) {
            binding.btnPrevious.visibility = View.VISIBLE
            binding.btnNext.visibility = View.VISIBLE
            binding.btnSubmit.visibility = View.INVISIBLE
        }
    }

    private fun questionCounter() {
        // Update the question counter
        val questionNumber = currentQuestionIndex + 1
//        val totalQuestions = mocktestQuestionList.size
        val totalQuestions = 120
        binding.questionCounter.text =
            getString(R.string.question_counter, questionNumber, totalQuestions)
    }

    private fun countdownTimer() {
        val countdownTimer = object : CountDownTimer(2 * 60 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                binding.countdownTimer.text = formattedTime
            }

            override fun onFinish() {
                binding.countdownTimer.text = getString(R.string.countdown_timer_default)
                //Handle timer finish event here
                submitMockTest(0)
                Toast.makeText(
                    this@DetailMockTestActivity,
                    "The mock test has ended. Time's up!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // Capture the start time when the countdown timer starts
        startTimeMillis = System.currentTimeMillis()
        countdownTimer.start()
    }

    private fun showNextQuestion() {
        if (currentQuestionIndex < mocktestQuestionList.size - 1) {
            currentQuestionIndex++
            showQuestion(mocktestQuestionList[currentQuestionIndex])
        }
    }

    private fun showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--
            showQuestion(mocktestQuestionList[currentQuestionIndex])
        }
    }

    private fun submitMockTest(elapsedTimeMillis: Long) {
        val intent = Intent(this, MockTestResultActivity::class.java)
        val json: String = Gson().toJson(mocktestQuestionList)
        intent.putExtra("MockTestData", json)

        // Pass the elapsed time to MockTestResultActivity
        intent.putExtra("ElapsedTime", elapsedTimeMillis)

        startActivity(intent)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                showExitDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        // Build the alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit Mock Test")
        builder.setMessage("Are you sure you want to exit the mock test?")

        // Set up the buttons
        builder.setPositiveButton("Exit") { dialog, which ->
            super.onBackPressed() // Exit the activity
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Dismiss the dialog
            dialog.dismiss()
        }
        builder.setNeutralButton("Submit") { dialog, which ->
            // Calculate the elapsed time and submit the mock test
            val elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis
            submitMockTest(elapsedTimeMillis)
        }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun showExitDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit Mock Test")
        builder.setMessage("Are you sure you want to exit the mock test?")

        builder.setPositiveButton("Exit") { dialog, which ->
            finish() // Exit the activity
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.setNeutralButton("Submit") { dialog, which ->
            val elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis
            submitMockTest(elapsedTimeMillis)
        }

        val dialog = builder.create()
        dialog.show()
    }

}