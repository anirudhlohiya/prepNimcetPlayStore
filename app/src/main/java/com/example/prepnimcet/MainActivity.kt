package com.example.prepnimcet

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.prepnimcet.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open_nav, R.string.close_nav)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.color6)))
        //Code for Navigation Drawer
        binding.navigationDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.share_icon -> Toast.makeText(applicationContext, "Share", Toast.LENGTH_SHORT)
                    .show()

                R.id.feedback_icon -> Toast.makeText(
                    applicationContext,
                    "FeedBack",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.rate_us_icon -> Toast.makeText(
                    applicationContext,
                    "Rate Us",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.above_icon -> Toast.makeText(applicationContext, "Above", Toast.LENGTH_SHORT)
                    .show()

                R.id.logout_icon -> Toast.makeText(
                    applicationContext,
                    "Logout Icon",
                    Toast.LENGTH_SHORT
                ).show()
            }
            //Code for Close Navigation Drawer after one item is click
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        //Code for Bottom Navigation Bar
        binding.bottomNavigationBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_bar_icon -> {
                    loadFragment(HomeFragment.newInstance())
                }

                R.id.quiz_bar_icon -> {
                    loadFragment(QuizFragment.newInstance())
                }

                R.id.mock_test_bar_icon -> {
                    loadFragment(MockTestFragment.newInstance())
                }

                R.id.profile_bar_icon -> {
                    loadFragment(ProfileFragment.newInstance())
                }
            }
            true
        }
        //Default Bottom bar option will be select by this
        binding.bottomNavigationBar.selectedItemId = R.id.home_bar_icon

    }

    private fun showExitConfirmationDialog() {
        val exitDialog = ExitConfirmationDialogFragment()
        exitDialog.show(supportFragmentManager, "exit_dialog")
    }

    //Function for replace fragment with other fragment
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    //Code for Navigation Drawer item click listener
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    //Code for Back Pressed Button Action
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}