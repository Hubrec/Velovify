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
import com.guerin.velovify.MainActivity
import com.guerin.velovify.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
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
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val username = binding.username
        val password = binding.password
        val register = binding.buttonRegister

        register.isEnabled = username.toString() != "" && password.toString() != ""


        register.setOnClickListener {
            auth.createUserWithEmailAndPassword(username.text.toString().trim(), password.text.toString().trim())
                .addOnSuccessListener {
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(baseContext, "Account created",
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("user", it.user)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                .addOnFailureListener {
                    Snackbar.make(binding.root, "Error: ${it.message}", Snackbar.LENGTH_SHORT).show()
                }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}