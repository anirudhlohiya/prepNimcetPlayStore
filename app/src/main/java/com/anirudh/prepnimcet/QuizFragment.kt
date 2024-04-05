package com.anirudh.prepnimcet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.anirudh.prepnimcet.databinding.FragmentQuizBinding
import com.google.firebase.firestore.FirebaseFirestore

//class QuizFragment : Fragment() {
//    private lateinit var database: FirebaseFirestore
//    private lateinit var binding: FragmentQuizBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentQuizBinding.inflate(inflater, container, false)
//        database = FirebaseFirestore.getInstance()
//
//        val categories = ArrayList<CategoryModel>()
//        val adapter = CategoryAdapter(requireContext(),categories)
//
//    }
//}

class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: CategoryAdapter
    private val categories = ArrayList<CategoryModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseFirestore.getInstance()
        adapter = CategoryAdapter(requireContext(), categories)

        binding.categoryList.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.categoryList.adapter = adapter

        database.collection("quizzes")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                categories.clear()
                value?.documents?.forEach { snapshot ->
                    val model = snapshot.toObject(CategoryModel::class.java)
                    model?.categoryId = snapshot.id
                    model?.let { categories.add(it) }
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}