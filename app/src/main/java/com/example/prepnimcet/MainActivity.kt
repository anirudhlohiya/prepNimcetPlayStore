package com.example.prepnimcet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.prepnimcet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog() {
        val exitDialog = ExitConfirmationDialogFragment()
        exitDialog.show(supportFragmentManager, "exit_dialog")
    }

}