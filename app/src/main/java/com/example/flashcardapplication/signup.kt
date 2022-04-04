package com.example.flashcardapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.flashcardapplication.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
var auth = FirebaseAuth.getInstance()
class signup : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    val db = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (auth.currentUser != null) {
            val intent = Intent(this@signup, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("userId", auth.currentUser!!.uid.toString())
            startActivity(intent)
            finish()
        }

        binding.goToLogin.setOnClickListener {
            val intent = Intent(this@signup, login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.signBtn.setOnClickListener {
            if (binding.signName.text.isEmpty()) {
                Toast.makeText(applicationContext, "Please Enter Name", Toast.LENGTH_SHORT).show()
            }
            else if (binding.signEmail.text.isEmpty()) {
                Toast.makeText(applicationContext, "Please Enter Email", Toast.LENGTH_SHORT).show()
            }
            else if (binding.signPass.text.isEmpty()) {
                Toast.makeText(applicationContext, "Please Enter Password", Toast.LENGTH_SHORT).show()
            }
            else if (binding.signConfirmPass.text.isEmpty()) {
                Toast.makeText(applicationContext, "Please Enter Name", Toast.LENGTH_SHORT).show()
            }
            else if (binding.signConfirmPass.text.toString() != binding.signPass.text.toString()){
                Toast.makeText(applicationContext, "Password Doesn't Match", Toast.LENGTH_SHORT).show()
            }
            else {
                val name = binding.signName.text.toString().trim()
                val email = binding.signEmail.text.toString().trim()
                val pass = binding.signPass.text.toString().trim()

                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user=auth.currentUser
                            val ref = db.getReference("Users")
                            ref.child(user!!.uid.toString()).setValue(Users(user.uid.toString(), name))
                            val intent = Intent(this@signup, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("userId", user.uid.toString())
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener(){
                        Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}