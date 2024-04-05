package com.anirudh.prepnimcet

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MocktestOptionAdapter(
    val content: Context, private var questionData: MockTestQuestionData
) :
    RecyclerView.Adapter<MocktestOptionAdapter.OptionViewHolder>() {

    private var options: ArrayList<String?> = arrayListOf(
        questionData.optionA,
        questionData.optionB,
        questionData.optionC,
        questionData.optionD
    )

    class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var option: TextView? = itemView.findViewById(R.id.mock_test_option)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view: View =
            LayoutInflater.from(content).inflate(R.layout.mock_test_option_item, parent, false)
        return OptionViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.option?.text = options[position]

        holder.option?.setOnClickListener {
            questionData.userAnswer = options[position]
            notifyDataSetChanged()
        }
        if (questionData.userAnswer == options[position]) {
            holder.option?.setBackgroundResource(R.drawable.mocktest_option_item_selected_bg)
        } else {
            holder.option?.setBackgroundResource(R.drawable.mocktest_option_item_bg)
        }


    }

    override fun getItemCount(): Int {
        return options.size
    }
}
