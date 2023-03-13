package com.guerin.velovify

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.guerin.velovify.databinding.ActivityMainBinding
import com.guerin.velovify.ui.connection.LoginActivity
import com.guerin.velovify.ui.connection.RegisterActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        user = auth.currentUser

        updateUI(user)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_list,
                R.id.navigation_map,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_list -> {
                    navController.navigate(R.id.navigation_list)
                    true
                }
                R.id.navigation_map -> {
                    navController.navigate(R.id.navigation_map)
                    true
                }
                R.id.navigation_notifications -> {
                    navController.navigate(R.id.navigation_notifications)
                    true
                }
                else -> false
            }
        }
    }

    fun changeFragment(id: Int) {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(id)
    }

    override fun onResume() {
        super.onResume()
        user = auth.currentUser
        updateUI(user)
    }

    fun updateUI(user: FirebaseUser?) {

        if (user != null) {
            binding.emailUser.text = user.email
            binding.buttonSignin.visibility = View.GONE

            binding.buttonRegister.text = "Logout"
            binding.buttonRegister.setOnClickListener {
                Firebase.auth.signOut()
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        } else {
            binding.emailUser.text = ""

            binding.buttonSignin.visibility = View.VISIBLE
            binding.buttonSignin.setOnClickListener{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                updateUI(auth.currentUser)
            }

            binding.buttonRegister.text = "Register"
            binding.buttonRegister.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                updateUI(auth.currentUser)
            }
        }
    }
}