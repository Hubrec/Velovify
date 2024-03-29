package com.guerin.velovify.ui.connection

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.guerin.velovify.MainActivity
import com.guerin.velovify.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val username = binding.username
        val password = binding.password
        val login = binding.buttonLogin

        login.isEnabled = username.toString() != "" && password.toString() != ""

        login.setOnClickListener {
            auth.signInWithEmailAndPassword(username.text.toString().trim(), password.text.toString().trim())
            .addOnSuccessListener {
                Toast.makeText(baseContext, "You are now connected",
                    Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("user", it.user)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, "Error: ${it.message}", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}