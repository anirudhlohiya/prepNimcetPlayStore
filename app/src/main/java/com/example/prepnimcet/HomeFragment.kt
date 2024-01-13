package com.example.prepnimcet

import android.os.Bundle
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepnimcet.databinding.FragmentHomeBinding
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var blogList:ArrayList<BlogData>
    private lateinit var blogAdapter: BlogAdapter
    private lateinit var database:FirebaseFirestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recyclerview?.layoutManager =LinearLayoutManager(context)
        binding?.recyclerview?.setHasFixedSize(true)

        blogList= ArrayList()
        blogAdapter= BlogAdapter(blogList)
        binding?.recyclerview?.adapter =blogAdapter

        fetchBlogData()
        blogAdapter.setOnItemClickListener(object :BlogAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent=Intent(requireContext(),DetailBlogActivity::class.java)

                intent.putExtra("title",blogList[position].title)
                intent.putExtra("author",blogList[position].author)
                intent.putExtra("date",blogList[position].date)
                intent.putExtra("para1",blogList[position].para1)
                intent.putExtra("para2",blogList[position].para2)
                intent.putExtra("para3",blogList[position].para3)
                startActivity(intent)
            }
        })
    }

    private fun fetchBlogData() {
        database= FirebaseFirestore.getInstance()
        database.collection("articles").
                addSnapshotListener(object :EventListener<QuerySnapshot>{
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error!=null){
                            Log.e("FirestoreError",error.message.toString())
                            return
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type==DocumentChange.Type.ADDED){
                                blogList.add(dc.document.toObject(BlogData::class.java))
                            }
                        }
                        blogAdapter.notifyDataSetChanged()
                        binding?.recyclerview?.clearFocus()
                    }

                })
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}