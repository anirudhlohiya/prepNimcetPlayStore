package com.example.prepnimcet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MockTestAdapter(private val mocktestList: ArrayList<MockTestData>) :
    RecyclerView.Adapter<MockTestAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: OnItemClickListener){
        listener=clickListener
    }
    class ViewHolder(itemView: View,clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val mocktestTitle: TextView = itemView.findViewById(R.id.mock_test_title)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.mock_test_recyclerview_item, parent, false)
        return ViewHolder(itemView,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listMocktest = mocktestList[position]
        holder.mocktestTitle.text = listMocktest.title.toString()
    }

    override fun getItemCount(): Int {
        return mocktestList.size
    }

}