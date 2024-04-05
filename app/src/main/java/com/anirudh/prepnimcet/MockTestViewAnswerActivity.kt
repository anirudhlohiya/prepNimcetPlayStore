package com.anirudh.prepnimcet

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.anirudh.prepnimcet.databinding.ActivityMockTestViewAnswerBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MockTestViewAnswerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMockTestViewAnswerBinding
    private lateinit var mockTestQuestionDataList: ArrayList<MockTestQuestionDataForResult>
    private var index = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMockTestViewAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAnswerView()
    }

    private fun setAnswerView() {
        val mockTestData: String? = intent.getStringExtra("MockTestData")
        mockTestQuestionDataList = Gson().fromJson(
            mockTestData,
            object : TypeToken<ArrayList<MockTestQuestionDataForResult>>() {}.type
        )
        Log.d("Mock Test", mockTestData.toString())

        val builder = StringBuilder("")
        for (questionData in mockTestQuestionDataList) {
            val questionNumber = index++
            builder.append("<font color'#18206F'><b>$questionNumber" + ".</br>${questionData.question}</b></font><br/><br/>")
            builder.append("<font color='#009688'>Answer : ${questionData.answer}</font><br/><br/>")
            if (!questionData.userAnswer.isNullOrEmpty()) {
                if (questionData.answer == questionData.userAnswer) {
                    builder.append("<font color='#00C853'>Your Answer : ${questionData.userAnswer}</font><br/><br/>")
                } else {
                    builder.append("<font color='#FF0000'>Your Answer : ${questionData.userAnswer}</font><br/><br/>")
                }
            } else {
                builder.append("<font color='#FF0000'>Your Answer : Unattempted</font><br/><br/>")
            }
        }
        binding.txtAnswer.text = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT)

    }
}