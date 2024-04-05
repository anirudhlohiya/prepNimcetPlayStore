package com.anirudh.prepnimcet

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.anirudh.prepnimcet.databinding.ActivityQuestionsBinding
import com.google.firebase.firestore.FirebaseFirestore

class QuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityQuestionsBinding

    private lateinit var database: FirebaseFirestore
    private var questionList = ArrayList<Questions>()
    private var index = 0
    private var currentQuestionIndex = 0
    private var timer: CountDownTimer? = null
    private var answerSelected = false
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.option1.setOnClickListener(this)
        binding.option2.setOnClickListener(this)
        binding.option3.setOnClickListener(this)
        binding.option4.setOnClickListener(this)

        database = FirebaseFirestore.getInstance()
        val catId = intent.getStringExtra("catId")

        // Fetch questions from Firebase
        database.collection("quizzes")
            .document(catId!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val quizTitle = documentSnapshot.getString("QuizTitle")
                    if (quizTitle != null) {
                        val questionsRef = database.collection("quizzes")
                            .document(catId)
                            .collection(quizTitle)
                            .document("questions")

                        // Generate a list of 5 random numbers between 1 and 15
                        val randomNumbers = (1..15).shuffled().take(5)

                        var fetchCounter = 0
                        for (i in randomNumbers) {
                            val questionRef = questionsRef.collection("q$i")
                            questionRef.get().addOnSuccessListener { questionSnapshot ->
                                for (doc in questionSnapshot.documents) {
                                    val questionData = doc.toObject(Questions::class.java)
                                    if (questionData != null) {
                                        questionList.add(questionData)
                                    }
                                }
                                fetchCounter++
                                if (fetchCounter == 5) {
                                    // Shuffle the questionList and start the quiz after all questions have been fetched
                                    questionList.shuffle()
                                    startQuiz()
                                    startTimer()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Quiz title is null.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Quiz does not exist.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        binding.btnNext.setOnClickListener {
            setNextQuestion()
        }


    }

    private fun startTimer() {
        if (timer != null) {
            timer?.cancel()
        }
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                // Move to the next question automatically
                setNextQuestion()
                startTimer() // Restart the timer for the next question
            }
        }.start()
    }

    private fun showCorrectAnswer() {
        val question = questionList[index - 1] // Get the current question
        when (question.answer) {
            "A" -> binding.option1.background =
                ContextCompat.getDrawable(this, R.drawable.blue_button_bg)

            "B" -> binding.option2.background =
                ContextCompat.getDrawable(this, R.drawable.blue_button_bg)

            "C" -> binding.option3.background =
                ContextCompat.getDrawable(this, R.drawable.blue_button_bg)

            "D" -> binding.option4.background =
                ContextCompat.getDrawable(this, R.drawable.blue_button_bg)
        }
    }

    private fun checkAnswer(selectedOption: String) {
        Log.d("QuestionsActivity", "checkAnswer called with selectedOption: $selectedOption")
        if (!answerSelected && currentQuestionIndex < questionList.size) {
            val question = questionList[currentQuestionIndex] // Get the current question
            Log.d("QuestionsActivity", "Current question: $question")
            if (question.answer == selectedOption) {
                // User selected the correct answer
                Log.d("QuestionsActivity", "User selected the correct answer")
                score++
                when (selectedOption) {
                    "A" -> binding.option1.background =
                        ContextCompat.getDrawable(this, R.drawable.blue_button_bg)

                    "B" -> binding.option2.background =
                        ContextCompat.getDrawable(this, R.drawable.blue_button_bg)

                    "C" -> binding.option3.background =
                        ContextCompat.getDrawable(this, R.drawable.blue_button_bg)

                    "D" -> binding.option4.background =
                        ContextCompat.getDrawable(this, R.drawable.blue_button_bg)
                }
            } else {
                Log.d("QuestionsActivity", "User selected the wrong answer")
                // User selected the wrong answer
                when (selectedOption) {
                    "A" -> binding.option1.background =
                        ContextCompat.getDrawable(this, R.drawable.red_button_bg)

                    "B" -> binding.option2.background =
                        ContextCompat.getDrawable(this, R.drawable.red_button_bg)

                    "C" -> binding.option3.background =
                        ContextCompat.getDrawable(this, R.drawable.red_button_bg)

                    "D" -> binding.option4.background =
                        ContextCompat.getDrawable(this, R.drawable.red_button_bg)
                }
                // Show the correct answer in blue
                showCorrectAnswer()
            }
            // Set answerSelected to true after processing the answer
            answerSelected = true
            // Increment currentQuestionIndex after a question has been answered
            currentQuestionIndex++
        } else if (currentQuestionIndex < questionList.size) {
            // User has already selected an answer
            Log.d("QuestionsActivity", "User has already selected an answer")
            Toast.makeText(this, "You have already selected an answer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startQuiz() {
        Log.d("QuestionsActivity", "startQuiz called")
        setNextQuestion()
    }

    private fun setNextQuestion() {
        if (index < questionList.size) {
            val question = questionList[index]
            with(binding) {
                val displayIndex = if (index >= 10) index + 1 - 10 else index + 1
                txtProgress.text = "$displayIndex/${questionList.size}"
                txtQuestion.text = question.question
                option1.text = question.optionA
                option2.text = question.optionB
                option3.text = question.optionC
                option4.text = question.optionD
                binding.pbProgress.progress = ((index + 1) * 20)
                btnNext.text = if (index == questionList.size - 1) "Submit" else "Next"
            }
            resetOptionBackgrounds()
            if (index < questionList.size) {
                answerSelected = false // Reset answerSelected for the new question
            }
            index++
            if (index < questionList.size) {
                startTimer()
            }
        } else {
            // send the user to the result activity with the score
            val intent = Intent(this, QuizResult::class.java)
            intent.putExtra("score", score)
            startActivity(intent)
            finish()
            // Reset answerSelected when the quiz is completed
            answerSelected = false
        }
    }


    private fun resetOptionBackgrounds() {
        binding.option1.background = ContextCompat.getDrawable(this, R.drawable.gray_button_bg)
        binding.option2.background = ContextCompat.getDrawable(this, R.drawable.gray_button_bg)
        binding.option3.background = ContextCompat.getDrawable(this, R.drawable.gray_button_bg)
        binding.option4.background = ContextCompat.getDrawable(this, R.drawable.gray_button_bg)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the timer to avoid memory leaks
        timer?.cancel()
    }

    override fun onClick(v: View?) {
        Log.d("QuestionsActivity", "onClick called with view ID: ${v?.id}")
        when (v?.id) {
            R.id.option1 -> {
                checkAnswer("A")
            }

            R.id.option2 -> {
                checkAnswer("B")
            }

            R.id.option3 -> {
                checkAnswer("C")
            }

            R.id.option4 -> {
                checkAnswer("D")
            }

            else -> {
                Log.d("QuestionsActivity", "onClick: Invalid view ID")
            }
        }
    }

}