package com.example.asleassignment.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.asleassignment.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.OkHttpClient

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private var authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Retrieve the Intent that started this activity
        val intent = intent

        // Retrieve the "authToken" extra from the Intent
        authToken = intent.getStringExtra("authToken")
        println("london$authToken")

        // Use the retrieved extra as needed
        if (authToken != null) {
            replaceFragment(NotesFragment())
            // Do something with the authToken
            // For example, log it or use it for authentication
        }



        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_discover -> {
                    replaceFragment(DiscoverFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_notes -> {

                    replaceFragment(NotesFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_matches -> {
                    replaceFragment(MatchesFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_profile -> {
                    replaceFragment(ProfileFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        // Set the initial selected fragment
        replaceFragment(DiscoverFragment())
    }

    private fun replaceFragment(fragment: Fragment) {

        if(authToken != null) {
            val bundle = Bundle()
            bundle.putString("key", authToken) // Replace "key" with the actual key and "value" with the desired value
            fragment.arguments = bundle
        }
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contentFrame, fragment)
        fragmentTransaction.commit()
    }
}