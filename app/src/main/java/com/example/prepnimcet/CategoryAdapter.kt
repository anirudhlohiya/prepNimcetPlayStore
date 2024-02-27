package com.example.prepnimcet;

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private val context: Context,
    private val categoryModels: ArrayList<CategoryModel>
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_quiz_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val model = categoryModels[position]

        holder.txtDescription.text = model.QuizDescription
        holder.txtTitle.text = model.QuizTitle
//        Glide.with(context)
//            .load(model.quizTitle)
//            .into(holder.txtTitle)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, QuestionsActivity::class.java)
            intent.putExtra("catId", model.categoryId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categoryModels.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtQuizTitle)
        val txtDescription: TextView = itemView.findViewById(R.id.txtQuizDescription)
    }
}
