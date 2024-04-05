package com.anirudh.prepnimcet

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anirudh.prepnimcet.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.updateDetailsBtn.visibility = View.VISIBLE

        // Initialize Firebase Firestore
        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        // Get the current authenticated user
        val user = auth.currentUser

        // Get the user's data from Firestore
        if (user != null) {
            firestore.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name")
                        val email = document.getString("email")
                        val mobileNumber = document.getString("mobileNumber") ?: ""
                        val course = document.getString("course") ?: ""
                        val dob = document.getString("dob") ?: "Click to Select Your Date of Birth"

                        binding.nameBox.setText(name)
                        binding.emailBox.setText(email)
                        binding.mobileNumberBox.setText(mobileNumber)
                        binding.courseSpinner.setSelection(getIndex(binding.courseSpinner, course))
                        binding.datePickerTrigger.text = dob

                        binding.emailBox.isEnabled = false

                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error: $exception", Toast.LENGTH_SHORT).show()
                }
        }

        val courses = arrayOf("BCA", "BBA", "B.COM")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, courses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner: Spinner = view.findViewById(R.id.courseSpinner)
        spinner.adapter = adapter

        val datePickerTrigger: TextView = view.findViewById(R.id.datePickerTrigger)
        datePickerTrigger.visibility = View.VISIBLE
        datePickerTrigger.setOnClickListener {
            showDatePicker()
        }

        val mobileNumberBox: EditText = binding.mobileNumberBox
        val courseSpinner: Spinner = binding.courseSpinner
        val datePickerTriger: TextView = binding.datePickerTrigger
        val updateDetailsBtn: Button = binding.updateDetailsBtn

        courseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                updateDetailsBtn.visibility = View.VISIBLE
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        datePickerTriger.setOnClickListener {
            showDatePicker()
            updateDetailsBtn.visibility = View.VISIBLE
        }

        updateDetailsBtn.setOnClickListener {
            val userMap = hashMapOf<String, Any>()

            val name = binding.nameBox.text.toString()
            val mobileNumber = mobileNumberBox.text.toString()
            val course = courseSpinner.selectedItem.toString()
            val dob = datePickerTrigger.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(context, "Name field cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mobileNumber.isNotEmpty()) {
                if (isValidMobile(mobileNumber)) {
                    userMap["mobileNumber"] = mobileNumber
                } else {
                    Toast.makeText(context, "Invalid mobile number", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            if (course.isNotEmpty()) userMap["course"] = course
            if (dob.isNotEmpty()) userMap["dob"] = dob

            if (user != null) {
                firestore.collection("users").document(user.uid)
                    .set(userMap, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(context, "Details updated successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error: $e", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun isValidMobile(phone: String): Boolean {
        return phone.length == 10
    }

    private fun getIndex(spinner: Spinner, myString: String?): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == myString) {
                return i
            }
        }
        return 0
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val currentDate = Calendar.getInstance()
                val diff = currentDate.timeInMillis - selectedDate.timeInMillis
                val years = TimeUnit.MILLISECONDS.toDays(diff) / 365

                if (years < 18) {
                    Toast.makeText(context, "You must be at least 18 years old", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.datePickerTrigger.text =
                        "$selectedDay/${selectedMonth + 1}/$selectedYear"
                }
            }, year, month, day)

        datePickerDialog.show()
    }


    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}