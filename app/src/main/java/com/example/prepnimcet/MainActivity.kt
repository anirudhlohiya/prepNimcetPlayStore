package com.example.prepnimcet

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.prepnimcet.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        //Code For Bottom Navigation Bar
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
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share_icon -> {Toast.makeText(this,"share app",Toast.LENGTH_LONG).show()}
            R.id.feedback_icon -> {Toast.makeText(this,"Feedback",Toast.LENGTH_LONG).show()}
            R.id.rate_us_icon -> {Toast.makeText(this,"Rate us",Toast.LENGTH_LONG).show()}
            R.id.above_icon -> {Toast.makeText(this,"About",Toast.LENGTH_LONG).show()}
            R.id.logout_icon -> {Toast.makeText(this,"Logout",Toast.LENGTH_LONG).show()}
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        showExitConfirmationDialog()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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
}