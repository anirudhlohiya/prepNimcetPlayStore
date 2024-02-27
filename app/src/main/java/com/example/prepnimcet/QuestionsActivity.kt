package com.example.prepnimcet

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.prepnimcet.databinding.ActivityQuestionsBinding
import com.google.firebase.firestore.FirebaseFirestore

class QuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityQuestionsBinding

    private lateinit var database: FirebaseFirestore
    private var questionList = ArrayList<Questions>()
    private var index = 0
    private var currentQuestionIndex = 0
    private var timer: CountDownTimer? = null
    private var answerSelected = false

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
                        for (i in 1..15) {
                            val questionRef = questionsRef.collection("q$i")
                            questionRef.get().addOnSuccessListener { questionSnapshot ->
                                for (doc in questionSnapshot.documents) {
                                    val questionData = doc.toObject(Questions::class.java)
                                    if (questionData != null) {
                                        questionList.add(questionData)
                                    }
                                }
                                if (questionList.size >= 5) {
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
        val correctAnswer = question.answer
        when (correctAnswer) {
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
        if (!answerSelected) {
            val question = questionList[currentQuestionIndex] // Get the current question
            if (question.answer == selectedOption) {
                // User selected the correct answer
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
            }
            answerSelected = true // Set answerSelected to true after processing the answer
        } else {
            // User has already selected an answer
            Toast.makeText(this, "You have already selected an answer", Toast.LENGTH_SHORT).show()
        }
    }


    private fun startQuiz() {
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
            index++
            currentQuestionIndex++
            resetOptionBackgrounds()
            startTimer()
            answerSelected = false // Reset answerSelected for the new question
        } else {
            // Handle quiz completion
            Toast.makeText(this, "Quiz Completed", Toast.LENGTH_SHORT).show()
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
        }
    }

}