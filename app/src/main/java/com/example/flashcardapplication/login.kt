package com.example.flashcardapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.flashcardapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        // Check if user is already logged In
        if (auth.currentUser != null) {
            val intent = Intent(this@login, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("userId", auth.currentUser!!.uid.toString())
            startActivity(intent)
            finish()
        }

        binding.createAcc.setOnClickListener {
            val intent = Intent(this@login, signup::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val pass = binding.loginPass.text.toString().trim()

            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {
                val intent = Intent(this@login, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("userId", auth.currentUser!!.uid.toString())
                startActivity(intent)
                finish()
            }
        }
    }
}