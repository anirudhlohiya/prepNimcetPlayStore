package com.example.prepnimcet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prepnimcet.databinding.ActivityDetailBlogBinding

class DetailBlogActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailBlogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityDetailBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val blog=intent.getParcelableExtra<BlogData>("blog")
        if (blog!=null){
            binding.detailedBlogTitle.text=blog.title
            binding.detailedBlogPara1.text=blog.detail
        }
    }
}