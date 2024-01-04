package com.example.prepnimcet

import android.os.Bundle
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepnimcet.databinding.FragmentHomeBinding
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var blogList:ArrayList<BlogData>
    private lateinit var blogAdapter: BlogAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recyclerview?.setHasFixedSize(true)
        binding?.recyclerview?.layoutManager =LinearLayoutManager(context)

        blogList= ArrayList()
        blogList.add(BlogData("NIMCET Exam Date","NIMCET 2024: The authority will release the NIMCET 2024 exam dates tentatively in February 2024. The NIMCET 2024 exam is expected to be conducted in June 2024. The application form will be released tentatively in March 2024."))
        blogList.add(BlogData("NIMCET Exam Date2","2NIMCET 2024: The authority will release the NIMCET 2024 exam dates tentatively in February 2024. The NIMCET 2024 exam is expected to be conducted in June 2024. The application form will be released tentatively in March 2024."))
        blogList.add(BlogData("NIMCET Exam Date3","3NIMCET 2024: The authority will release the NIMCET 2024 exam dates tentatively in February 2024. The NIMCET 2024 exam is expected to be conducted in June 2024. The application form will be released tentatively in March 2024."))
        blogList.add(BlogData("NIMCET Exam Date4","4NIMCET 2024: The authority will release the NIMCET 2024 exam dates tentatively in February 2024. The NIMCET 2024 exam is expected to be conducted in June 2024. The application form will be released tentatively in March 2024."))

        blogAdapter= BlogAdapter(blogList)
        binding?.recyclerview?.adapter =blogAdapter

        blogAdapter.onItemClick={
            val intent=Intent(context,DetailBlogActivity::class.java)
            intent.putExtra("blog",it)
            startActivity(intent)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}