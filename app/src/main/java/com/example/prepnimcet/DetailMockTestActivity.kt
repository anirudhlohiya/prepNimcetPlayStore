package com.example.prepnimcet

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepnimcet.databinding.ActivityDetailMockTestBinding
import com.google.firebase.firestore.FirebaseFirestore

class DetailMockTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMockTestBinding
    private lateinit var mocktestQuestionList: ArrayList<MockTestQuestionData>
    private lateinit var mocktestQuestionAdapter: QuestionAdapter
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMockTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        fetchQuestionAndOption(id, title)

        binding.optionList.layoutManager = LinearLayoutManager(this)
        binding.optionList.setHasFixedSize(true)

        mocktestQuestionList = ArrayList()
        mocktestQuestionAdapter = QuestionAdapter(mocktestQuestionList)
        binding.optionList.adapter = mocktestQuestionAdapter

    }

    private fun fetchQuestionAndOption(id: String?, title: String?) {
        for (qNum in 1..120) {
            database = FirebaseFirestore.getInstance()
            database.collection("mocktest")
                .document(id.toString())
                .collection(title.toString())
                .document("questions")
                .collection("q$qNum")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val questionData = document.toObject(MockTestQuestionData::class.java)
                        mocktestQuestionList.add(questionData)
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    mocktestQuestionAdapter.notifyDataSetChanged()
                    binding.optionList.clearFocus()
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
    }
}



