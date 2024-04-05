package com.anirudh.prepnimcet

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.anirudh.prepnimcet.databinding.ActivityDetailBlogBinding

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBlogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityDetailBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.color6)))
        setValuesOnViews()
    }

    private fun setValuesOnViews() {
        binding.detailedBlogTitle.text=intent.getStringExtra("title")
        binding.detailedBlogAuthor.text=intent.getStringExtra("author")
        binding.detailedBlogDate.text=intent.getStringExtra("date")
        binding.detailedBlogPara1.text=intent.getStringExtra("para1")
        binding.detailedBlogPara2.text=intent.getStringExtra("para2")
        binding.detailedBlogPara3.text=intent.getStringExtra("para3")
    }
}