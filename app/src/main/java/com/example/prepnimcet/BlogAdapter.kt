package com.example.prepnimcet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BlogAdapter(private val blogList: ArrayList<BlogData>) : RecyclerView.Adapter<BlogAdapter.ViewHolder>() {

    var onItemClick:((BlogData)->Unit)?=null

    class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        val blogTitle: TextView = item.findViewById(R.id.blog_title)
        val blogDetail: TextView = item.findViewById(R.id.blog_detail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val blogPosition=blogList[position]
        holder.blogTitle.text=blogPosition.title
        holder.blogDetail.text=blogPosition.detail

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(blogPosition)
        }
    }

    override fun getItemCount(): Int {
        return blogList.size
    }
}