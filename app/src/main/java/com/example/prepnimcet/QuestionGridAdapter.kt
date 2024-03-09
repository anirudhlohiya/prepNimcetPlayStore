package com.example.prepnimcet

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import androidx.core.content.ContextCompat

class QuestionGridAdapter(
    private val context: Context,
    private val mockTestQuestionStateList: List<MockTestQuestionState>
) : BaseAdapter() {

    override fun getCount(): Int {
        return mockTestQuestionStateList.size
    }

    override fun getItem(position: Int): Any {
        return mockTestQuestionStateList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val button = Button(context)
        val questionState = mockTestQuestionStateList[position]
        val questionNumber = "Question ${position + 1}"

        button.text = questionNumber
        button.setTextColor(getTextColor(questionState))
        button.setBackgroundColor(ContextCompat.getColor(context, getBackgroundColor(questionState)))
        button.setOnClickListener {
            // Handle click event if needed
        }
        return button
    }

    private fun getTextColor(mockTestQuestionState: MockTestQuestionState): Int {
        return when (mockTestQuestionState) {
            MockTestQuestionState.ANSWERED -> ContextCompat.getColor(context, R.color.green)
            MockTestQuestionState.VISITED -> ContextCompat.getColor(context, R.color.gray)
            else -> ContextCompat.getColor(context, R.color.black)
        }
    }

    private fun getBackgroundColor(mockTestQuestionState: MockTestQuestionState): Int {
        return android.R.color.transparent // You can set the background color as needed
    }
}
