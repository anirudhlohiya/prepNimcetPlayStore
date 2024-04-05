package com.anirudh.prepnimcet

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.anirudh.prepnimcet.databinding.FragmentMockTestBinding
import com.google.firebase.firestore.FirebaseFirestore

class MockTestFragment : Fragment() {
    private var binding: FragmentMockTestBinding? = null
    private lateinit var mocktestList: ArrayList<MockTestData>
    private lateinit var mocktestAdapter: MockTestAdapter
    private lateinit var database: FirebaseFirestore

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMockTestBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.mockTestRecyclerview?.layoutManager = LinearLayoutManager(context)
        binding?.mockTestRecyclerview?.setHasFixedSize(false)
        //Mock Test Adapter
        mocktestList = ArrayList()
        mocktestAdapter = MockTestAdapter(mocktestList)
        binding?.mockTestRecyclerview?.adapter = mocktestAdapter
        fetchMockTestData()

        mocktestAdapter.setOnItemClickListener(object : MockTestAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Check if mocktestList is not empty and position is valid
                    val intent = Intent(requireContext(), DetailMockTestActivity::class.java)
                    intent.putExtra("title", mocktestList[position].title)
                    intent.putExtra("id", mocktestList[position].id)
                    startActivity(intent)
            }
        })
    }

    private fun fetchMockTestData() {
        database = FirebaseFirestore.getInstance()
        database.collection("mocktest")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val mockTestId = document.toObject(MockTestData::class.java)
                    mockTestId.id = document.id
                    val mockTestData = document.toObject(MockTestData::class.java)
                    mockTestData.title = document.data.toString()

                    mocktestList.add(mockTestId)

                    Log.d(TAG, "${document.id} => ${document.data}")

                    // Notify the adapter when mock tests are fetched
                    mocktestAdapter.notifyDataSetChanged()
                    binding?.mockTestRecyclerview?.clearFocus()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MockTestFragment()
    }
}