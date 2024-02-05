package com.example.prepnimcet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class QuestionAdapter(private var mockTestQuestionData: ArrayList<MockTestQuestionData>) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var question: TextView = itemView.findViewById(R.id.mock_test_question)
        var optionA: Button = itemView.findViewById(R.id.optionA)
        var optionB: Button = itemView.findViewById(R.id.optionB)
        var optionC: Button = itemView.findViewById(R.id.optionC)
        var optionD: Button = itemView.findViewById(R.id.optionD)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.option_item, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val questionPosition = mockTestQuestionData[position]
        holder.question.text = questionPosition.question.toString()
        holder.optionA.text = questionPosition.optionA.toString()
        holder.optionB.text = questionPosition.optionB.toString()
        holder.optionC.text = questionPosition.optionC.toString()
        holder.optionD.text = questionPosition.optionD.toString()
    }

    override fun getItemCount(): Int {
        return mockTestQuestionData.size
    }

}