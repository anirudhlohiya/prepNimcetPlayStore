package com.anirudh.prepnimcet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArticleAdapter(private val blogList: ArrayList<com.anirudh.prepnimcet.ArticleData>) :
    RecyclerView.Adapter<com.anirudh.prepnimcet.ArticleAdapter.ViewHolder>() {

    private lateinit var myListener: OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener){
        myListener=clickListener
    }

    class ViewHolder(item: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(item) {

        val blogTitle: TextView = item.findViewById(com.anirudh.prepnimcet.R.id.blog_title)
        val blogAuthor: TextView = item.findViewById(com.anirudh.prepnimcet.R.id.blog_author)
        val blogDate: TextView = item.findViewById(com.anirudh.prepnimcet.R.id.blog_date)
        val blogDetail: TextView = item.findViewById(com.anirudh.prepnimcet.R.id.blog_detail)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(com.anirudh.prepnimcet.R.layout.article_item,parent,false)
        return ViewHolder(view, myListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val blogPosition=blogList[position]
        holder.blogTitle.text=blogPosition.title.toString()
        holder.blogAuthor.text=blogPosition.author.toString()
        holder.blogDate.text=blogPosition.date.toString()
        holder.blogDetail.text=blogPosition.para1.toString()

    }

    override fun getItemCount(): Int {
        return blogList.size
    }
}