package com.guerin.velovify

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
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

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    val ActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val usr = result.data?.getSerializableExtra("user")
            Log.i("MainActivity", "RESULT_OK")
            updateUI(user)
        }
    }

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
                ActivityResultLauncher.launch(intent)

            }

            binding.buttonRegister.text = "Register"
            binding.buttonRegister.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                ActivityResultLauncher.launch(intent)
            }
        }
    }

    private fun generateNotification() {
        val notification = NotificationCompat.Builder(this, "channelId")
            .setSmallIcon(R.drawable.velovify)
            .setContentTitle("Velovify")
            .setContentText("Vous avez une nouvelle notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()


        val notificationManager = NotificationManagerCompat.from(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(1, notification)
    }

    fun askPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // C'est ok :)
                generateNotification()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                POST_NOTIFICATIONS
            ) -> {
                // Ouvrir les paramètres
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission requise")
                builder.setMessage("To add this feature you really need to turn on notifications")
                builder.setPositiveButton("Allow them") { dialog, which ->
                    startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    })
                }
                builder.setNegativeButton("Don't allow them") { dialog, which ->
                    dialog.dismiss()
                }
                builder.show()
            }
            else -> {
                // The registered ActivityResultCallback gets the result of this request
                requestPermissionLauncher.launch(
                    POST_NOTIFICATIONS
                )
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            generateNotification()
        } else {
            Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show()
        }
    }
}